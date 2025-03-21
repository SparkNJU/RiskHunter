
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