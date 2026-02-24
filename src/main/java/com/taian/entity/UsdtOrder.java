package com.taian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * USDT 买卖订单
 */
@Data
@TableName("usdt_order")
public class UsdtOrder {
    @TableId(type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Long id;
    private OrderType type;        // 买入/卖出
    private BigDecimal amount;     // USDT 数量
    private BigDecimal price;      // 单价（港币）
    private BigDecimal totalAmount; // 总金额 = amount * price
    private BigDecimal costAmount;  // 成本金额（仅卖出时有值）
    private BigDecimal profit;      // 利润（仅卖出时有值）
    private String deductionDetail; // 扣减明细 JSON，用于删除时回滚库存
    private Integer settledFromBorrow; // 1=借入货卖出后已点击计算利润
    private BigDecimal borrowedReturnCost; // 还借成本(点击计算利润时填入)
    private String remark;         // 备注
    private LocalDateTime createTime;

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
        recalcTotal();
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        recalcTotal();
    }

    public void recalcTotal() {
        if (amount != null && price != null) {
            this.totalAmount = amount.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }

    public String getCreateTimeStr() {
        if (createTime == null) return "";
        return createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /** 是否需计算利润：卖出借入货(含混合)的订单，补货还借后可点击计算。供 list 展示用 */
    public boolean getNeedCalculateProfit() {
        return type == OrderType.SELL && (profit == null || profit.compareTo(BigDecimal.ZERO) == 0)
                && totalAmount != null && !Integer.valueOf(1).equals(settledFromBorrow);
    }
