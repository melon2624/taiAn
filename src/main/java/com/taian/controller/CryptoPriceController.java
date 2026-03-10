package com.taian.controller;

import com.taian.bean.CryptoPrice;
import com.taian.bean.CryptoPriceData;
import com.taian.service.CryptoPriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 加密货币价格接口
 * 提供BTC、ETH、TRX、BNB的实时买卖价格查询
 */
@Tag(name = "加密货币价格接口", description = "获取BTC、ETH、TRX、BNB的实时买卖价格")
@RestController
@RequestMapping("/restapi/cai")
@CrossOrigin(origins = "*")
public class CryptoPriceController {

    @Autowired
    private CryptoPriceService cryptoPriceService;

    /**
     * 获取所有加密货币的买卖价格
     * @return CryptoPriceData 包含BTC、ETH、TRX、BNB的买卖价格
     */
    @Operation(summary = "获取所有加密货币买卖价格", description = "返回BTC、ETH、TRX、BNB的实时买价和卖价")
    @GetMapping("/cryptoPrices")
    public CryptoPriceData getCryptoPrices() {
        return cryptoPriceService.getCryptoPrices();
    }

    /**
     * 获取单个加密货币的买卖价格
     * @param symbol 加密货币符号 (BTC, ETH, TRX, BNB)
     * @return CryptoPrice 包含买价和卖价的JSON对象
     */
    @Operation(summary = "获取单个加密货币买卖价格", description = "按符号获取加密货币的买价和卖价，支持: BTC, ETH, TRX, BNB")
    @GetMapping("/cryptoPrice/{symbol}")
    public CryptoPrice getSingleCryptoPrice(
            @Parameter(description = "加密货币符号: BTC, ETH, TRX, BNB")
            @PathVariable String symbol) {
        return cryptoPriceService.getSinglePrice(symbol);
    }

    /**
     * 获取BTC的买卖价格
     * @return CryptoPrice BTC的买价和卖价
     */
    @Operation(summary = "获取BTC买卖价格", description = "返回BTC的买价和卖价")
    @GetMapping("/btc")
    public CryptoPrice getBtcPrice() {
        return cryptoPriceService.getSinglePrice("BTC");
    }

    /**
     * 获取ETH的买卖价格
     * @return CryptoPrice ETH的买价和卖价
     */
    @Operation(summary = "获取ETH买卖价格", description = "返回ETH的买价和卖价")
    @GetMapping("/eth")
    public CryptoPrice getEthPrice() {
        return cryptoPriceService.getSinglePrice("ETH");
    }

    /**
     * 获取TRX的买卖价格
     * @return CryptoPrice TRX的买价和卖价
     */
    @Operation(summary = "获取TRX买卖价格", description = "返回TRX的买价和卖价")
    @GetMapping("/trx")
    public CryptoPrice getTrxPrice() {
        return cryptoPriceService.getSinglePrice("TRX");
    }

    /**
     * 获取BNB的买卖价格
     * @return CryptoPrice BNB的买价和卖价
     */
    @Operation(summary = "获取BNB买卖价格", description = "返回BNB的买价和卖价")
    @GetMapping("/bnb")
    public CryptoPrice getBnbPrice() {
        return cryptoPriceService.getSinglePrice("BNB");
    }

    /**
     * 更新加密货币价格（基于行情价格计算买卖价）
     * @param symbol 加密货币符号
     * @param marketPrice 币安行情价格
     * @return 更新后的买卖价格
     */
    @Operation(summary = "更新单个加密货币价格", description = "基于行情价格计算并更新买卖价（用于测试币安API集成）")
    @PostMapping("/cryptoPrice/{symbol}")
    public CryptoPrice updateSinglePrice(
            @Parameter(description = "加密货币符号: BTC, ETH, TRX, BNB")
            @PathVariable String symbol,
            @Parameter(description = "币安行情价格")
            @RequestParam String marketPrice) {
        cryptoPriceService.updateSinglePrice(symbol, marketPrice);
        return cryptoPriceService.getSinglePrice(symbol);
    }
}

