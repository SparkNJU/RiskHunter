
-- 清空原有数据
TRUNCATE TABLE risk_signal;

-- 查看安全路径配置（需FILE权限）
SHOW VARIABLES LIKE 'secure_file_priv';

-- 加载CSV数据（逗号分隔版本）
LOAD DATA INFILE "C:\Program Files\MySQL\MySQL Server 9.0\Uploads\emp_value(1).csv"
INTO TABLE risk_signal
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ','  -- 关键修改：改为逗号分隔
OPTIONALLY ENCLOSED BY '"' -- 处理可能存在的引号
LINES TERMINATED BY '\r\n'
IGNORE 1 ROWS
(
    @base_currency,
    @target_currency,
    @time_str,
    emp,
    exchange_rate,
    interest_rate,
    fx_reserves,
    @news_factor
)
SET
    base_currency = CAST(TRIM(@base_currency) AS UNSIGNED),
    target_currency = CAST(TRIM(@target_currency) AS UNSIGNED),
    time = @time_str, 
    analysis = CONCAT('', ROUND(@news_factor, 4)),
    advice = '建议持续监测市场变化';

-- 删除并重新创建 user 表
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
                                      `id` INT NOT NULL,
                                      `address` VARCHAR(255) NULL,
                                      `username` VARCHAR(255) NULL,
                                      `password` VARCHAR(255) NULL,
                                      `phone` VARCHAR(255) NULL,
                                      `role` INT NULL,
                                      PRIMARY KEY (`id`)
);

-- 插入 user 数据
INSERT INTO riskhunter.user (id, address, username, password, phone, role) VALUES (1, 'China, Nanjing, Gulou district', 'seecoder', '123456', '13545687101', 1);
INSERT INTO riskhunter.user (id, address, username, password, phone, role) VALUES (2, null, 'seecoder', '123456789', '13333333333', 2);
INSERT INTO riskhunter.user (id, address, username, password, phone, role) VALUES (3, null, 'CEO', '123456', '13444444444', 3);
INSERT INTO riskhunter.user (id, address, username, password, phone, role) VALUES (4, 'China, Nanjing, Qixia district', 'user4', '123456', '13512345678', 4);
INSERT INTO riskhunter.user (id, address, username, password, phone, role) VALUES (5, 'China, Nanjing, Qixia district', 'user4', '123456', '13512345677', 4);
INSERT INTO riskhunter.user (id, address, username, password, phone, role) VALUES (6, 'China, Nanjing, Qixia district', 'user3', '123456', '13512345674', 3);