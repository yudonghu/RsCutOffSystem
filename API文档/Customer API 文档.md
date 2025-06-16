# Customer API 文档

## 概述
Customer API 提供完整的客户管理功能，包括基础CRUD操作、查询检索、业务操作和统计分析。

## 基础信息
- **Base URL**: `/api/customers`
- **Content-Type**: `application/json`
- **支持跨域**: 是

---

## 1. 基础CRUD操作

### 1.1 创建客户
**POST** `/api/customers`

#### 请求体 (CreateCustomerRequest)
```json
{
  "name": "张三",                    // Required: 客户姓名
  "phone": "13812345678",           // Optional: 手机号码 (格式: ^[1-9]\\d{10}$)
  "email": "zhangsan@example.com",  // Optional: 邮箱地址
  "gender": "MALE",                 // Optional: 性别 (MALE/FEMALE)
  "birthday": "1990-01-01",         // Optional: 生日
  "customerType": "MEMBER",         // Required: 客户类型 (WALK_IN/MEMBER)
  "membershipLevel": "BRONZE",      // Optional: 会员等级 (BRONZE/SILVER/GOLD/PLATINUM/DIAMOND)
  "membershipStartDate": "2024-01-01", // Optional: 会员开始日期
  "membershipEndDate": "2024-12-31",   // Optional: 会员结束日期
  "points": 0,                      // Optional: 积分 (默认0, ≥0)
  "status": "ACTIVE",               // Optional: 状态 (ACTIVE/INACTIVE/BLOCKED)
  "notes": "VIP客户"                // Optional: 备注
}
```

#### 响应示例
```json
{
  "uuid": "550e8400-e29b-41d4-a716-446655440000",
  "createdAt": "2024-01-01T10:00:00",
  "customerNumber": 1001,
  "name": "张三",
  "phone": "13812345678",
  "email": "zhangsan@example.com",
  "gender": "MALE",
  "birthday": "1990-01-01",
  "customerType": "MEMBER",
  "membershipLevel": "BRONZE",
  "membershipStartDate": "2024-01-01",
  "membershipEndDate": "2024-12-31",
  "points": 0,
  "totalSpent": 0.0,
  "visitCount": 0,
  "lastVisitDate": null,
  "status": "ACTIVE",
  "notes": "VIP客户"
}
```

### 1.2 获取所有客户 (分页)
**GET** `/api/customers?page=0&size=10&sortBy=createdAt&sortDir=desc`

#### 查询参数
- `page`: 页码 (默认0)
- `size`: 每页大小 (默认10)
- `sortBy`: 排序字段 (默认createdAt)
- `sortDir`: 排序方向 (asc/desc, 默认desc)

#### 响应示例
```json
{
  "content": [
    {
      "uuid": "550e8400-e29b-41d4-a716-446655440000",
      "customerNumber": 1001,
      "name": "张三",
      // ... 其他字段
    }
  ],
  "totalElements": 50,
  "totalPages": 5,
  "number": 0,
  "size": 10,
  "first": true,
  "last": false
}
```

### 1.3 根据UUID获取客户
**GET** `/api/customers/{uuid}`

#### 路径参数
- `uuid`: 客户UUID

#### 响应: CustomerResponse对象

### 1.4 更新客户信息
**PUT** `/api/customers/{uuid}`

#### 请求体 (UpdateCustomerRequest)
```json
{
  "name": "张三三",                  // Optional: 更新后的姓名
  "phone": "13987654321",           // Optional: 更新后的手机号
  "membershipLevel": "SILVER",      // Optional: 更新会员等级
  "points": 100                     // Optional: 更新积分
  // 所有字段都是可选的，只传需要更新的字段
}
```

### 1.5 软删除客户
**DELETE** `/api/customers/{uuid}`

将客户状态设置为INACTIVE

### 1.6 彻底删除客户
**DELETE** `/api/customers/{uuid}/hard`

从数据库中完全删除客户记录

---

## 2. 查询检索操作

### 2.1 根据客户编号获取客户
**GET** `/api/customers/number/{customerNumber}`

#### 示例
```bash
GET /api/customers/number/1001
```

### 2.2 根据电话号码获取客户
**GET** `/api/customers/phone/{phone}`

#### 示例
```bash
GET /api/customers/phone/13812345678
```

### 2.3 根据姓名搜索客户
**GET** `/api/customers/search?name=张`

