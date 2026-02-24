-- 使用前请先创建数据库: CREATE DATABASE taian DEFAULT CHARACTER SET utf8mb4;

-- 若已有 usdt_order 表，执行:
-- ALTER TABLE usdt_order ADD COLUMN settled_from_borrow TINYINT DEFAULT 0 COMMENT '1=借入货卖出后已计算利润' AFTER deduction_detail;
-- ALTER TABLE usdt_order ADD COLUMN borrowed_return_cost DECIMAL(20,2) NULL COMMENT '还借成本' AFTER settled_from_borrow;

-- 订单表
CREATE TABLE IF NOT EXISTS usdt_order (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(10) NOT NULL COMMENT 'BUY/SELL',
    amount DECIMAL(20,8) NOT NULL COMMENT 'USDT数量',
    price DECIMAL(20,4) NOT NULL COMMENT '单价港币',
    total_amount DECIMAL(20,2) NOT NULL COMMENT '总金额',
    cost_amount DECIMAL(20,2) NULL COMMENT '成本金额(仅卖出)',
    profit DECIMAL(20,2) NULL COMMENT '利润(仅卖出)',
    deduction_detail TEXT NULL COMMENT '扣减明细JSON',
    settled_from_borrow TINYINT DEFAULT 0 COMMENT '1=借入货卖出后已计算利润',
    borrowed_return_cost DECIMAL(20,2) NULL COMMENT '还借成本(点击计算利润时填入)',
    remark VARCHAR(500) DEFAULT '',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_type (type),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='USDT买卖订单';

-- 库存批次表
CREATE TABLE IF NOT EXISTS inventory_batch (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cost_price DECIMAL(20,4) NOT NULL COMMENT '成本价港币',
    amount DECIMAL(20,8) NOT NULL COMMENT 'USDT数量',
    INDEX idx_cost_price (cost_price)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存批次';
