-- 东软环保公众监督系统 NEP - 建表脚本
-- 字符集 utf8mb4，排序规则 utf8mb4_general_ci（MySQL 5.5 兼容，避免 utf8mb4_0900_ai_ci）

CREATE DATABASE IF NOT EXISTS nep DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
USE nep;

-- 1. AQI 等级表
CREATE TABLE aqi (
  id INT PRIMARY KEY AUTO_INCREMENT,
  level INT NOT NULL UNIQUE COMMENT 'AQI 等级 1-6',
  grade VARCHAR(20) NOT NULL COMMENT '优/良/轻度污染/中度污染/重度污染/严重污染',
  description VARCHAR(200) COMMENT '等级描述',
  color VARCHAR(20) COMMENT '前端展示颜色',
  deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 2. 省区域表
CREATE TABLE grid_province (
  id INT PRIMARY KEY AUTO_INCREMENT,
  province_name VARCHAR(50) NOT NULL UNIQUE,
  province_code VARCHAR(20),
  deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 3. 市区域表
CREATE TABLE grid_city (
  id INT PRIMARY KEY AUTO_INCREMENT,
  city_name VARCHAR(50) NOT NULL,
  city_code VARCHAR(20),
  province_id INT NOT NULL,
  is_major_city TINYINT DEFAULT 0 COMMENT '是否 106 大城市',
  deleted TINYINT DEFAULT 0,
  INDEX idx_province (province_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 4. 网格员表
CREATE TABLE grid_member (
  id INT PRIMARY KEY AUTO_INCREMENT,
  login_code VARCHAR(50) NOT NULL UNIQUE COMMENT '登录编码',
  password VARCHAR(100) NOT NULL COMMENT 'MD5+nep_2026_ 加密',
  real_name VARCHAR(50) NOT NULL,
  phone VARCHAR(20),
  province_id INT NOT NULL,
  city_id INT NOT NULL,
  status VARCHAR(20) DEFAULT '工作',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  INDEX idx_region (province_id, city_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 5. 公众监督员表
CREATE TABLE supervisor (
  id INT PRIMARY KEY AUTO_INCREMENT,
  phone VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号（唯一身份）',
  password VARCHAR(100) NOT NULL,
  real_name VARCHAR(50) NOT NULL,
  birth_date DATE,
  gender VARCHAR(10),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 6. 系统管理员表
CREATE TABLE admins (
  id INT PRIMARY KEY AUTO_INCREMENT,
  login_code VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  real_name VARCHAR(50) NOT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 7. 公众监督反馈表
CREATE TABLE aqi_feedback (
  id INT PRIMARY KEY AUTO_INCREMENT,
  supervisor_id INT NOT NULL,
  province_id INT NOT NULL,
  city_id INT NOT NULL,
  detail_address VARCHAR(200),
  estimated_level INT NOT NULL COMMENT '预估 AQI 等级 1-6',
  feedback_desc TEXT,
  feedback_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  status VARCHAR(20) DEFAULT '未指派' COMMENT '未指派/已指派/已完成',
  assigned_grid_member_id INT,
  assign_type VARCHAR(20) COMMENT '本地/异地',
  deleted TINYINT DEFAULT 0,
  INDEX idx_supervisor (supervisor_id),
  INDEX idx_region (province_id, city_id),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 8. 统计数据表
CREATE TABLE statistics (
  id INT PRIMARY KEY AUTO_INCREMENT,
  aqi_feedback_id INT NOT NULL UNIQUE,
  grid_member_id INT NOT NULL,
  so2_concentration DECIMAL(10,2),
  so2_iaqi INT,
  co_concentration DECIMAL(10,2),
  co_iaqi INT,
  pm25_concentration DECIMAL(10,2),
  pm25_iaqi INT,
  total_aqi INT COMMENT '综合 AQI（取三项最大）',
  total_level INT COMMENT '综合 AQI 等级 1-6',
  confirm_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0,
  INDEX idx_grid_member (grid_member_id),
  INDEX idx_confirm_time (confirm_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
