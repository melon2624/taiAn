package com.taian.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "泰安 USDT 接口", description = "价格查询/更新、买卖订单管理、统计")
@RestController
@RequestMapping("/restapi/cai")
@CrossOrigin(origins = "*")
public class UsdtController {

    String currentPrice = "0";      // 展示价格
    String costPrice = "0";         // 成本价格


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

}
