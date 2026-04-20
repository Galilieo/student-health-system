# 乐健（Student Health System）

## 项目简介
“乐健”是一款面向大学生的智能健康习惯管理系统，围绕饮水、睡眠、运动等日常行为提供记录、反馈与习惯养成支持。

## 主要功能
- 用户登录与注册
- 每日健康打卡（饮水、睡眠、运动）
- 首页健康概览
- 历史记录查看
- 统计分析（持续完善中）
- 健康提醒与建议（持续完善中）

## 技术栈
- Android：Java + XML
- 后端：Spring Boot
- 数据库：MySQL

## 项目结构
```text
android-app/   Android 客户端
backend/       后端服务
database/      数据库脚本与设计
docs/          项目文档
test/          测试资料
ppt/           展示材料
```

## 快速运行（Android）
1. 使用 Android Studio 打开 `android-app/`
2. 等待 Gradle 同步完成
3. 连接模拟器或真机后运行 `app`

说明：  
- 模拟器访问本机后端可使用 `http://10.0.2.2:8080`  
- 真机调试请改为电脑局域网 IP

## 接口约定（简版）
- 返回结构：`{ code, message, data }`
- 鉴权方式：`Authorization: Bearer <token>`
- 核心接口：
  - `POST /login`
  - `POST /register`
  - `POST /checkin`
  - `GET /summary/today`
  - `GET /history`
