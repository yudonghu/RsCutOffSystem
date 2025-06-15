# RsCutOffSystem API 文档

## 🏢 系统概述

RsCutOffSystem（轻松SPA日结系统）是一个专为按摩店设计的综合管理系统，提供客户管理、服务管理、交易处理和用户管理等完整功能。

### 🏗️ 技术架构
- **后端框架**: Spring Boot 3.x
- **数据库**: H2 (开发) / MySQL (生产)
- **API设计**: RESTful风格
- **数据格式**: JSON
- **认证方式**: 基于UUID的用户识别

### 📋 模块统计总览

| 模块名称 | 功能描述 | API总数 | 主要特性 |
|----------|----------|---------|----------|
| **Customer** | 客户管理 | **25个** | 会员体系、积分系统、生日提醒、客户分析 |
| **Service** | 服务管理 | **22个** | 服务分类、价格管理、菜单展示、批量操作 |
| **Transaction** | 交易管理 | **24个** | 交易记录、日结功能、财务统计、支付管理 |
| **User** | 用户管理 | **12个** | 员工管理、角色权限、状态跟踪、权限控制 |
| **总计** | **四大核心模块** | **83个API** | **完整的SPA管理生态系统** |

---

## 🧑‍🤝‍🧑 Customer 模块 (客户管理)

### 📊 模块概览
- **API总数**: 25个接口
- **核心实体**: Customer
- **主要功能**: 客户生命周期管理、会员体系、积分系统

### 🎯 核心功能特性
- ✅ **客户信息管理**: 完整的客户档案管理
- ✅ **三级会员体系**: 铜牌/银牌/金牌会员等级
- ✅ **积分系统**: 积分累积、消费、兑换机制
- ✅ **智能分类**: 自动识别会员/散客
- ✅ **营销工具**: 生日提醒、不活跃客户分析
- ✅ **数据统计**: 客户行为分析和统计报告

### 🔗 API接口详情

#### 基础CRUD操作 (5个)
```http
POST   /api/customers                    # 创建新客户
GET    /api/customers                    # 获取客户列表(支持分页排序)
GET    /api/customers/{uuid}             # 根据UUID获取客户详情
PUT    /api/customers/{uuid}             # 更新客户信息
DELETE /api/customers/{uuid}             # 软删除客户(设为不活跃)
DELETE /api/customers/{uuid}/hard        # 彻底删除客户
```

#### 查询检索接口 (12个)
```http
GET /api/customers/number/{customerNumber}     # 根据客户编号查询
GET /api/customers/phone/{phone}               # 根据电话号码查询
GET /api/customers/search?name={name}          # 根据姓名模糊搜索
GET /api/customers/active                      # 获取所有活跃客户
GET /api/customers/members                     # 获取所有会员客户
GET /api/customers/walk-in                     # 获取所有散客
GET /api/customers/type/{type}                 # 根据客户类型查询
GET /api/customers/status/{status}             # 根据客户状态查询
GET /api/customers/membership/{level}          # 根据会员等级查询
GET /api/customers/members/expiring?days=30    # 获取即将到期的会员
GET /api/customers/birthday/today              # 获取今天生日的客户
GET /api/customers/birthday/month/{month}      # 获取指定月份生日的客户
GET /api/customers/inactive?days=90            # 获取不活跃客户
```

#### 业务操作接口 (6个)
```http
POST /api/customers/{uuid}/points/add?points=100      # 增加客户积分
POST /api/customers/{uuid}/points/deduct?points=50    # 扣除客户积分
POST /api/customers/{uuid}/visit?spentAmount=200      # 更新客户访问信息
```

#### 统计功能接口 (2个)
```http
GET /api/customers/stats                 # 获取客户统计信息
GET /api/customers/{uuid}/exists         # 检查客户是否存在
```

### 📋 数据模型说明

#### 客户类型 (CustomerType)
- `MEMBER` - 会员客户
- `WALK_IN` - 散客

#### 客户状态 (CustomerStatus)
- `ACTIVE` - 活跃状态
- `INACTIVE` - 不活跃状态

#### 会员等级 (MembershipLevel)
- `BRONZE` - 铜牌会员
- `SILVER` - 银牌会员
- `GOLD` - 金牌会员

### 💡 使用示例

#### 创建新客户
```javascript
const customerData = {
  "name": "张三",
  "phone": "13800138000", 
  "gender": "MALE",
  "birthDate": "1990-01-15",
  "customerType": "WALK_IN",
  "address": "北京市朝阳区xxx街道"
};

fetch('/api/customers', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(customerData)
})
.then(response => response.json())
.then(customer => console.log('客户创建成功:', customer.uuid));
```

