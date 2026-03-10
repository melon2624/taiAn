package com.taian.bean;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 单个加密货币的买卖价格数据
 * 包含原始行情价格、买价和卖价
 */
public class CryptoPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 币种名称 (BTC, ETH, TRX, BNB) */
    private String symbol;

    /** 币安行情价格（原始价格） */
    @JsonIgnore
    private String marketPrice;

    /** 买价（Ask Price）- 卖给用户的价格，比行情价高 */
    private String askPrice;

    /** 卖价（Bid Price）- 买入用户的价格，比行情价低 */
    private String bidPrice;

    /** 买卖价差百分比 (例如: 0.001 表示 0.1%) */
    @JsonIgnore
    private String spreadPercentage;

    /** 最后更新时间 (时间戳) */
    private long lastUpdateTime;

    public CryptoPrice() {
        this.symbol = "";
        this.marketPrice = "0";
        this.askPrice = "0";
        this.bidPrice = "0";
        this.spreadPercentage = "0";
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public CryptoPrice(String symbol, String marketPrice, String askPrice, String bidPrice, String spreadPercentage) {
        this.symbol = symbol;
        this.marketPrice = marketPrice;
        this.askPrice = askPrice;
        this.bidPrice = bidPrice;
        this.spreadPercentage = spreadPercentage;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(String askPrice) {
        this.askPrice = askPrice;
    }

    public String getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getSpreadPercentage() {
        return spreadPercentage;
    }

    public void setSpreadPercentage(String spreadPercentage) {
        this.spreadPercentage = spreadPercentage;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "CryptoPrice{" +
                "symbol='" + symbol + '\'' +
                ", marketPrice='" + marketPrice + '\'' +
                ", askPrice='" + askPrice + '\'' +
                ", bidPrice='" + bidPrice + '\'' +
                ", spreadPercentage='" + spreadPercentage + '\'' +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
