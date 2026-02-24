package com.taian.controller;

import com.alibaba.fastjson.JSONObject;
import com.taian.entity.InventoryBatch;
import com.taian.entity.OrderType;
import com.taian.entity.UsdtOrder;
import com.taian.service.UsdtInventoryService;
import com.taian.service.UsdtOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "泰安 USDT 接口", description = "价格查询/更新、买卖订单管理、统计")
@RestController
@RequestMapping("/restapi/cai")
public class UsdtController {

    String currentPrice = "0";      // 展示价格
    String costPrice = "0";         // 成本价格

    @Autowired
    private UsdtOrderService usdtOrderService;
    @Autowired
    private UsdtInventoryService inventoryService;

    @Operation(summary = "获取 USDT 展示价格", description = "返回港币单价，纯文本")
    @GetMapping("/usdtPrice")
    public String getUsdtPrice() {
        return currentPrice;
    }

    @Operation(summary = "设置 USDT 展示价格", description = "更新展示用价格，需传入港币单价")
    @PostMapping("/usdtPrice")
    public String setUsdtPrice(@Parameter(description = "价格，如 7.78") @RequestParam String price) {
        currentPrice = price;
        return currentPrice;
    }

    @Operation(summary = "获取 USDT 成本价格", description = "返回成本价港币单价，纯文本")
    @GetMapping("/usdtCostPrice")
    public String getUsdtCostPrice() {
        return costPrice;
    }

    @Operation(summary = "设置 USDT 成本价格", description = "更新成本价，需传入港币单价")
    @PostMapping("/usdtCostPrice")
    public String setUsdtCostPrice(@Parameter(description = "成本价格，如 7.75") @RequestParam String price) {
        costPrice = price;
        return costPrice;
    }

    @Operation(summary = "获取库存", description = "按成本价分批次，可展示各成本价还有多少 USDT")
    @GetMapping("/inventory")
    public Object getInventory() {
        List<InventoryBatch> list = inventoryService.getInventory();
        BigDecimal total = inventoryService.getTotalAmount();
        JSONObject r = new JSONObject();
        r.put("success", true);
        r.put("batches", list);
        r.put("totalAmount", total);
        return r;
    }

    @Operation(summary = "添加库存批次", description = "手动入库，用于初始化或补货。同成本价会合并")
    @PostMapping("/inventory/add")
    public Object addInventory(
            @Parameter(description = "成本价", required = true) @RequestParam String costPrice,
            @Parameter(description = "USDT 数量", required = true) @RequestParam String amount) {
        inventoryService.addBatch(new BigDecimal(costPrice), new BigDecimal(amount));
        JSONObject r = new JSONObject();
        r.put("success", true);
        r.put("batches", inventoryService.getInventory());
        return r;
    }

    @Operation(summary = "转出预览", description = "挪用/转出前预览，检查库存是否足够、将从哪些批次扣减。不实际扣库存")
    @GetMapping("/order/transferOutPreview")
    public Object transferOutPreview(
            @Parameter(description = "转出 USDT 数量", required = true) @RequestParam String amount) {
        BigDecimal usdtAmount = new BigDecimal(amount);
        com.alibaba.fastjson.JSONObject preview = inventoryService.previewDeduct(usdtAmount);
        JSONObject r = new JSONObject();
        r.put("success", true);
        r.put("usdtAmount", usdtAmount);
        r.put("batchesUsed", preview.getJSONArray("batchesUsed"));
        r.put("sufficient", preview.getBoolean("sufficient"));
        r.put("shortage", preview.get("shortage"));
        return r;
    }

    @Operation(summary = "卖出预览", description = "输入 USDT 数量或港币金额+售价，返回收入、成本、利润、扣减批次。不实际扣库存")
    @GetMapping("/order/sellPreview")
    public Object sellPreview(
            @Parameter(description = "卖出 USDT 数量，与 amountHkd 二选一") @RequestParam(required = false) String amount,
            @Parameter(description = "顾客支付港币金额，与 amount 二选一。有则 amount=amountHkd/sellPrice") @RequestParam(required = false) String amountHkd,
            @Parameter(description = "出售单价（港币）", required = true) @RequestParam String sellPrice) {
        BigDecimal sp = new BigDecimal(sellPrice);
        BigDecimal usdtAmount;
        if (amount != null && !amount.trim().isEmpty()) {
            usdtAmount = new BigDecimal(amount);
        } else if (amountHkd != null && !amountHkd.trim().isEmpty()) {
            usdtAmount = new BigDecimal(amountHkd).divide(sp, 8, BigDecimal.ROUND_DOWN);
        } else {
            JSONObject err = new JSONObject();
            err.put("success", false);
            err.put("message", "请提供 amount 或 amountHkd");
            return err;
        }
        com.alibaba.fastjson.JSONObject preview = inventoryService.previewDeduct(usdtAmount);
        BigDecimal revenue = usdtAmount.multiply(sp).setScale(2, java.math.RoundingMode.HALF_UP);
        BigDecimal costAmount = preview.getBigDecimal("costAmount");
        BigDecimal profit = revenue.subtract(costAmount != null ? costAmount : BigDecimal.ZERO);
        JSONObject r = new JSONObject();
        r.put("success", true);
        r.put("usdtAmount", usdtAmount);
        r.put("sellPrice", sp);
        r.put("revenue", revenue);
        r.put("costAmount", costAmount);
        r.put("profit", profit);
        r.put("batchesUsed", preview.getJSONArray("batchesUsed"));
        r.put("sufficient", preview.getBoolean("sufficient"));
        r.put("shortage", preview.get("shortage"));
        return r;
    }

