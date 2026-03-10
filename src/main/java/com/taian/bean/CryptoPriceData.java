package com.taian.bean;

import java.io.Serializable;

/**
 * 加密货币价格数据类
 * 用于存储BTC、ETH、TRX、BNB的实时买卖价格
 */
public class CryptoPriceData implements Serializable {

    private static final long serialVersionUID = 1L;

    /** BTC价格 */
    private CryptoPrice btc;

    /** ETH价格 */
    private CryptoPrice eth;

    /** TRX价格 */
    private CryptoPrice trx;

    /** BNB价格 */
    private CryptoPrice bnb;

    /** 最后更新时间 (时间戳) */
    private long lastUpdateTime;

    public CryptoPriceData() {
        this.btc = new CryptoPrice("BTC", "0", "0", "0", "0");
        this.eth = new CryptoPrice("ETH", "0", "0", "0", "0");
        this.trx = new CryptoPrice("TRX", "0", "0", "0", "0");
        this.bnb = new CryptoPrice("BNB", "0", "0", "0", "0");
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public CryptoPrice getBtc() {
        return btc;
    }

    public void setBtc(CryptoPrice btc) {
        this.btc = btc;
    }

    public CryptoPrice getEth() {
        return eth;
    }

    public void setEth(CryptoPrice eth) {
        this.eth = eth;
    }

    public CryptoPrice getTrx() {
        return trx;
    }

    public void setTrx(CryptoPrice trx) {
        this.trx = trx;
    }

    public CryptoPrice getBnb() {
        return bnb;
    }

    public void setBnb(CryptoPrice bnb) {
        this.bnb = bnb;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "CryptoPriceData{" +
                "btc=" + btc +
                ", eth=" + eth +
                ", trx=" + trx +
                ", bnb=" + bnb +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