#### 查询参数
- `name`: 客户姓名关键词

### 2.4 获取所有活跃客户
**GET** `/api/customers/active`

返回状态为ACTIVE的所有客户

### 2.5 获取所有会员客户
**GET** `/api/customers/members`

返回客户类型为MEMBER的所有客户

### 2.6 获取所有散客
**GET** `/api/customers/walk-in`

返回客户类型为WALK_IN的所有客户

### 2.7 根据客户类型获取客户
**GET** `/api/customers/type/{type}`

#### 路径参数
- `type`: 客户类型 (WALK_IN/MEMBER)

### 2.8 根据客户状态获取客户
**GET** `/api/customers/status/{status}`

#### 路径参数
- `status`: 客户状态 (ACTIVE/INACTIVE/BLOCKED)

### 2.9 根据会员等级获取客户
**GET** `/api/customers/membership/{level}`

#### 路径参数
- `level`: 会员等级 (BRONZE/SILVER/GOLD/PLATINUM/DIAMOND)

### 2.10 获取即将到期的会员
**GET** `/api/customers/members/expiring?days=30`

#### 查询参数
- `days`: 天数 (默认30天)

### 2.11 获取今天生日的客户
**GET** `/api/customers/birthday/today`

### 2.12 获取指定月份生日的客户
**GET** `/api/customers/birthday/month/{month}`

#### 路径参数
- `month`: 月份 (1-12)

### 2.13 获取不活跃客户
**GET** `/api/customers/inactive?days=90`

#### 查询参数
- `days`: 天数 (默认90天内无访问记录)

---

## 3. 业务操作

### 3.1 增加客户积分
**POST** `/api/customers/{uuid}/points/add?points=100`

#### 查询参数
- `points`: 要增加的积分数

### 3.2 扣除客户积分
**POST** `/api/customers/{uuid}/points/deduct?points=50`

#### 查询参数
- `points`: 要扣除的积分数

### 3.3 更新客户访问信息
**POST** `/api/customers/{uuid}/visit?spentAmount=299.99`

#### 查询参数
- `spentAmount`: 本次消费金额

此操作会更新客户的:
- 总消费金额 (totalSpent)
- 访问次数 (visitCount)
- 最后访问时间 (lastVisitDate)

---

## 4. 统计和分析

### 4.1 获取客户统计信息
**GET** `/api/customers/stats`

#### 响应示例 (CustomerStatsResponse)
```json
{
  "totalCustomers": 1500,
  "walkInCustomers": 800,
  "memberCustomers": 700,
  "activeCustomers": 1200,
  "inactiveCustomers": 250,
  "blockedCustomers": 50,
  "bronzeMembers": 300,
  "silverMembers": 200,
  "goldMembers": 150,
  "platinumMembers": 40,
  "diamondMembers": 10
}
```

### 4.2 检查客户是否存在
**GET** `/api/customers/{uuid}/exists`

#### 响应
```json
true
```

---

## 错误响应

### 400 Bad Request
```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "客户姓名不能为空",
  "path": "/api/customers"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "客户不存在",
  "path": "/api/customers/550e8400-e29b-41d4-a716-446655440000"
}
```

---

## 使用示例

### 创建新会员客户
```javascript
const response = await fetch('/api/customers', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    name: '李四',
    phone: '13912345678',
    email: 'lisi@example.com',
    customerType: 'MEMBER',
    membershipLevel: 'SILVER',
    membershipStartDate: '2024-01-01',
    membershipEndDate: '2024-12-31',
    points: 500
  })
});
const customer = await response.json();
```

### 搜索客户并更新积分
```javascript
// 1. 搜索客户
const searchResponse = await fetch('/api/customers/search?name=李四');
const customers = await searchResponse.json();

if (customers.length > 0) {
  const customer = customers[0];
  
  // 2. 增加积分
  const pointsResponse = await fetch(
    `/api/customers/${customer.uuid}/points/add?points=100`,
    { method: 'POST' }
  );
  const updatedCustomer = await pointsResponse.json();
  console.log('积分已增加:', updatedCustomer.points);
}
```

### 获取即将到期的会员
```javascript
const response = await fetch('/api/customers/members/expiring?days=7');
const expiringMembers = await response.json();

expiringMembers.forEach(member => {
  console.log(`${member.name} 的会员将在 ${member.membershipEndDate} 到期`);
});
```