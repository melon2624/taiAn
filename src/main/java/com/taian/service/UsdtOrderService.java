package com.taian.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taian.entity.OrderType;
import com.taian.entity.UsdtOrder;
import com.taian.mapper.UsdtOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * USDT 买卖订单服务（MySQL 持久化）
 */
@Service
public class UsdtOrderService {

    @Autowired
    private UsdtOrderMapper orderMapper;
    @Autowired
    private UsdtInventoryService inventoryService;

    @Transactional(rollbackFor = Exception.class)
    public UsdtOrder addOrder(OrderType type, BigDecimal amount, BigDecimal price, String remark) {
        UsdtOrder o = new UsdtOrder();
        o.setType(type);
        o.setAmount(amount);
        o.setRemark(remark != null ? remark : "");
        o.setCreateTime(LocalDateTime.now());

        if (type == OrderType.BUY || type == OrderType.TRANSFER_IN) {
            if (price == null) throw new IllegalStateException("买入/转入需提供成本价");
            inventoryService.addBatch(price, amount);
            o.setPrice(price);
            o.recalcTotal();
        } else if (type == OrderType.BORROW_IN) {
            // 借入：价格未定填0，先入账可卖，进货后再还
            BigDecimal cost = (price != null && price.compareTo(BigDecimal.ZERO) >= 0) ? price : BigDecimal.ZERO;
            inventoryService.addBatch(cost, amount);
            o.setPrice(cost);
            o.setTotalAmount(BigDecimal.ZERO);
        } else if (type == OrderType.SELL) {
            if (price == null) throw new IllegalStateException("卖出需提供售价");
            com.alibaba.fastjson.JSONObject preview = inventoryService.previewDeduct(amount);
            if (!Boolean.TRUE.equals(preview.getBoolean("sufficient"))) {
                throw new IllegalStateException("库存不足，缺 " + preview.getBigDecimal("shortage") + " USDT");
            }
            BigDecimal costAmount = preview.getBigDecimal("costAmount");
            com.alibaba.fastjson.JSONArray batchesUsed = preview.getJSONArray("batchesUsed");
            boolean usedBorrowed = false;
            if (batchesUsed != null) {
                for (int i = 0; i < batchesUsed.size(); i++) {
                    com.alibaba.fastjson.JSONObject b = batchesUsed.getJSONObject(i);
                    BigDecimal cp = b.getBigDecimal("costPrice");
                    if (cp != null && cp.compareTo(BigDecimal.ZERO) == 0) {
                        usedBorrowed = true;
                        break;
                    }
                }
            }
            inventoryService.deductFifo(amount);
            BigDecimal revenue = amount.multiply(price).setScale(2, RoundingMode.HALF_UP);
            o.setPrice(price);
            o.setCostAmount(costAmount);
            o.setTotalAmount(revenue);
            o.setDeductionDetail(batchesUsed.toJSONString());
            if (usedBorrowed) {
                o.setProfit(BigDecimal.ZERO);
            } else {
                o.setProfit(revenue.subtract(costAmount));
            }
        } else if (type == OrderType.TRANSFER_OUT) {
            // 转出/挪用：只扣库存
            com.alibaba.fastjson.JSONObject preview = inventoryService.previewDeduct(amount);
            if (!Boolean.TRUE.equals(preview.getBoolean("sufficient"))) {
                throw new IllegalStateException("库存不足，缺 " + preview.getBigDecimal("shortage") + " USDT");
            }
            inventoryService.deductFifo(amount);
            o.setPrice(BigDecimal.ZERO);
            o.setTotalAmount(BigDecimal.ZERO);
            o.setDeductionDetail(preview.getJSONArray("batchesUsed").toJSONString());
        } else if (type == OrderType.BORROW_RETURN) {
            // 还借：扣库存，并记录还借成本（用于计算真实利润）
            com.alibaba.fastjson.JSONObject preview = inventoryService.previewDeduct(amount);
            if (!Boolean.TRUE.equals(preview.getBoolean("sufficient"))) {
                throw new IllegalStateException("库存不足，缺 " + preview.getBigDecimal("shortage") + " USDT");
            }
            BigDecimal returnCost = preview.getBigDecimal("costAmount");
            inventoryService.deductFifo(amount);
            o.setPrice(BigDecimal.ZERO);
            o.setTotalAmount(BigDecimal.ZERO);
            o.setCostAmount(returnCost);  // 还借成本 = 使用的批次总成本，用于真实利润计算
            o.setDeductionDetail(preview.getJSONArray("batchesUsed").toJSONString());
        }

        orderMapper.insert(o);
        return o;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrder(Long id) {
        UsdtOrder order = orderMapper.selectById(id);
        if (order == null) return false;

        if (order.getType() == OrderType.BUY || order.getType() == OrderType.TRANSFER_IN || order.getType() == OrderType.BORROW_IN) {
            inventoryService.deductBatch(order.getPrice(), order.getAmount());
        } else if ((order.getType() == OrderType.SELL || order.getType() == OrderType.TRANSFER_OUT || order.getType() == OrderType.BORROW_RETURN)
                && order.getDeductionDetail() != null && !order.getDeductionDetail().isEmpty()) {
            inventoryService.returnBatches(order.getDeductionDetail());
        }

        return orderMapper.deleteById(id) > 0;
    }

    public List<UsdtOrder> listOrders(String typeFilter) {
        LambdaQueryWrapper<UsdtOrder> q = new LambdaQueryWrapper<>();
        if (typeFilter != null && !typeFilter.isEmpty()) {
            q.eq(UsdtOrder::getType, OrderType.valueOf(typeFilter.toUpperCase()));
        }
        q.orderByDesc(UsdtOrder::getCreateTime);
        return orderMapper.selectList(q);
    }

    @Transactional(rollbackFor = Exception.class)
    public UsdtOrder calculateProfit(Long sellOrderId, BigDecimal returnCostAmount) {
        UsdtOrder order = orderMapper.selectById(sellOrderId);
        if (order == null || order.getType() != OrderType.SELL) {
            throw new IllegalStateException("订单不存在或非卖出订单");
        }
        if (Integer.valueOf(1).equals(order.getSettledFromBorrow())) {
            throw new IllegalStateException("该订单已计算过利润");
        }
        if (order.getTotalAmount() == null) {
            throw new IllegalStateException("订单数据异常");
        }
        BigDecimal ownCost = order.getCostAmount() != null ? order.getCostAmount() : BigDecimal.ZERO;
        BigDecimal profit = order.getTotalAmount().subtract(ownCost).subtract(returnCostAmount).setScale(2, RoundingMode.HALF_UP);
        order.setBorrowedReturnCost(returnCostAmount);
        order.setProfit(profit);
        order.setSettledFromBorrow(1);
        orderMapper.updateById(order);
        return order;
    }

    public JSONObject getStats() {
        List<UsdtOrder> orders = orderMapper.selectList(null);
        BigDecimal buyAmount = BigDecimal.ZERO;
        BigDecimal buyTotal = BigDecimal.ZERO;
        BigDecimal sellAmount = BigDecimal.ZERO;
        BigDecimal sellTotal = BigDecimal.ZERO;
        BigDecimal transferOutAmount = BigDecimal.ZERO;
        BigDecimal transferInAmount = BigDecimal.ZERO;
        BigDecimal borrowInAmount = BigDecimal.ZERO;
        BigDecimal borrowReturnAmount = BigDecimal.ZERO;

        for (UsdtOrder o : orders) {
            BigDecimal amt = o.getAmount() != null ? o.getAmount() : BigDecimal.ZERO;
            switch (o.getType()) {
                case BUY:
                    buyAmount = buyAmount.add(amt);
                    buyTotal = buyTotal.add(o.getTotalAmount() != null ? o.getTotalAmount() : BigDecimal.ZERO);
                    break;
                case SELL:
                    sellAmount = sellAmount.add(amt);
                    sellTotal = sellTotal.add(o.getTotalAmount() != null ? o.getTotalAmount() : BigDecimal.ZERO);
                    break;
                case TRANSFER_OUT:
                    transferOutAmount = transferOutAmount.add(amt);
                    break;
                case TRANSFER_IN:
                    transferInAmount = transferInAmount.add(amt);
                    break;
                case BORROW_IN:
                    borrowInAmount = borrowInAmount.add(amt);
                    break;
                case BORROW_RETURN:
                    borrowReturnAmount = borrowReturnAmount.add(amt);
                    break;
            }
        }

        BigDecimal buyAvg = buyAmount.compareTo(BigDecimal.ZERO) > 0
                ? buyTotal.divide(buyAmount, 4, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
        BigDecimal sellAvg = sellAmount.compareTo(BigDecimal.ZERO) > 0
                ? sellTotal.divide(sellAmount, 4, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
        BigDecimal totalProfit = orders.stream()
                .filter(o -> o.getType() == OrderType.SELL && o.getProfit() != null)
                .map(UsdtOrder::getProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal borrowedSellRevenue = orders.stream()
                .filter(o -> o.getType() == OrderType.SELL && o.getCostAmount() != null && o.getCostAmount().compareTo(BigDecimal.ZERO) == 0 && o.getTotalAmount() != null)
                .map(UsdtOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal borrowReturnCost = orders.stream()
                .filter(o -> o.getType() == OrderType.BORROW_RETURN && o.getCostAmount() != null)
                .map(UsdtOrder::getCostAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal allocatedReturnCost = orders.stream()
                .filter(o -> o.getType() == OrderType.SELL && Integer.valueOf(1).equals(o.getSettledFromBorrow()) && o.getBorrowedReturnCost() != null)
                .map(UsdtOrder::getBorrowedReturnCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal profit = totalProfit;
        BigDecimal trueProfit = totalProfit.add(borrowedSellRevenue).subtract(borrowReturnCost.subtract(allocatedReturnCost));
        BigDecimal holdAmount = inventoryService.getTotalAmount();

        JSONObject stats = new JSONObject();
        stats.put("buyAmount", buyAmount);
        stats.put("buyTotal", buyTotal);
        stats.put("buyAvgPrice", buyAvg);
        stats.put("sellAmount", sellAmount);
        stats.put("sellTotal", sellTotal);
        stats.put("sellAvgPrice", sellAvg);
        stats.put("profit", profit);
        stats.put("borrowReturnCost", borrowReturnCost);
        stats.put("trueProfit", trueProfit);
        stats.put("holdAmount", holdAmount);
        stats.put("transferOutAmount", transferOutAmount);
        stats.put("transferInAmount", transferInAmount);
        stats.put("borrowInAmount", borrowInAmount);
        stats.put("borrowReturnAmount", borrowReturnAmount);
        return stats;
    }
}
