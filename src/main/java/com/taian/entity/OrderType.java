package com.taian.entity;

/**
 * 订单类型：买入 / 卖出 / 转出(挪用) / 转入(归还) / 借入 / 还借
 */
public enum OrderType {
    BUY("买入"),
    SELL("卖出"),
    TRANSFER_OUT("转出"),    // 挪用、转出：扣库存，无收入无利润，可后续归还
    TRANSFER_IN("转入"),     // 归还、转入：加库存，按指定成本价入账
    BORROW_IN("借入"),       // 从别人接货，价格未定则成本填0，先卖后进货再还
    BORROW_RETURN("还借");   // 进货后归还借入的货，扣库存

    private final String label;

    OrderType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
