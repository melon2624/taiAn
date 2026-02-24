package com.taian.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taian.entity.InventoryBatch;
import com.taian.mapper.InventoryBatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * USDT 库存服务：按成本价分批次，FIFO（低成本优先）扣减，MySQL 持久化
 */
@Service
public class UsdtInventoryService {

    @Autowired
    private InventoryBatchMapper batchMapper;

    /** 获取当前库存（按成本价升序） */
    public List<InventoryBatch> getInventory() {
        LambdaQueryWrapper<InventoryBatch> q = new LambdaQueryWrapper<>();
        q.gt(InventoryBatch::getAmount, BigDecimal.ZERO);
        q.orderByAsc(InventoryBatch::getCostPrice);
        return batchMapper.selectList(q);
    }

    /** 获取库存总量 */
    public BigDecimal getTotalAmount() {
        List<InventoryBatch> list = getInventory();
        return list.stream()
                .map(b -> b.getAmount() != null ? b.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 添加库存批次。若已存在相同成本价则合并，否则新增
     */
    @Transactional(rollbackFor = Exception.class)
    public void addBatch(BigDecimal costPrice, BigDecimal amount) {
        if (costPrice == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return;

        LambdaQueryWrapper<InventoryBatch> q = new LambdaQueryWrapper<>();
        q.eq(InventoryBatch::getCostPrice, costPrice);
        InventoryBatch existing = batchMapper.selectOne(q);
        if (existing != null) {
            existing.setAmount(existing.getAmount().add(amount));
            batchMapper.updateById(existing);
        } else {
            InventoryBatch b = new InventoryBatch();
            b.setCostPrice(costPrice);
            b.setAmount(amount);
            batchMapper.insert(b);
        }
    }

    /**
     * 预售预览：按 FIFO 模拟扣减，返回成本及利润，不实际扣库存
     */
    public JSONObject previewDeduct(BigDecimal sellAmount) {
        JSONObject result = new JSONObject();
        result.put("totalUsdt", sellAmount);
        result.put("costAmount", BigDecimal.ZERO);

        if (sellAmount == null || sellAmount.compareTo(BigDecimal.ZERO) <= 0) {
            result.put("batchesUsed", new JSONArray());
            return result;
        }

        List<InventoryBatch> batches = getInventory();
        BigDecimal remain = sellAmount;
        BigDecimal costAmount = BigDecimal.ZERO;
        JSONArray used = new JSONArray();

        for (InventoryBatch b : batches) {
            if (remain.compareTo(BigDecimal.ZERO) <= 0) break;
            BigDecimal amt = b.getAmount() != null ? b.getAmount() : BigDecimal.ZERO;
            if (amt.compareTo(BigDecimal.ZERO) <= 0) continue;

            BigDecimal deduct = remain.min(amt);
            BigDecimal batchCost = deduct.multiply(b.getCostPrice()).setScale(2, RoundingMode.HALF_UP);
            costAmount = costAmount.add(batchCost);

            JSONObject u = new JSONObject();
            u.put("costPrice", b.getCostPrice());
            u.put("amount", deduct);
            used.add(u);

            remain = remain.subtract(deduct);
        }

        result.put("costAmount", costAmount);
        result.put("batchesUsed", used);
        result.put("sufficient", remain.compareTo(BigDecimal.ZERO) <= 0);
        result.put("shortage", remain.compareTo(BigDecimal.ZERO) > 0 ? remain : BigDecimal.ZERO);
        return result;
    }

    /**
     * 实际扣减库存（FIFO），返回总成本
     */
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal deductFifo(BigDecimal sellAmount) {
        if (sellAmount == null || sellAmount.compareTo(BigDecimal.ZERO) <= 0) return BigDecimal.ZERO;

        List<InventoryBatch> batches = getInventory();
        BigDecimal remain = sellAmount;
        BigDecimal costAmount = BigDecimal.ZERO;

        for (InventoryBatch b : batches) {
            if (remain.compareTo(BigDecimal.ZERO) <= 0) break;
            BigDecimal amt = b.getAmount() != null ? b.getAmount() : BigDecimal.ZERO;
            if (amt.compareTo(BigDecimal.ZERO) <= 0) continue;

            BigDecimal deduct = remain.min(amt);
            BigDecimal batchCost = deduct.multiply(b.getCostPrice()).setScale(2, RoundingMode.HALF_UP);
            costAmount = costAmount.add(batchCost);

            BigDecimal newAmount = amt.subtract(deduct);
            if (newAmount.compareTo(BigDecimal.ZERO) <= 0) {
                batchMapper.deleteById(b.getId());
            } else {
                b.setAmount(newAmount);
                batchMapper.updateById(b);
            }
            remain = remain.subtract(deduct);
        }

        return costAmount;
    }

    /** 从指定成本价批次扣减（用于删除买入订单时回滚） */
    @Transactional(rollbackFor = Exception.class)
    public void deductBatch(BigDecimal costPrice, BigDecimal amount) {
        if (costPrice == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return;

        LambdaQueryWrapper<InventoryBatch> q = new LambdaQueryWrapper<>();
        q.eq(InventoryBatch::getCostPrice, costPrice);
        InventoryBatch b = batchMapper.selectOne(q);
        if (b == null) return;

        BigDecimal newAmount = b.getAmount().subtract(amount);
        if (newAmount.compareTo(BigDecimal.ZERO) <= 0) {
            batchMapper.deleteById(b.getId());
        } else {
            b.setAmount(newAmount);
            batchMapper.updateById(b);
        }
    }

    /** 加回批次（用于删除卖出订单时回滚） */
    @Transactional(rollbackFor = Exception.class)
    public void returnBatches(String deductionDetailJson) {
        if (deductionDetailJson == null || deductionDetailJson.trim().isEmpty()) return;
        try {
            JSONArray arr = JSON.parseArray(deductionDetailJson);
            if (arr == null) return;
            for (int i = 0; i < arr.size(); i++) {
                JSONObject u = arr.getJSONObject(i);
                addBatch(u.getBigDecimal("costPrice"), u.getBigDecimal("amount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
