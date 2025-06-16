# Relaxation Spa 日结管理系统 - 后端API

## 📋 项目概述

Relaxation Spa 日结管理系统是一个专为SPA/按摩店设计的完整管理系统，提供客户管理、服务管理、交易记录、财务统计和报表生成等核心功能。

### 🎯 核心功能
- 👥 **客户管理**: 会员体系、积分系统、客户画像分析
- 🛍️ **服务管理**: 服务分类、价格管理、服务菜单
- 💰 **交易管理**: 收支记录、支付方式、日结操作
- 👨‍💼 **员工管理**: 角色权限、员工档案、绩效统计
- 📊 **报表系统**: 财务汇总、数据分析、多维度统计

---

## 🛠️ 技术栈

### 后端框架
- **Spring Boot** 3.x
- **Spring Data JPA** - 数据持久化
- **Spring Web** - RESTful API
- **Jakarta Validation** - 数据验证
- **H2 Database** (开发环境)
- **MySQL** (生产环境)

### 核心特性
- RESTful API 设计
- 分页查询支持
- 跨域访问支持 (CORS)
- UUID 主键设计
- 数据验证和错误处理
- 多环境配置支持

---

## 🚀 快速开始

### 环境要求
- Java 17 或更高版本
- Maven 3.6+
- MySQL 8.0+ (生产环境)

### 1. 克隆项目
```bash
git clone <repository-url>
cd rscutoffsystem
```

### 2. 开发环境启动
```bash
# 使用内置H2数据库
mvn spring-boot:run

# 或者指定开发环境
mvn spring-boot:run -Dspring.profiles.active=dev
```

### 3. 访问应用
- **API Base URL**: http://localhost:8080/api
- **H2 控制台**: http://localhost:8080/h2-console
    - JDBC URL: `jdbc:h2:file:./data/rscutoff_dev`
    - 用户名: `sa`
    - 密码: (空)

---

## 📚 API 模块说明

### 1. 客户管理 API (`/api/customers`)
```http
GET    /api/customers              # 获取客户列表(分页)
POST   /api/customers              # 创建新客户
GET    /api/customers/{uuid}       # 获取客户详情
PUT    /api/customers/{uuid}       # 更新客户信息
DELETE /api/customers/{uuid}       # 删除客户(软删除)

# 业务操作
POST   /api/customers/{uuid}/points/add     # 增加积分
POST   /api/customers/{uuid}/visit          # 更新访问信息
GET    /api/customers/active               # 获取活跃客户
GET    /api/customers/members/expiring     # 即将到期会员
```

### 2. 服务管理 API (`/api/services`)
```http
GET    /api/services               # 获取服务列表(分页)
POST   /api/services               # 创建新服务
GET    /api/services/{uuid}        # 获取服务详情
PUT    /api/services/{uuid}        # 更新服务信息

# 分类查询
GET    /api/services/chair-massage      # 椅背按摩服务
GET    /api/services/foot-reflexology   # 足疗服务
GET    /api/services/menu               # 服务菜单(按类型分组)
```

### 3. 交易管理 API (`/api/transactions`)
```http
GET    /api/transactions           # 获取交易列表(分页)
POST   /api/transactions           # 创建新交易
GET    /api/transactions/{uuid}    # 获取交易详情
PUT    /api/transactions/{uuid}    # 更新交易信息

# 日结操作
POST   /api/transactions/daily-cutoff      # 执行日结
GET    /api/transactions/daily-cutoff/today # 今日日结报告
GET    /api/transactions/stats/today       # 今日交易统计
```

### 4. 员工管理 API (`/api/users`)
```http
GET    /api/users                  # 获取员工列表(分页)
POST   /api/users                  # 创建新员工
GET    /api/users/{uuid}           # 获取员工详情
PUT    /api/users/{uuid}           # 更新员工信息

# 角色查询
GET    /api/users/role/THERAPIST   # 获取技师列表
GET    /api/users/active           # 获取在职员工
```

### 5. 报表管理 API (`/api/reports`)
```http
GET    /api/reports                # 获取报表列表(分页)
POST   /api/reports                # 创建新报表
GET    /api/reports/{uuid}         # 获取报表详情

# 快速报表生成
POST   /api/reports/financial-summary/today    # 今日财务汇总
POST   /api/reports/financial-summary/this-month # 本月财务汇总
GET    /api/reports/stats                      # 报表统计信息
```

---

## 🗄️ 数据库设计

### 核心实体

#### Customer (客户)
```sql
- uuid (主键)
- customer_number (客户编号)
- name (姓名)
- phone (电话)
- email (邮箱)
- customer_type (客户类型: WALK_IN/MEMBER)
- membership_level (会员等级)
- points (积分)
- total_spent (总消费)
- status (状态: ACTIVE/INACTIVE/BLOCKED)
```

#### Service (服务)
```sql
- uuid (主键)
- service_code (服务代码)
- service_name (服务名称)
- service_type (服务类型)
- duration_minutes (时长)
- price (价格)
- status (状态: ACTIVE/INACTIVE)
```

#### Transaction (交易)
```sql
- uuid (主键)
- transaction_number (交易编号)
- type (类型: INCOME/EXPENSE)
- total_amount (总金额)
- payment_method (支付方式)
- status (状态: PENDING/CONFIRMED/CANCELLED)
- related_customer_uuid (关联客户)
- related_user_uuid (关联员工)
```

#### User (员工)
```sql
- uuid (主键)
- employee_number (员工编号)
- nickname (昵称)
- roles (角色: ADMIN/MANAGER/CASHIER/THERAPIST/RECEPTIONIST)
- employment_status (在职状态)
```

---

## 🧪 API 测试

### 使用 curl 测试示例

