# database

数据库脚本与表设计目录

## 文件说明

| 文件         | 说明                |
| ---------- | ----------------- |
| schema.sql | 数据库建表脚本（含测试账号的数据） |

## 数据库初始化

### 方法一：命令行执行

```bash
# 1. 登录MySQL
mysql -u root -p

# 2. 执行脚本
source d:/Trae CN/Project/student-health-system/database/schema.sql
```

### 方法二：直接导入

```bash
mysql -u root -p < schema.sql
```

### 方法三：
在已安装并且登录MySQL的情况下可复制黏贴schema.sql文件中的sql语句直接运行

## 表结构说明

### 1. users（用户表）

- `account`: 登录账号（唯一）
- `password`: 加密密码
- `username`: 用户名（昵称）
- `role`: 角色（0学生，1管理员）
- `status`: 状态（0禁用，1启用）

### 2. checkin\_records（打卡记录表）

- `user_id`: 用户ID（外键）
- `checkin_date`: 打卡日期
- `water`: 饮水量（杯数）
- `sleep`: 睡眠时长（小时）
- `exercise`: 运动时长（分钟）
- **唯一约束**: 同一用户同一天只能有一条记录

### 3. user\_sessions（用户会话表）

- 用于存储登录token
- 支持token过期机制

## 测试账号

| 账号   | 密码     | 说明   |
| ---- | ------ | ---- |
| test | 123456 | 测试账号 |