#### 查询今日生日客户
```javascript
fetch('/api/customers/birthday/today')
  .then(response => response.json())
  .then(customers => {
    console.log(`今日有 ${customers.length} 位客户生日`);
    customers.forEach(customer => {
      console.log(`${customer.name} - ${customer.phone}`);
    });
  });
```

---

## 🛎️ Service 模块 (服务管理)

### 📊 模块概览
- **API总数**: 22个接口
- **核心实体**: Service
- **主要功能**: 服务目录管理、价格体系、服务分类

### 🎯 核心功能特性
- ✅ **服务分类管理**: 椅背按摩、足疗、身体按摩、组合服务
- ✅ **灵活定价**: 支持不同价格策略和批量价格更新
- ✅ **服务菜单**: 按类型组织的专业服务展示
- ✅ **时长管理**: 精确的服务时间规划
- ✅ **状态控制**: 服务上下架和可用性管理
- ✅ **搜索筛选**: 多条件服务查询和筛选

### 🔗 API接口详情

#### 基础CRUD操作 (5个)
```http
POST   /api/services                     # 创建新服务
GET    /api/services                     # 获取服务列表(支持分页排序)
GET    /api/services/{uuid}              # 根据UUID获取服务详情
PUT    /api/services/{uuid}              # 更新服务信息
DELETE /api/services/{uuid}              # 软删除服务
DELETE /api/services/{uuid}/hard         # 彻底删除服务
```

#### 分类查询接口 (8个)
```http
GET /api/services/code/{serviceCode}        # 根据服务代码获取服务
GET /api/services/active                    # 获取所有可用服务
GET /api/services/type/{serviceType}        # 根据服务类型获取服务
GET /api/services/chair-massage             # 获取椅背按摩服务
GET /api/services/foot-reflexology          # 获取足疗服务
GET /api/services/body-massage              # 获取身体按摩服务
GET /api/services/combination-massage       # 获取组合按摩服务
GET /api/services/menu                      # 获取服务菜单(按类型分组)
```

#### 条件筛选接口 (5个)
```http
GET /api/services/price-range?minPrice=50&maxPrice=200           # 根据价格范围获取服务
GET /api/services/duration-range?minDuration=30&maxDuration=90   # 根据时长范围获取服务
GET /api/services/search?keyword=按摩                            # 搜索服务
GET /api/services/quick                                          # 获取快速服务(30分钟以下)
GET /api/services/long                                           # 获取长时间服务(60分钟以上)
```

#### 业务操作接口 (4个)
```http
POST /api/services/{uuid}/activate          # 激活服务
POST /api/services/{uuid}/deactivate        # 停用服务
PUT  /api/services/batch-update-prices      # 批量更新价格
POST /api/services/initialize               # 初始化默认服务
GET  /api/services/stats                    # 获取服务统计
GET  /api/services/{uuid}/exists            # 检查服务是否存在
```

### 📋 数据模型说明

#### 服务类型 (ServiceType)
- `CHAIR_MASSAGE` - 椅背按摩
- `FOOT_REFLEXOLOGY` - 足疗
- `BODY_MASSAGE` - 身体按摩
- `COMBINATION_MASSAGE` - 组合按摩

### 💡 使用示例

#### 创建新服务
```javascript
const serviceData = {
  "serviceName": "经典足疗",
  "serviceCode": "FOOT001", 
  "serviceType": "FOOT_REFLEXOLOGY",
  "price": 88.00,
  "duration": 60,
  "description": "传统中式足底按摩，缓解疲劳",
  "isActive": true,
  "displayOrder": 1
};

fetch('/api/services', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(serviceData)
})
.then(response => response.json())
.then(service => console.log('服务创建成功:', service.serviceName));
```

#### 获取服务菜单
```javascript
fetch('/api/services/menu')
  .then(response => response.json())
  .then(menu => {
    menu.forEach(category => {
      console.log(`${category.categoryName}:`);
      category.services.forEach(service => {
        console.log(`  - ${service.serviceName}: ¥${service.price} (${service.duration}分钟)`);
      });
    });
  });
```

---

## 💳 Transaction 模块 (交易管理)

### 📊 模块概览
- **API总数**: 24个接口
- **核心实体**: Transaction, TransactionItem
- **主要功能**: 财务交易管理、日结功能、统计分析