    @Operation(summary = "添加订单", description = "BUY/TRANSFER_IN：加库存需price。SELL：需price。BORROW_IN：借入，price填0表示待定。TRANSFER_OUT/BORROW_RETURN：只扣库存，不需price")
    @PostMapping("/order/add")
    public Object addOrder(
            @Parameter(description = "BUY/SELL/TRANSFER_OUT/TRANSFER_IN/BORROW_IN/BORROW_RETURN", required = true) @RequestParam String type,
            @Parameter(description = "USDT 数量", required = true) @RequestParam String amount,
            @Parameter(description = "单价：买入/转入=成本价，卖出=售价。BORROW_IN价格待定填0或省略。TRANSFER_OUT/BORROW_RETURN可省略") @RequestParam(required = false) String price,
            @Parameter(description = "备注，可选") @RequestParam(required = false) String remark) {
        try {
            OrderType orderType = OrderType.valueOf(type.toUpperCase());
            BigDecimal amt = new BigDecimal(amount);
            BigDecimal prc = (price != null && !price.trim().isEmpty()) ? new BigDecimal(price) : null;
            UsdtOrder order = usdtOrderService.addOrder(orderType, amt, prc, remark);
            JSONObject r = new JSONObject();
            r.put("success", true);
            r.put("order", order);
            return r;
        } catch (IllegalStateException e) {
            JSONObject r = new JSONObject();
            r.put("success", false);
            r.put("message", e.getMessage());
            return r;
        } catch (IllegalArgumentException e) {
            JSONObject r = new JSONObject();
            r.put("success", false);
            r.put("message", "类型无效，需为 BUY/SELL/TRANSFER_OUT/TRANSFER_IN/BORROW_IN/BORROW_RETURN");
            return r;
        }
    }

    @Operation(summary = "计算利润", description = "补货还借后，对使用了借入货的卖出订单填入还借成本，计算并更新利润")
    @PostMapping("/order/calculateProfit/{id}")
    public Object calculateProfit(
            @Parameter(description = "卖出订单ID") @PathVariable Long id,
            @Parameter(description = "还借成本（港币）", required = true) @RequestParam String returnCostAmount) {
        try {
            UsdtOrder order = usdtOrderService.calculateProfit(id, new BigDecimal(returnCostAmount));
            JSONObject r = new JSONObject();
            r.put("success", true);
            r.put("order", order);
            return r;
        } catch (IllegalStateException e) {
            JSONObject r = new JSONObject();
            r.put("success", false);
            r.put("message", e.getMessage());
            return r;
        }
    }

    @Operation(summary = "删除订单", description = "根据订单 ID 删除")
    @PostMapping("/order/delete/{id}")
    public Object deleteOrder(@Parameter(description = "订单ID") @PathVariable Long id) {
        boolean ok = usdtOrderService.deleteOrder(id);
        JSONObject r = new JSONObject();
        r.put("success", ok);
        return r;
    }

    @Operation(summary = "订单列表", description = "不传 type 查全部，传类型筛选")
    @GetMapping("/order/list")
    public Object listOrders(@Parameter(description = "BUY/SELL/TRANSFER_OUT/TRANSFER_IN/BORROW_IN/BORROW_RETURN，可选") @RequestParam(required = false) String type) {
        List<UsdtOrder> list = usdtOrderService.listOrders(type);
        JSONObject r = new JSONObject();
        r.put("success", true);
        r.put("list", list);
        return r;
    }

    @Operation(summary = "统计数据", description = "买入/卖出数量、总金额、均价、盈亏、持仓")
    @GetMapping("/order/stats")
    public Object getStats() {
        JSONObject stats = usdtOrderService.getStats();
        JSONObject r = new JSONObject();
        r.put("success", true);
        r.put("stats", stats);
        return r;
    }
}
