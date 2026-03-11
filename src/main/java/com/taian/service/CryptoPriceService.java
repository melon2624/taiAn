package com.taian.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.taian.bean.CryptoPrice;
import com.taian.bean.CryptoPriceData;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * 加密货币价格服务类
 * 用于管理加密货币价格的获取和更新
 */
@Service
public class CryptoPriceService {

    /** 在内存中存储的加密货币价格数据 */
    private CryptoPriceData cryptoPriceData = new CryptoPriceData();
    
    /** 随机数生成器，用于生成买卖价差 */
    private Random random = new Random();

    /**
     * 定时任务：每10分钟调用一次币安API获取价格
     * fixedDelay = 600000 毫秒 = 10分钟
     */
    @Scheduled(fixedDelay = 600000, initialDelay = 5000)
    public void fetchCryptoPricesFromBinance() {
        try {
            // 调用币安API获取价格
            // 用户在这里实现币安API的调用逻辑
            updateCryptoPriceDataFromBinance();
        } catch (Exception e) {
            System.err.println("获取币安价格失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static final String BINANCE_API = "https://fapi.binance.com/fapi/v1/ticker/price?symbol=";

    /**
     * 调用币安合约行情接口获取单个币种价格
     *
     * @param symbol 交易对，如 BTCUSDT
     * @return 价格字符串，失败返回 null
     */
    private String fetchPriceFromBinance(String symbol) {
        try {
            String response = HttpUtil.get(BINANCE_API + symbol, 5000);
            JSONObject json = JSONUtil.parseObj(response);
            return json.getStr("price");
        } catch (Exception e) {
            System.err.println("获取 " + symbol + " 价格失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 更新所有币种的价格数据，依次调用币安接口
     */
    private void updateCryptoPriceDataFromBinance() {
        String[][] pairs = {
            {"BTCUSDT", "BTC"},
            {"ETHUSDT", "ETH"},
            {"TRXUSDT", "TRX"},
            {"BNBUSDT", "BNB"}
        };
        for (String[] pair : pairs) {
            String price = fetchPriceFromBinance(pair[0]);
            if (price != null) {
                updatePriceWithSpread(pair[1], price);
                System.out.println("更新 " + pair[1] + " 价格: " + price);
            }
        }
    }

    /**
     * 获取当前缓存的加密货币价格数据
     * 
     * @return CryptoPriceData 包含最新的BTC、ETH、TRX、BNB的买卖价格
     */
    public CryptoPriceData getCryptoPrices() {
        return this.cryptoPriceData;
    }

    /**
     * 根据币安的行情价格计算买卖价
     * 买价 > 卖价，相差约0.1% (范围：0.08% - 0.12%)
     * 
     * @param symbol 加密货币符号 (BTC, ETH, TRX, BNB)
     * @param marketPrice 币安行情价格
     */
    public void updatePriceWithSpread(String symbol, String marketPrice) {
        if (symbol == null || marketPrice == null) {
            return;
        }
        
        try {
            // 将行情价格转换为 BigDecimal 以确保精度
            BigDecimal market = new BigDecimal(marketPrice);
            
            // 生成随机的价差百分比 (0.08% - 0.12%)
            double spreadPercentage = 0.0008 + (random.nextDouble() * 0.0004);
            BigDecimal spread = BigDecimal.valueOf(spreadPercentage);
            
            // 计算买价 (Ask Price): 行情价 * (1 + 价差)
            BigDecimal askPrice = market.multiply(BigDecimal.ONE.add(spread))
                    .setScale(8, RoundingMode.HALF_UP);
            
            // 计算卖价 (Bid Price): 行情价 * (1 - 价差/2)
            BigDecimal bidPrice = market.multiply(BigDecimal.ONE.subtract(spread.divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP)))
                    .setScale(8, RoundingMode.HALF_UP);
            
            // 创建 CryptoPrice 对象
            CryptoPrice cryptoPrice = new CryptoPrice(
                    symbol.toUpperCase(),
                    market.toString(),
                    askPrice.toString(),
                    bidPrice.toString(),
                    String.format("%.4f%%", spreadPercentage * 100)
            );
            cryptoPrice.setLastUpdateTime(System.currentTimeMillis());
            
            // 根据币种更新相应的字段
            switch (symbol.toUpperCase()) {
                case "BTC":
                    this.cryptoPriceData.setBtc(cryptoPrice);
                    break;
                case "ETH":
                    this.cryptoPriceData.setEth(cryptoPrice);
                    break;
                case "TRX":
                    this.cryptoPriceData.setTrx(cryptoPrice);
                    break;
                case "BNB":
                    this.cryptoPriceData.setBnb(cryptoPrice);
                    break;
            }
            
            this.cryptoPriceData.setLastUpdateTime(System.currentTimeMillis());
        } catch (NumberFormatException e) {
            System.err.println("价格格式错误: " + marketPrice);
            e.printStackTrace();
        }
    }

    /**
     * 获取单个加密货币的价格
     * 
     * @param symbol 加密货币符号 (BTC, ETH, TRX, BNB)
     * @return CryptoPrice 对象，包含买卖价信息
     */
    public CryptoPrice getSinglePrice(String symbol) {
        if (symbol == null) {
            return new CryptoPrice();
        }
        
        switch (symbol.toUpperCase()) {
            case "BTC":
                return this.cryptoPriceData.getBtc();
            case "ETH":
                return this.cryptoPriceData.getEth();
            case "TRX":
                return this.cryptoPriceData.getTrx();
            case "BNB":
                return this.cryptoPriceData.getBnb();
            default:
                return new CryptoPrice();
        }
    }

    /**
     * 更新单个加密货币的价格（基于行情价格计算买卖价）
     * 
     * @param symbol 加密货币符号 (BTC, ETH, TRX, BNB)
     * @param marketPrice 币安行情价格
     */
    public void updateSinglePrice(String symbol, String marketPrice) {
        updatePriceWithSpread(symbol, marketPrice);
    }
}