### 🎯 核心功能特性
- ✅ **完整交易记录**: 收入/支出全记录管理
- ✅ **多种支付方式**: 现金、刷卡、支付宝、微信等
- ✅ **详细交易分类**: 精确的收支分类管理
- ✅ **自动日结功能**: 每日财务自动结算
- ✅ **实时统计分析**: 多维度财务数据分析
- ✅ **交易状态跟踪**: 完整的交易生命周期管理

### 🔗 API接口详情

#### 基础CRUD操作 (4个)
```http
POST   /api/transactions                 # 创建新交易
GET    /api/transactions                 # 获取交易列表(支持分页排序)
GET    /api/transactions/{uuid}          # 根据UUID获取交易详情  
PUT    /api/transactions/{uuid}          # 更新交易信息
DELETE /api/transactions/{uuid}          # 删除交易
```

#### 查询检索接口 (10个)
```http
GET /api/transactions/number/{transactionNumber}          # 根据交易编号获取交易
GET /api/transactions/type/{type}                         # 根据交易类型获取交易
GET /api/transactions/status/{status}                     # 根据交易状态获取交易
GET /api/transactions/date/{date}                         # 根据交易日期获取交易
GET /api/transactions/date-range?startDate=&endDate=      # 根据日期范围获取交易
GET /api/transactions/income                              # 获取收入交易
GET /api/transactions/expense                             # 获取支出交易  
GET /api/transactions/pending                             # 获取待确认的交易
GET /api/transactions/unprocessed                         # 获取未处理的交易
GET /api/transactions/customer/{customerUuid}             # 根据客户获取交易历史
GET /api/transactions/user/{userUuid}                     # 根据员工获取交易历史
```

#### 业务操作接口 (3个)
```http
POST /api/transactions/{uuid}/confirm    # 确认交易
POST /api/transactions/{uuid}/cancel     # 取消交易
```

#### 日结功能接口 (3个)
```http
POST /api/transactions/daily-cutoff?cutOffDate={date}     # 执行日结
GET  /api/transactions/daily-cutoff/{cutOffDate}          # 获取日结报告
GET  /api/transactions/daily-cutoff/today                 # 获取今日日结报告
```

#### 统计分析接口 (4个)
```http
GET /api/transactions/stats?startDate=&endDate=     # 获取交易统计
GET /api/transactions/stats/today                   # 获取今日交易统计
GET /api/transactions/stats/this-week               # 获取本周交易统计  
GET /api/transactions/stats/this-month              # 获取本月交易统计
GET /api/transactions/{uuid}/exists                 # 检查交易是否存在
```

### 📋 数据模型说明

#### 交易类型 (TransactionType)
- `INCOME` - 收入
- `EXPENSE` - 支出

#### 交易状态 (TransactionStatus)
- `PENDING` - 待确认
- `CONFIRMED` - 已确认
- `CANCELLED` - 已取消

#### 支付方式 (PaymentMethod)
- `CASH` - 现金
- `CARD` - 刷卡
- `ALIPAY` - 支付宝
- `WECHAT` - 微信支付
- `OTHER` - 其他

#### 交易分类 (TransactionCategory)
**收入类别:**
- `SERVICE_MASSAGE` - 按摩服务收入
- `SERVICE_TIP` - 小费收入
- `MEMBERSHIP_FEE` - 会员费收入
- `PRODUCT_SALES` - 产品销售收入

**支出类别:**
- `SALARY_MASSAGE_THERAPIST` - 按摩师工资
- `SALARY_RECEPTIONIST` - 前台工资
- `RENT` - 房租支出
- `UTILITIES` - 水电费
- `MARKETING` - 营销费用
- `OTHER_EXPENSE` - 其他支出

### 💡 使用示例

#### 创建收入交易
```javascript
const incomeTransaction = {
  "transactionType": "INCOME",
  "totalAmount": 188.00,
  "paymentMethod": "CASH", 
  "customerUuid": "customer-uuid-here",
  "handledByUuid": "user-uuid-here",
  "notes": "足疗服务 + 小费",
  "transactionItems": [
    {
      "category": "SERVICE_MASSAGE",
      "description": "经典足疗60分钟",
      "amount": 88.00,
      "quantity": 1
    },
    {
      "category": "SERVICE_TIP",
      "description": "服务小费", 
      "amount": 100.00,
      "quantity": 1
    }
  ]
};

fetch('/api/transactions', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(incomeTransaction)
})
.then(response => response.json())
.then(transaction => console.log('交易创建成功:', transaction.transactionNumber));
```

