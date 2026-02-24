package com.taian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 库存批次：同一成本价的 USDT 数量
 */
@Data
@TableName("inventory_batch")
public class InventoryBatch {
    @TableId(type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Long id;
    private BigDecimal costPrice;   // 成本价（港币）
    private BigDecimal amount;      // USDT 数量
}
