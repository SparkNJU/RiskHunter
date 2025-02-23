-- 创建 riskhunter 数据库
CREATE DATABASE IF NOT EXISTS riskhunter;

-- 使用 riskhunter 数据库
USE riskhunter;

-- 创建 user 表
CREATE TABLE IF NOT EXISTS `user` (
  `id` INT NOT NULL,
  `address` VARCHAR(255) NULL,
  `name` VARCHAR(255) NULL,
  `password` VARCHAR(255) NULL,
  `phone` VARCHAR(255) NULL,
  `role` INT NULL,
  PRIMARY KEY (`id`)
);
-- 插入数据
INSERT INTO riskhunter.user (id, address, name, password, phone, role) VALUES (1, 'China, Nanjing, Gulou district', 'seecoder', '123456', '13545687101', 1);
INSERT INTO riskhunter.user (id, address, name, password, phone, role) VALUES (2, null, 'seecoder', '123456789', '13333333333', 2);
INSERT INTO riskhunter.user (id, address, name, password, phone, role) VALUES (3, null, 'CEO', '123456', '13444444444', 3);
INSERT INTO riskhunter.user (id, address, name, password, phone, role) VALUES (4, 'China, Nanjing, Qixia district', '软件工程僵尸', '123456', '13512345678', 4);
INSERT INTO riskhunter.user (id, address, name, password, phone, role) VALUES (5, 'China, Nanjing, Qixia district', '软件工程僵尸', '123456', '13512345677', 4);
INSERT INTO riskhunter.user (id, address, name, password, phone, role) VALUES (6, 'China, Nanjing, Qixia district', '软件工程僵尸', '123456', '13512345674', 1);