#### 1. 创建客户
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "张三",
    "phone": "13812345678",
    "customerType": "MEMBER",
    "membershipLevel": "BRONZE"
  }'
```

#### 2. 创建服务
```bash
curl -X POST http://localhost:8080/api/services \
  -H "Content-Type: application/json" \
  -d '{
    "serviceCode": "CM001",
    "serviceName": "经典椅背按摩",
    "serviceType": "CHAIR_MASSAGE",
    "durationMinutes": 30,
    "price": 68.00
  }'
```

#### 3. 创建交易
```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "type": "INCOME",
    "transactionDate": "2024-01-01",
    "description": "椅背按摩服务",
    "relatedCustomerUuid": "customer-uuid-here",
    "relatedUserUuid": "user-uuid-here",
    "paymentMethod": "CASH",
    "items": [{
      "category": "CHAIR_MASSAGE_SERVICE",
      "amount": 68.00,
      "description": "经典椅背按摩30分钟"
    }]
  }'
```

#### 4. 获取今日统计
```bash
curl http://localhost:8080/api/transactions/stats/today
```

### Postman 测试集合
建议创建 Postman Collection 包含所有 API 端点，方便前端开发时测试。

---

## 🔧 配置说明

### 开发环境配置 (`application.properties`)
```properties
# 服务端口
server.port=8080

# H2 数据库配置
spring.datasource.url=jdbc:h2:file:./data/rscutoff_dev
spring.h2.console.enabled=true

# JPA 配置
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# 日志配置
logging.level.com.relaxationspa.rscutoffsystem=DEBUG
```

### 生产环境配置 (`application-prod.properties`)
```properties
# MySQL 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/rscutoff_db
spring.datasource.username=rscutoff_user
spring.datasource.password=${DB_PASSWORD}

# 生产环境 JPA 配置
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
```

---

## 🚀 部署指南

### 1. 开发环境部署
```bash
# 直接运行
mvn spring-boot:run

# 或打包后运行
mvn clean package
java -jar target/rscutoffsystem-0.0.1-SNAPSHOT.jar
```

### 2. 生产环境部署

#### 准备数据库
```sql
-- 创建数据库
CREATE DATABASE rscutoff_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户
CREATE USER 'rscutoff_user'@'%' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON rscutoff_db.* TO 'rscutoff_user'@'%';
FLUSH PRIVILEGES;
```

#### 部署应用
```bash
# 设置环境变量
export DB_PASSWORD=your_secure_password
export SPRING_PROFILES_ACTIVE=prod

# 打包应用
mvn clean package -Pprod

# 运行应用
java -jar target/rscutoffsystem-0.0.1-SNAPSHOT.jar
```

### 3. Docker 部署 (可选)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/rscutoffsystem-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

```bash
# 构建镜像
docker build -t rscutoff-backend .

# 运行容器
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_PASSWORD=your_password \
  rscutoff-backend
```

---

## 📊 核心业务流程

### 1. 客户服务完整流程
```
1. 客户到店 → GET /api/customers/search?name=客户姓名
2. 选择服务 → GET /api/services/active
3. 创建交易 → POST /api/transactions
4. 更新客户 → POST /api/customers/{uuid}/visit
```

### 2. 日结操作流程
```
1. 查看今日统计 → GET /api/transactions/stats/today
2. 执行日结 → POST /api/transactions/daily-cutoff
3. 生成报表 → POST /api/reports/quick/daily-cutoff
```

### 3. 员工绩效分析
```
1. 获取员工列表 → GET /api/users/role/THERAPIST
2. 查看个人交易 → GET /api/transactions/user/{uuid}
3. 生成绩效报表 → POST /api/reports (EMPLOYEE_PERFORMANCE)
```

---

## 🔍 常见问题

### Q: 如何重置开发数据库？
A: 删除 `./data/rscutoff_dev.mv.db` 文件，重启应用即可自动重建。

### Q: 如何查看实时 SQL 语句？
A: 开发环境已配置 `spring.jpa.show-sql=true`，控制台会显示所有 SQL。

### Q: 如何添加初始化数据？
A: 可以创建 `data.sql` 文件在 `src/main/resources/` 目录下，或使用 `@PostConstruct` 方法。

### Q: API 返回 CORS 错误怎么办？
A: 所有 Controller 已配置 `@CrossOrigin(origins = "*")`，如果仍有问题，请检查前端请求头。

### Q: 如何修改服务端口？
A: 在 `application.properties` 中修改 `server.port=8080` 为其他端口。

---

## 📞 开发支持

### API 文档
- 详细的 API 文档已创建，包含所有端点的请求/响应示例
- 每个模块都有完整的使用示例和业务场景

### 前端集成建议
```javascript
// API 基础配置
const API_BASE_URL = 'http://localhost:8080/api';

// 示例 API 调用
const getCustomers = (params) => {
  return axios.get(`${API_BASE_URL}/customers`, { params });
};

const createTransaction = (data) => {
  return axios.post(`${API_BASE_URL}/transactions`, data);
};
```

### 开发工具推荐
- **API 测试**: Postman 或 Insomnia
- **数据库查看**: H2 Console (开发) / MySQL Workbench (生产)
- **日志查看**: 控制台输出或日志文件

---

## 🎯 下一步开发

后端 API 已经完整实现，接下来可以：

1. **前端开发**: 使用 React + Ant Design Pro
2. **API 测试**: 创建完整的测试用例
3. **性能优化**: 添加缓存和索引优化
4. **安全增强**: 添加认证和授权机制
5. **监控告警**: 添加应用监控和日志分析

---

**项目状态**: ✅ 后端开发完成，可以开始前端开发
**最后更新**: 2024年6月