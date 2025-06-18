# RsCutOffSystem - 按摩店管理系统

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0+-green)
![H2 Database](https://img.shields.io/badge/H2-Database-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

一个专为按摩店设计的综合管理系统，提供完整的业务管理解决方案，包括用户管理、客户管理、服务管理、交易管理和报表分析等功能。

## 📋 目录

- [项目简介](#项目简介)
- [主要功能](#主要功能)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [快速开始](#快速开始)
- [开发环境配置](#开发环境配置)
- [API 文档](#api-文档)
- [测试环境](#测试环境)
- [生产环境部署](#生产环境部署)
- [贡献指南](#贡献指南)
- [许可证](#许可证)

## 📖 项目简介

RsCutOffSystem 是一个现代化的按摩店管理系统，旨在帮助按摩店提高运营效率，优化客户服务体验。系统采用 Spring Boot 框架开发，提供 RESTful API 接口，支持前后端分离架构。

### 核心特性

- 🏢 **多角色用户管理** - 支持店长、经理、技师、前台等多种角色
- 👥 **客户关系管理** - 完整的客户档案、会员制度和积分系统
- 💼 **服务项目管理** - 灵活的服务分类和价格管理
- 💰 **财务交易管理** - 详细的收支记录和日结功能
- 📊 **数据报表分析** - 多维度的业务分析和统计报表
- 🔒 **安全权限控制** - 基于角色的访问控制和数据安全
- 🌐 **跨平台支持** - RESTful API 支持多种前端技术栈

## 🚀 主要功能

### 1. 用户管理模块
- 员工账户管理（创建、更新、禁用）
- 多级角色权限控制
- 用户状态管理和审核
- 员工信息档案管理

### 2. 客户管理模块
- 客户基本信息管理
- 会员等级制度（铜/银/金/白金/钻石）
- 积分系统（1元=1积分）
- 生日提醒和会员到期提醒
- 客户活跃度分析

### 3. 服务管理模块
- 服务项目分类管理
- 价格和时长设置
- 服务状态控制
- 批量价格调整
- 服务统计分析

### 4. 交易管理模块
- 收入支出记录管理
- 多种支付方式支持
- 多员工协作记录
- 日结功能
- 财务统计分析

### 5. 报表管理模块
- 财务汇总报表
- 员工业绩分析
- 客户消费分析
- 服务受欢迎度统计
- 自定义报表生成

## 🛠 技术栈

### 后端技术
- **Java 17** - 编程语言
- **Spring Boot 3.0+** - 应用框架
- **Spring Data JPA** - 数据持久化
- **Spring Security** - 安全框架
- **Hibernate** - ORM 框架
- **Maven** - 项目管理工具

### 数据库
- **H2 Database** - 开发环境（内存/文件数据库）
- **MySQL 8.0+** - 生产环境

### 开发工具
- **Spring Boot DevTools** - 热重载
- **Lombok** - 代码简化
- **Jackson** - JSON 序列化
- **Validation** - 数据验证

## 📁 项目结构

```
RsCutOffSystem/
├── .idea/                          # IntelliJ IDEA 配置文件
├── .mvn/                           # Maven 包装器文件
├── API文档/                        # API 接口文档
│   ├── Customer API 文档.md
│   ├── Report API文档.md
│   ├── Service API 文档.md
│   ├── Transaction API 文档.md
│   └── User API 文档.md
├── data/                           # H2 数据库文件目录
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.relaxationspa.rscutoffsystem/
│   │   │       ├── controller/     # REST 控制器
│   │   │       │   ├── CustomerController.java
│   │   │       │   ├── ReportController.java
│   │   │       │   ├── ServiceController.java
│   │   │       │   ├── TransactionController.java
│   │   │       │   └── UserController.java
│   │   │       ├── dto/            # 数据传输对象
│   │   │       │   ├── CustomerDTO.java
│   │   │       │   ├── ReportDTO.java
│   │   │       │   ├── ServiceDTO.java
│   │   │       │   ├── TransactionDTO.java
│   │   │       │   └── UserDTO.java
│   │   │       ├── entity/         # 实体类
│   │   │       │   ├── Customer.java
│   │   │       │   ├── Report.java
│   │   │       │   ├── Service.java
│   │   │       │   ├── Transaction.java
│   │   │       │   ├── TransactionItem.java
│   │   │       │   └── User.java
│   │   │       ├── exception/      # 异常处理
│   │   │       ├── repository/     # 数据访问层
│   │   │       │   ├── CustomerRepository.java
│   │   │       │   ├── ReportRepository.java
│   │   │       │   ├── ServiceRepository.java
│   │   │       │   ├── TransactionRepository.java
│   │   │       │   └── UserRepository.java
│   │   │       ├── service/        # 业务逻辑层
│   │   │       │   ├── CustomerService.java
│   │   │       │   ├── ReportService.java
│   │   │       │   ├── ServiceService.java
│   │   │       │   ├── TransactionService.java
│   │   │       │   └── UserService.java
│   │   │       └── RsCutOffSystemApplication.java
│   │   └── resources/
│   │       ├── static/             # 静态资源
│   │       ├── templates/          # 模板文件
│   │       ├── application.properties          # 开发环境配置
│   │       └── application-prod.properties     # 生产环境配置
│   └── test/                       # 测试文件
├── target/                         # 编译输出目录
├── .gitattributes                  # Git 属性配置
├── mvnw                           # Maven 包装器脚本 (Unix)
├── mvnw.cmd                       # Maven 包装器脚本 (Windows)
├── pom.xml                        # Maven 项目配置
└── README.md                      # 项目说明文档
```

## 🚀 快速开始

### 前置要求

- Java 17 或更高版本
- Maven 3.6+ 或使用项目自带的 Maven 包装器
- IDE：IntelliJ IDEA（推荐）或 Eclipse

### 1. 克隆项目

```bash
git clone <repository-url>
cd RsCutOffSystem
```

### 2. 构建项目

使用 Maven 包装器（推荐）：

```bash
# Windows
./mvnw clean install

# macOS/Linux
./mvnw clean install
```

或使用本地 Maven：

```bash
mvn clean install
```

### 3. 运行应用

```bash
# 使用 Maven 包装器
./mvnw spring-boot:run

# 或使用 java -jar
java -jar target/RsCutOffSystem-*.jar
```

### 4. 访问应用

- **应用地址**: http://localhost:8080
- **H2 数据库控制台**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:./data/rscutoff_dev`
  - 用户名: `sa`
  - 密码: (空)

## 🔧 开发环境配置

### IDE 配置

#### IntelliJ IDEA
1. 打开项目根目录
2. 确保 Project SDK 设置为 Java 17+
3. 安装推荐插件：
  - Lombok Plugin
  - Spring Boot Helper
  - Database Navigator

#### 开发环境特性
- **热重载**: 使用 Spring Boot DevTools，修改代码后自动重启
- **数据库**: H2 文件数据库，数据持久化到 `./data/` 目录
- **日志级别**: DEBUG，便于开发调试
- **SQL 显示**: 启用 SQL 语句打印和格式化

### 环境变量配置

开发环境不需要额外的环境变量配置，所有配置都在 `application.properties` 中。

### 数据库初始化

首次启动时，Hibernate 会自动创建数据库表结构（`ddl-auto=create`）。

## 📚 API 文档

系统提供完整的 RESTful API 接口，详细文档请参考：

- [用户管理 API](./API文档/User%20API%20文档.md) - 员工账户和权限管理
- [客户管理 API](./API文档/Customer%20API%20文档.md) - 客户信息和会员管理
- [服务管理 API](./API文档/Service%20API%20文档.md) - 服务项目和价格管理
- [交易管理 API](./API文档/Transaction%20API%20文档.md) - 财务交易和日结管理
- [报表管理 API](./API文档/Report%20API文档.md) - 数据分析和报表生成

### API 基础信息

- **基础 URL**: `http://localhost:8080/api`
- **响应格式**: JSON
- **字符编码**: UTF-8
- **时区**: Asia/Shanghai
- **跨域**: 已开启 CORS 支持

### 常用 API 端点

```bash
# 健康检查
GET /api/health

# 用户管理
GET /api/users
POST /api/users

# 客户管理  
GET /api/customers
POST /api/customers

# 服务管理
GET /api/services
POST /api/services

# 交易管理
GET /api/transactions
POST /api/transactions

# 报表管理
GET /api/reports
POST /api/reports
```

## 🧪 测试环境

### 前端开发测试配置

当前端开发人员需要连接后端 API 时：

#### 1. 启动后端服务

```bash
# 确保后端服务运行在 8080 端口
./mvnw spring-boot:run
```

#### 2. 前端项目配置

在前端项目中配置 API 基础 URL：

```javascript
// 开发环境配置
const API_BASE_URL = 'http://localhost:8080/api';

// 或使用环境变量
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
```

#### 3. 跨域处理

后端已配置 CORS 允许所有来源访问（`@CrossOrigin(origins = "*")`），前端无需额外配置。

#### 4. 测试数据

系统提供初始化接口创建测试数据：

```bash
# 初始化默认服务
POST http://localhost:8080/api/services/initialize

# 创建测试用户
POST http://localhost:8080/api/users
{
  "username": "testuser",
  "password": "password123",
  "name": "测试用户",
  "email": "test@example.com",
  "role": "STAFF"
}
```

### API 测试工具

推荐使用以下工具测试 API：

- **Postman** - GUI 工具，方便调试
- **curl** - 命令行工具
- **Insomnia** - 轻量级 API 客户端

### 示例 API 调用

```bash
# 获取所有用户
curl -X GET http://localhost:8080/api/users

# 创建客户
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "张三",
    "phone": "1234567890",
    "customerType": "MEMBER",
    "membershipLevel": "GOLD"
  }'

# 创建交易
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "type": "INCOME",
    "transactionDate": "2024-06-18",
    "relatedUserUuids": ["user-uuid"],
    "paymentMethod": "CASH",
    "items": [{
      "category": "SERVICE_MASSAGE",
      "amount": 100.00
    }]
  }'
```

## 🚀 生产环境部署

### 1. 环境准备

#### 服务器要求
- **操作系统**: Linux (Ubuntu 20.04+ 推荐)
- **Java**: OpenJDK 17 或 Oracle JDK 17
- **内存**: 最少 2GB，推荐 4GB+
- **磁盘**: 10GB+ 可用空间
- **数据库**: MySQL 8.0+

#### 安装 Java 17

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# CentOS/RHEL
sudo yum install java-17-openjdk-devel

# 验证安装
java -version
```

### 2. 数据库配置

#### MySQL 安装和配置

```bash
# Ubuntu/Debian
sudo apt install mysql-server

# 启动服务
sudo systemctl start mysql
sudo systemctl enable mysql

# 安全配置
sudo mysql_secure_installation
```

#### 创建数据库和用户

```sql
-- 连接到 MySQL
mysql -u root -p

-- 创建数据库
CREATE DATABASE rscutoff_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户
CREATE USER 'rscutoff_user'@'localhost' IDENTIFIED BY 'your_secure_password';

-- 授权
GRANT ALL PRIVILEGES ON rscutoff_db.* TO 'rscutoff_user'@'localhost';
FLUSH PRIVILEGES;

-- 退出
EXIT;
```

### 3. 应用配置

#### 生产环境配置文件

在 `application-prod.properties` 中已配置生产环境参数：

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/rscutoff_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
spring.datasource.username=rscutoff_user
spring.datasource.password=${DB_PASSWORD:rscutoff_password}

# JPA 配置
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# 日志配置
logging.level.com.relaxationspa.rscutoffsystem=INFO
```

#### 环境变量设置

```bash
# 设置数据库密码
export DB_PASSWORD=your_secure_password

# 或创建环境配置文件
echo "DB_PASSWORD=your_secure_password" > /opt/rscutoff/.env
```

### 4. 构建和部署

#### 构建应用

```bash
# 生产环境构建
./mvnw clean package -Pprod

# 或跳过测试快速构建
./mvnw clean package -DskipTests
```

#### 部署应用

```bash
# 创建应用目录
sudo mkdir -p /opt/rscutoff
sudo chown $USER:$USER /opt/rscutoff

# 复制 JAR 文件
cp target/RsCutOffSystem-*.jar /opt/rscutoff/app.jar

# 创建启动脚本
cat > /opt/rscutoff/start.sh << 'EOF'
#!/bin/bash
cd /opt/rscutoff
source .env 2>/dev/null || true
java -jar app.jar --spring.profiles.active=prod
EOF

chmod +x /opt/rscutoff/start.sh
```

### 5. 系统服务配置

#### 创建 systemd 服务

```bash
sudo tee /etc/systemd/system/rscutoff.service > /dev/null << 'EOF'
[Unit]
Description=RsCutOff System
After=network.target mysql.service

[Service]
Type=simple
User=ubuntu
WorkingDirectory=/opt/rscutoff
Environment=SPRING_PROFILES_ACTIVE=prod
EnvironmentFile=-/opt/rscutoff/.env
ExecStart=/usr/bin/java -jar app.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

# 重载配置
sudo systemctl daemon-reload

# 启动服务
sudo systemctl start rscutoff
sudo systemctl enable rscutoff

# 查看状态
sudo systemctl status rscutoff
```

### 6. 反向代理配置 (Nginx)

#### 安装 Nginx

```bash
sudo apt install nginx
```

#### 配置站点

```bash
sudo tee /etc/nginx/sites-available/rscutoff > /dev/null << 'EOF'
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
EOF

# 启用站点
sudo ln -s /etc/nginx/sites-available/rscutoff /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

### 7. SSL 证书配置 (Let's Encrypt)

```bash
# 安装 Certbot
sudo apt install certbot python3-certbot-nginx

# 获取证书
sudo certbot --nginx -d your-domain.com

# 自动续期
sudo crontab -e
# 添加以下行：
# 0 12 * * * /usr/bin/certbot renew --quiet
```

### 8. 数据库迁移

首次部署时需要初始化数据库结构：

```bash
# 临时设置 ddl-auto 为 create
# 在 application-prod.properties 中修改：
# spring.jpa.hibernate.ddl-auto=create

# 启动应用进行初始化
sudo systemctl start rscutoff

# 查看日志确认表创建成功
sudo journalctl -u rscutoff -f

# 停止应用，恢复配置
sudo systemctl stop rscutoff
# 将 ddl-auto 改回 validate

# 重新启动
sudo systemctl start rscutoff
```

### 9. 监控和维护

#### 日志管理

```bash
# 查看应用日志
sudo journalctl -u rscutoff -f

# 查看最近的错误
sudo journalctl -u rscutoff --since "1 hour ago" -p err

# 设置日志轮转
sudo tee /etc/logrotate.d/rscutoff > /dev/null << 'EOF'
/opt/rscutoff/logs/*.log {
    daily
    missingok
    rotate 30
    compress
    delaycompress
    notifempty
    create 0644 ubuntu ubuntu
}
EOF
```

#### 性能监控

```bash
# 监控应用进程
htop

# 监控数据库
mysql -u rscutoff_user -p -e "SHOW PROCESSLIST;"

# 磁盘使用情况
df -h

# 内存使用情况
free -h
```

#### 备份策略

```bash
# 创建数据库备份脚本
cat > /opt/rscutoff/backup.sh << 'EOF'
#!/bin/bash
BACKUP_DIR="/opt/rscutoff/backups"
DATE=$(date +%Y%m%d_%H%M%S)

mkdir -p $BACKUP_DIR

# 数据库备份
mysqldump -u rscutoff_user -p$DB_PASSWORD rscutoff_db > $BACKUP_DIR/db_backup_$DATE.sql

# 删除 30 天前的备份
find $BACKUP_DIR -name "*.sql" -mtime +30 -delete
EOF

chmod +x /opt/rscutoff/backup.sh

# 设置定时备份
sudo crontab -e
# 添加以下行（每天凌晨 2 点备份）：
# 0 2 * * * /opt/rscutoff/backup.sh
```

### 10. 安全配置

#### 防火墙设置

```bash
# 配置 ufw
sudo ufw allow ssh
sudo ufw allow 'Nginx Full'
sudo ufw allow mysql
sudo ufw enable
```

#### 应用安全

- 定期更新系统和 Java
- 使用强密码
- 限制数据库访问权限
- 配置 HTTPS
- 定期备份数据

## 🤝 贡献指南

我们欢迎任何形式的贡献！请遵循以下步骤：

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

### 代码规范

- 遵循 Java 代码规范
- 使用有意义的变量和方法名
- 添加适当的注释
- 编写单元测试

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 支持与联系

如果您在使用过程中遇到问题或有任何建议，请：

- 创建 [Issue](../../issues)
- 发送邮件至：support@relaxationspa.com
- 查看 [Wiki](../../wiki) 了解更多信息

---

**感谢您使用 RsCutOffSystem！** 🎉