#### 执行日结
```javascript
const today = new Date().toISOString().split('T')[0];
fetch(`/api/transactions/daily-cutoff?cutOffDate=${today}`, {
  method: 'POST'
})
.then(response => response.json())
.then(report => {
  console.log('=== 日结报告 ===');
  console.log(`日期: ${report.cutOffDate}`);
  console.log(`总收入: ¥${report.totalIncome}`);
  console.log(`总支出: ¥${report.totalExpense}`);
  console.log(`净利润: ¥${report.netProfit}`);
  console.log(`交易笔数: ${report.totalTransactions}`);
});
```

---

## 👥 User 模块 (用户管理)

### 📊 模块概览
- **API总数**: 12个接口
- **核心实体**: User
- **主要功能**: 员工管理、角色权限、状态跟踪

### 🎯 核心功能特性
- ✅ **员工档案管理**: 完整的员工信息管理
- ✅ **多角色权限**: 经理、前台、按摩师、清洁员角色
- ✅ **在职状态跟踪**: 在职、离职、停职状态管理
- ✅ **唯一标识管理**: 员工编号和昵称系统
- ✅ **权限控制**: 基于角色的功能权限控制

### 🔗 API接口详情

#### 基础CRUD操作 (5个)
```http
POST   /api/users                       # 创建新用户
GET    /api/users                       # 获取用户列表(支持分页排序)
GET    /api/users/{uuid}                # 根据UUID获取用户详情
PUT    /api/users/{uuid}                # 更新用户信息
DELETE /api/users/{uuid}                # 软删除用户(设为离职状态)
DELETE /api/users/{uuid}/hard           # 彻底删除用户
```

#### 查询检索接口 (5个)
```http
GET /api/users/employee/{employeeNumber}    # 根据员工编号获取用户
GET /api/users/nickname/{nickname}          # 根据昵称获取用户  
GET /api/users/active                       # 获取所有在职用户
GET /api/users/role/{role}                  # 根据角色获取用户
GET /api/users/status/{status}              # 根据在职状态获取用户
```

#### 统计功能接口 (2个)
```http
GET /api/users/{uuid}/exists            # 检查用户是否存在
```

### 📋 数据模型说明

#### 用户角色 (Role)
- `MANAGER` - 经理 (最高权限)
- `RECEPTIONIST` - 前台接待
- `MASSAGE_THERAPIST` - 按摩师
- `CLEANER` - 清洁员

#### 在职状态 (EmploymentStatus)
- `ACTIVE` - 在职
- `INACTIVE` - 离职
- `SUSPENDED` - 停职

### 💡 使用示例

#### 创建新员工
```javascript
const employeeData = {
  "name": "李师傅",
  "nickname": "李师傅",
  "phone": "13900139000",
  "email": "li@spa.com",
  "role": "MASSAGE_THERAPIST",
  "employmentStatus": "ACTIVE",
  "hireDate": "2024-01-15"
};

fetch('/api/users', {
  method: 'POST', 
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(employeeData)
})
.then(response => response.json())
.then(user => console.log('员工创建成功:', user.employeeNumber));
```

#### 获取所有按摩师
```javascript
fetch('/api/users/role/MASSAGE_THERAPIST')
  .then(response => response.json())
  .then(therapists => {
    console.log(`共有 ${therapists.length} 位按摩师:`);
    therapists.forEach(therapist => {
      console.log(`- ${therapist.name} (${therapist.nickname}) - ${therapist.phone}`);
    });
  });
```

---

## 🔧 系统配置与环境

### 📋 环境配置文件

#### 开发环境 (application.properties)
```properties
# 服务器配置
server.port=8080
spring.application.name=RsCutOffSystem

# H2数据库配置 (开发环境)
spring.datasource.url=jdbc:h2:file:./data/rscutoff_dev
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA配置
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
spring.jpa.properties.hibernate.format_sql=true

# H2控制台配置
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# 日志配置
logging.level.com.relaxationspa.rscutoffsystem=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG

# JSON时间格式配置
spring.jackson.time-zone=Asia/Shanghai
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
```

#### 生产环境 (application-prod.properties)
```properties
# MySQL数据库配置 (生产环境)
spring.datasource.url=jdbc:mysql://localhost:3306/rscutoff_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
spring.datasource.username=rscutoff_user
spring.datasource.password=${DB_PASSWORD:rscutoff_password}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA配置 (生产环境)
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# 关闭H2控制台
spring.h2.console.enabled=false

# 生产环境日志配置
logging.level.com.relaxationspa.rscutoffsystem=INFO
logging.level.org.springframework.web=WARN
logging.level.org.hibernate.SQL=WARN
```

