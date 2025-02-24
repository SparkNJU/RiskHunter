
-- 创建 riskhunter 数据库
CREATE DATABASE IF NOT EXISTS riskhunter;

-- 使用 riskhunter 数据库
USE riskhunter;

CREATE TABLE risk_signal(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    base_currency INT NOT NULL,
    target_currency INT NOT NULL,
    time DATETIME NOT NULL,
    emp DOUBLE NOT NULL,
    exchange_rate DOUBLE NOT NULL,
    interest_rate DOUBLE NOT NULL,
    fx_reserves DOUBLE NOT NULL,
    analysis VARCHAR(5000) NOT NULL,
    advice VARCHAR(5000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

use riskhunter;
INSERT INTO risk_signal (base_currency, target_currency, time, emp, exchange_rate, interest_rate, fx_reserves, analysis, advice) VALUES
(1, 2, '2023-01-01 00:00:00', 100.5, 1.12, 2.5, 500.0, 'Initial analysis: Stable economy.', 'Monitor inflation closely.'),
(1, 2, '2023-01-15 00:00:00', 101.0, 1.11, 2.6, 505.0, 'Slight increase in EMP, exchange rate stable.', 'Consider gradual interest rate adjustments.'),
(1, 2, '2023-02-01 00:00:00', 101.5, 1.10, 2.7, 510.0, 'EMP increasing, exchange rate decreasing.', 'Evaluate impact of currency fluctuations.'),
(1, 3, '2023-02-15 00:00:00', 102.0, 1.09, 2.8, 515.0, 'EMP continues to rise, exchange rate decline accelerates.', 'Implement measures to stabilize the currency.'),
(1, 4, '2023-03-01 00:00:00', 102.5, 1.08, 2.9, 520.0, 'Further increase in EMP, exchange rate under pressure.', 'Strengthen FX reserves and consider capital controls.'),
(1, 3, '2023-03-15 00:00:00', 103.0, 1.07, 3.0, 525.0, 'Significant increase in EMP, exchange rate volatility.', 'Implement aggressive monetary tightening.'),
(1, 2, '2023-04-01 00:00:00', 103.5, 1.06, 3.1, 530.0, 'EMP nearing critical level, exchange rate instability.', 'Urgent action required to restore confidence.'),
(1, 5, '2023-04-15 00:00:00', 104.0, 1.05, 3.2, 535.0, 'EMP surpasses critical level, exchange rate collapse imminent.', 'Intervene heavily in the FX market.'),
(1, 3, '2023-05-01 00:00:00', 104.5, 1.04, 3.3, 540.0, 'EMP remains high, exchange rate continues downward trend.', 'Seek external assistance if necessary.'),
(2, 3, '2023-05-15 00:00:00', 105.0, 1.03, 3.4, 545.0, 'EMP at peak, exchange rate severely depreciated.', 'Implement structural reforms to address underlying issues.'),
(1, 2, '2023-06-01 00:00:00', 104.8, 1.035, 3.4, 540.0, 'Slight decrease in EMP, exchange rate showing signs of stability.', 'Continue monitoring closely and maintain cautious stance.'),
(2, 5, '2023-06-15 00:00:00', 104.5, 1.04, 3.3, 535.0, 'EMP decreasing gradually, exchange rate recovering slightly.', 'Begin easing monetary policy cautiously.'),
(2, 5, '2023-07-01 00:00:00', 104.0, 1.05, 3.2, 530.0, 'EMP continues to decline, exchange rate stabilizing.', 'Focus on attracting foreign investment.'),
(2, 5, '2023-07-15 00:00:00', 103.5, 1.06, 3.1, 525.0, 'EMP approaching normal levels, exchange rate relatively stable.', 'Promote exports to improve trade balance.'),
(2, 4, '2023-08-01 00:00:00', 103.0, 1.07, 3.0, 520.0, 'EMP within acceptable range, exchange rate under control.', 'Improve infrastructure to enhance competitiveness.'),
(2, 3, '2023-08-15 00:00:00', 102.5, 1.08, 2.9, 515.0, 'EMP well below previous peak, exchange rate strengthening.', 'Invest in education and training.'),
(2, 4, '2023-09-01 00:00:00', 102.0, 1.09, 2.8, 510.0, 'Economy showing signs of recovery, exchange rate appreciation.', 'Reduce government debt to improve fiscal sustainability.'),
(3, 4, '2023-09-15 00:00:00', 101.5, 1.10, 2.7, 505.0, 'Economic outlook positive, exchange rate remains strong.', 'Strengthen regulatory framework to prevent future crises.'),
(4, 5, '2023-10-01 00:00:00', 101.0, 1.11, 2.6, 500.0, 'Stable economic conditions, exchange rate holding steady.', 'Diversify the economy to reduce vulnerability to shocks.'),
(3, 4, '2023-10-15 00:00:00', 100.5, 1.12, 2.5, 495.0, 'Economy returning to normal, exchange rate stabilizing.', 'Maintain prudent fiscal and monetary policies.');



-- 创建 user 表
CREATE TABLE IF NOT EXISTS `user` (
                                      `id` INT NOT NULL,
                                      `address` VARCHAR(255) NULL,
                                      `username` VARCHAR(255) NULL,
                                      `password` VARCHAR(255) NULL,
                                      `phone` VARCHAR(255) NULL,
                                      `role` INT NULL,
                                      PRIMARY KEY (`id`)
);
-- 插入数据
INSERT INTO riskhunter.user (id, address, username, password, phone, role) VALUES (1, 'China, Nanjing, Gulou district', 'seecoder', '123456', '13545687101', 1);
INSERT INTO riskhunter.user (id, address, username, password, phone, role) VALUES (2, null, 'seecoder', '123456789', '13333333333', 2);
INSERT INTO riskhunter.user (id, address, username, password, phone, role) VALUES (3, null, 'CEO', '123456', '13444444444', 3);
INSERT INTO riskhunter.user (id, address, username, password, phone, role) VALUES (4, 'China, Nanjing, Qixia district', 'user4', '123456', '13512345678', 4);
INSERT INTO riskhunter.user (id, address, username, password, phone, role) VALUES (5, 'China, Nanjing, Qixia district', 'user4', '123456', '13512345677', 4);
INSERT INTO riskhunter.user (id, address, username, password, phone, role) VALUES (6, 'China, Nanjing, Qixia district', 'user3', '123456', '13512345674', 3);