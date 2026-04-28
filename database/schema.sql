

-- 创建数据库
CREATE DATABASE IF NOT EXISTS student_health_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE student_health_system;

-- 1. 用户表
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户唯一标识',
    account VARCHAR(50) UNIQUE NOT NULL COMMENT '账号（登录用）',
    password VARCHAR(255) NOT NULL COMMENT '加密后的密码',
    username VARCHAR(50) COMMENT '用户名（昵称）',
    role TINYINT DEFAULT 0 COMMENT '角色：0-学生，1-管理员',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_account (account)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 打卡记录表
CREATE TABLE IF NOT EXISTS checkin_records (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '记录唯一标识',
    user_id INT NOT NULL COMMENT '用户ID',
    checkin_date DATE NOT NULL COMMENT '打卡日期',
    water INT DEFAULT 0 COMMENT '饮水量（杯数）',
    sleep DECIMAL(4,1) DEFAULT 0 COMMENT '睡眠时长（小时）',
    exercise INT DEFAULT 0 COMMENT '运动时长（分钟）',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_date (user_id, checkin_date),
    INDEX idx_user_id (user_id),
    INDEX idx_checkin_date (checkin_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='打卡记录表';

-- 3. 用户会话表（用于token管理）
CREATE TABLE IF NOT EXISTS user_sessions (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '会话ID',
    user_id INT NOT NULL COMMENT '用户ID',
    token VARCHAR(255) UNIQUE NOT NULL COMMENT '登录token',
    expires_at DATETIME NOT NULL COMMENT '过期时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_token (token)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户会话表';

-- 插入测试数据
INSERT INTO users (account, password, username, role) VALUES
('test', '123456', 'Jiang', 0);