### 🛡️ 异常处理机制

系统提供统一的异常处理，所有API都遵循以下错误响应格式：

#### 资源未找到 (404)
```json
{
  "code": "RESOURCE_NOT_FOUND",
  "message": "客户未找到: 550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2024-03-15T10:30:00"
}
```

#### 验证失败 (400)
```json
{
  "code": "VALIDATION_FAILED",
  "message": "请求参数验证失败", 
  "fieldErrors": {
    "name": "姓名不能为空",
    "phone": "电话格式不正确",
    "email": "邮箱格式无效"
  },
  "timestamp": "2024-03-15T10:30:00"
}
```

#### 服务器错误 (500)
```json
{
  "code": "INTERNAL_SERVER_ERROR",
  "message": "服务器内部错误", 
  "timestamp": "2024-03-15T10:30:00"
}
```

---

## 🚀 快速开始指南

### 1. 环境准备
```bash
# 确保Java 17+已安装
java -version

# 确保Maven已安装
mvn -version
```

### 2. 启动应用
```bash
# 开发环境启动
mvn spring-boot:run

# 生产环境启动
mvn spring-boot:run -Dspring.profiles.active=prod
```

### 3. 访问应用
- **应用地址**: http://localhost:8080
- **H2控制台** (开发环境): http://localhost:8080/h2-console
- **数据库连接**: jdbc:h2:file:./data/rscutoff_dev

### 4. API测试
```bash
# 测试客户统计API
curl http://localhost:8080/api/customers/stats

# 测试服务列表API  
curl http://localhost:8080/api/services?page=0&size=5

# 创建测试客户
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "测试客户",
    "phone": "13800138000", 
    "customerType": "WALK_IN"
  }'
```

---

## 📈 性能特性与最佳实践

### 🔍 分页查询支持
所有列表接口都支持标准分页参数：
```http
GET /api/customers?page=0&size=10&sortBy=createdAt&sortDir=desc
```
- `page`: 页码 (从0开始)
- `size`: 每页大小 (默认10)
- `sortBy`: 排序字段 (默认createdAt)
- `sortDir`: 排序方向 (asc/desc，默认desc)

### 🗂️ 数据库索引优化
- **Customer**: phone, customerNumber字段建立索引
- **Service**: serviceCode, serviceType字段建立索引
- **Transaction**: transactionDate, customerUuid字段建立索引
- **User**: employeeNumber, nickname字段建立索引

### 🛡️ 数据验证策略
- **前端验证**: 基础格式和必填项验证
- **后端验证**: Bean Validation注解 + 业务逻辑验证
- **数据库约束**: 唯一性约束和外键约束

### 🔒 软删除策略
- **Customer**: 状态设为INACTIVE，保留历史数据
- **User**: 状态设为INACTIVE，保留员工记录
- **Service**: isActive设为false，保留服务历史
- **Transaction**: 支持真实删除，但建议谨慎操作

---

## 🎯 API使用最佳实践

### 1. 错误处理
```javascript
async function apiCall(url, options) {
  try {
    const response = await fetch(url, options);
    
    if (!response.ok) {
      const error = await response.json();
      throw new Error(`${error.code}: ${error.message}`);
    }
    
    return await response.json();
  } catch (error) {
    console.error('API调用失败:', error.message);
    throw error;
  }
}
```

### 2. 分页数据处理
```javascript
async function loadCustomers(page = 0, size = 10) {
  const response = await fetch(`/api/customers?page=${page}&size=${size}`);
  const data = await response.json();
  
  console.log(`总计: ${data.totalElements} 客户`);
  console.log(`总页数: ${data.totalPages}`);
  console.log(`当前页: ${data.number + 1}`);
  
  return data.content; // 返回客户列表
}
```

### 3. 表单验证
```javascript
function validateCustomer(customerData) {
  const errors = {};
  
  if (!customerData.name || customerData.name.trim() === '') {
    errors.name = '客户姓名不能为空';
  }
  
  if (!customerData.phone || !/^1[3-9]\d{9}$/.test(customerData.phone)) {
    errors.phone = '请输入有效的手机号码';
  }
  
  return Object.keys(errors).length === 0 ? null : errors;
}
```

---

## 📞 技术支持与联系

### 🆘 常见问题
1. **数据库连接失败**: 检查配置文件中的数据库连接信息
2. **API返回404**: 确认URL路径和HTTP方法正确
3. **验证失败**: 检查请求参数格式和必填字段
4. **