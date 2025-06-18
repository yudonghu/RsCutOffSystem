# 客户管理模块 API 文档

## API 索引

| 序号 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 1 | POST | `/api/customers` | 创建客户 |
| 2 | GET | `/api/customers` | 获取所有客户（分页） |
| 3 | GET | `/api/customers/{uuid}` | 根据UUID获取客户 |
| 4 | GET | `/api/customers/number/{customerNumber}` | 根据客户编号获取客户 |
| 5 | GET | `/api/customers/phone/{phone}` | 根据电话号码获取客户 |
| 6 | GET | `/api/customers/search` | 根据姓名搜索客户 |
| 7 | GET | `/api/customers/active` | 获取所有活跃客户 |
| 8 | GET | `/api/customers/members` | 获取所有会员客户 |
| 9 | GET | `/api/customers/walk-in` | 获取所有散客 |
| 10 | GET | `/api/customers/type/{type}` | 根据客户类型获取客户 |
| 11 | GET | `/api/customers/status/{status}` | 根据客户状态获取客户 |
| 12 | GET | `/api/customers/membership/{level}` | 根据会员等级获取客户 |
| 13 | GET | `/api/customers/members/expiring` | 获取即将到期的会员 |
| 14 | GET | `/api/customers/birthday/today` | 获取今天生日的客户 |
| 15 | GET | `/api/customers/birthday/month/{month}` | 获取指定月份生日的客户 |
| 16 | GET | `/api/customers/inactive` | 获取不活跃客户 |
| 17 | PUT | `/api/customers/{uuid}` | 更新客户信息 |
| 18 | DELETE | `/api/customers/{uuid}` | 删除客户（软删除） |
| 19 | DELETE | `/api/customers/{uuid}/hard` | 彻底删除客户 |
| 20 | POST | `/api/customers/{uuid}/points/add` | 增加客户积分 |
| 21 | POST | `/api/customers/{uuid}/points/deduct` | 扣除客户积分 |
| 22 | POST | `/api/customers/{uuid}/visit` | 更新客户访问信息 |
| 23 | GET | `/api/customers/stats` | 获取客户统计信息 |
| 24 | GET | `/api/customers/{uuid}/exists` | 检查客户是否存在 |

## 概述

客户管理模块提供按摩店完整的客户关系管理功能，包括客户基本信息管理、会员制度、积分系统、生日提醒、客户活跃度分析等。支持散客和会员两种客户类型，提供多层级会员等级管理。

**基础路径**: `/api/customers`

---

## 数据模型

### 客户实体 (Customer)

| 字段 | 类型 | 必填 | 描述 |
|------|------|------|------|
| uuid | UUID | 是 | 客户唯一标识符 |
| customerNumber | Long | 是 | 客户编号（自动生成，唯一） |
| name | String | 是 | 客户姓名 |
| phone | String | 否 | 电话号码（唯一） |
| email | String | 否 | 邮箱地址（唯一） |
| gender | Enum | 否 | 性别：MALE/FEMALE/OTHER |
| birthday | LocalDate | 否 | 生日 |
| customerType | Enum | 是 | 客户类型：WALK_IN/MEMBER |
| membershipLevel | Enum | 否 | 会员等级：BRONZE/SILVER/GOLD/PLATINUM/DIAMOND |
| membershipStartDate | LocalDate | 否 | 会员开始日期 |
| membershipEndDate | LocalDate | 否 | 会员结束日期 |
| points | Integer | 是 | 积分（默认0） |
| totalSpent | Double | 是 | 累计消费金额（默认0.0） |
| visitCount | Integer | 是 | 访问次数（默认0） |
| lastVisitDate | LocalDateTime | 否 | 最后访问时间 |
| status | Enum | 是 | 客户状态：ACTIVE/INACTIVE/BLOCKED |
| notes | String | 否 | 备注信息 |
| createdAt | LocalDateTime | 是 | 创建时间（自动生成） |

### 枚举说明

#### 性别 (Gender)
- `MALE` - 男性
- `FEMALE` - 女性
- `OTHER` - 其他

#### 客户类型 (CustomerType)
- `WALK_IN` - 散客/非会员
- `MEMBER` - 会员

#### 会员等级 (MembershipLevel)
- `BRONZE` - 铜卡
- `SILVER` - 银卡
- `GOLD` - 金卡
- `PLATINUM` - 白金卡
- `DIAMOND` - 钻石卡

#### 客户状态 (CustomerStatus)
- `ACTIVE` - 活跃
- `INACTIVE` - 不活跃
- `BLOCKED` - 黑名单

---

## API 接口

### 1. 创建客户

**POST** `/api/customers`

创建新的客户记录。

#### 请求体

```json
{
  "name": "张三",
  "phone": "1234567890",
  "email": "zhangsan@example.com",
  "gender": "MALE",
  "birthday": "1990-05-15",
  "customerType": "MEMBER",
  "membershipLevel": "GOLD",
  "membershipStartDate": "2024-06-18",
  "membershipEndDate": "2025-06-18",
  "points": 100,
  "status": "ACTIVE",
  "notes": "VIP客户，服务态度要特别好"
}
```

#### 请求字段说明

| 字段 | 类型 | 必填 | 验证规则 | 描述 |
|------|------|------|----------|------|
| name | String | 是 | 不能为空 | 客户姓名 |
| phone | String | 否 | 10位数字，唯一 | 电话号码 |
| email | String | 否 | 邮箱格式，唯一 | 邮箱地址 |
| gender | String | 否 | 枚举值 | 性别 |
| birthday | String | 否 | 日期格式 YYYY-MM-DD | 生日 |
| customerType | String | 是 | 枚举值 | 客户类型 |
| membershipLevel | String | 否 | 会员时必填 | 会员等级 |
| membershipStartDate | String | 否 | 会员时必填 | 会员开始日期 |
| membershipEndDate | String | 否 | 会员时必填 | 会员结束日期 |
| points | Integer | 否 | 非负数，默认0 | 初始积分 |
| status | String | 否 | 默认ACTIVE | 客户状态 |
| notes | String | 否 | - | 备注信息 |

#### 响应示例

**成功 (201 Created)**
```json
{
  "uuid": "550e8400-e29b-41d4-a716-446655440000",
  "createdAt": "2024-06-18T14:30:00",
  "customerNumber": 100001,
  "name": "张三",
  "phone": "1234567890",
  "email": "zhangsan@example.com",
  "gender": "MALE",
  "birthday": "1990-05-15",
  "customerType": "MEMBER",
  "membershipLevel": "GOLD",
  "membershipStartDate": "2024-06-18",
  "membershipEndDate": "2025-06-18",
  "points": 100,
  "totalSpent": 0.0,
  "visitCount": 0,
  "lastVisitDate": null,
  "status": "ACTIVE",
  "notes": "VIP客户，服务态度要特别好"
}
```

**验证失败 (400 Bad Request)**
```json
{
  "message": "电话号码已被使用",
  "status": 400
}
```

---

### 2. 获取所有客户（分页）

**GET** `/api/customers`

分页获取所有客户列表。

#### 查询参数

| 参数 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| page | int | 0 | 页码（从0开始） |
| size | int | 10 | 每页数量 |
| sortBy | String | createdAt | 排序字段 |
| sortDir | String | desc | 排序方向（asc/desc） |

#### 请求示例

```
GET /api/customers?page=0&size=20&sortBy=customerNumber&sortDir=asc
```

#### 响应示例

```json
{
  "content": [
    {
      "uuid": "550e8400-e29b-41d4-a716-446655440000",
      "customerNumber": 100001,
      "name": "张三",
      "phone": "1234567890",
      "customerType": "MEMBER",
      "membershipLevel": "GOLD",
      "status": "ACTIVE",
      "totalSpent": 1500.0,
      "visitCount": 8,
      "lastVisitDate": "2024-06-15T16:30:00"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false
    },
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

---

### 3. 根据UUID获取客户

**GET** `/api/customers/{uuid}`

根据客户UUID获取客户详细信息。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 客户唯一标识符 |

#### 响应示例

**成功 (200 OK)**

返回完整的客户信息，格式参考"创建客户"的响应。

**客户不存在 (404 Not Found)**
```json
{
  "message": "客户未找到: 550e8400-e29b-41d4-a716-446655440000",
  "status": 404
}
```

---

### 4. 根据客户编号获取客户

**GET** `/api/customers/number/{customerNumber}`

根据客户编号获取客户信息。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| customerNumber | Long | 客户编号 |

#### 请求示例

```
GET /api/customers/number/100001
```

---

### 5. 根据电话号码获取客户

**GET** `/api/customers/phone/{phone}`

根据电话号码获取客户信息。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| phone | String | 电话号码 |

#### 请求示例

```
GET /api/customers/phone/1234567890
```

#### 响应示例

**找到客户 (200 OK)**

返回客户信息。

**未找到客户 (404 Not Found)**

无响应体。

---

### 6. 根据姓名搜索客户

**GET** `/api/customers/search`

根据姓名关键词搜索客户（模糊匹配，不区分大小写）。

#### 查询参数

| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| name | String | 是 | 姓名关键词 |

#### 请求示例

```
GET /api/customers/search?name=张
```

---

### 7. 获取所有活跃客户

**GET** `/api/customers/active`

获取所有状态为ACTIVE的客户。

---

### 8. 获取所有会员客户

**GET** `/api/customers/members`

获取所有类型为MEMBER的客户。

---

### 9. 获取所有散客

**GET** `/api/customers/walk-in`

获取所有类型为WALK_IN的客户。

---

### 10. 根据客户类型获取客户

**GET** `/api/customers/type/{type}`

根据客户类型获取客户列表。

#### 路径参数

| 参数 | 类型 | 可选值 | 描述 |
|------|------|--------|------|
| type | String | WALK_IN, MEMBER | 客户类型 |

---

### 11. 根据客户状态获取客户

**GET** `/api/customers/status/{status}`

根据客户状态获取客户列表。

#### 路径参数

| 参数 | 类型 | 可选值 | 描述 |
|------|------|--------|------|
| status | String | ACTIVE, INACTIVE, BLOCKED | 客户状态 |

---

### 12. 根据会员等级获取客户

**GET** `/api/customers/membership/{level}`

根据会员等级获取客户列表。

#### 路径参数

| 参数 | 类型 | 可选值 | 描述 |
|------|------|--------|------|
| level | String | BRONZE, SILVER, GOLD, PLATINUM, DIAMOND | 会员等级 |

---

### 13. 获取即将到期的会员

**GET** `/api/customers/members/expiring`

获取会员即将到期的客户列表。

#### 查询参数

| 参数 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| days | int | 30 | 提前天数 |

#### 请求示例

```
GET /api/customers/members/expiring?days=15
```

---

### 14. 获取今天生日的客户

**GET** `/api/customers/birthday/today`

获取今天生日的所有客户。

#### 响应示例

```json
[
  {
    "uuid": "550e8400-e29b-41d4-a716-446655440000",
    "customerNumber": 100001,
    "name": "张三",
    "birthday": "1990-06-18",
    "phone": "1234567890",
    "customerType": "MEMBER",
    "membershipLevel": "GOLD"
  }
]
```

---

### 15. 获取指定月份生日的客户

**GET** `/api/customers/birthday/month/{month}`

获取指定月份生日的所有客户。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| month | int | 月份（1-12） |

#### 请求示例

```
GET /api/customers/birthday/month/6
```

---

### 16. 获取不活跃客户

**GET** `/api/customers/inactive`

获取指定天数内未访问的客户（不活跃客户）。

#### 查询参数

| 参数 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| days | int | 90 | 天数阈值 |

#### 请求示例

```
GET /api/customers/inactive?days=60
```

---

### 17. 更新客户信息

**PUT** `/api/customers/{uuid}`

更新指定客户的信息。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 客户唯一标识符 |

#### 请求体

```json
{
  "name": "张三丰",
  "phone": "1234567891",
  "email": "zhangsanfeng@example.com",
  "membershipLevel": "PLATINUM",
  "membershipEndDate": "2025-12-31",
  "notes": "升级为白金会员"
}
```

#### 注意事项

- 所有字段都是可选的，只更新提供的字段
- 更新电话和邮箱时会检查唯一性
- 会员信息更新需要符合业务规则

---

### 18. 删除客户（软删除）

**DELETE** `/api/customers/{uuid}`

将客户状态设置为INACTIVE，实现软删除。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 客户唯一标识符 |

#### 响应示例

**成功 (204 No Content)**

无响应体。

---

### 19. 彻底删除客户

**DELETE** `/api/customers/{uuid}/hard`

从数据库中彻底删除客户记录。

#### 注意事项

- 此操作不可恢复
- 建议先确认该客户没有关联的交易记录

---

### 20. 增加客户积分

**POST** `/api/customers/{uuid}/points/add`

为会员客户增加积分。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 客户唯一标识符 |

#### 查询参数

| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| points | int | 是 | 增加的积分数量 |

#### 请求示例

```
POST /api/customers/{uuid}/points/add?points=50
```

#### 响应示例

**成功 (200 OK)**

返回更新后的客户信息。

**业务逻辑错误 (400 Bad Request)**
```json
{
  "message": "只有会员客户才能累积积分",
  "status": 400
}
```

---

### 21. 扣除客户积分

**POST** `/api/customers/{uuid}/points/deduct`

扣除会员客户的积分。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 客户唯一标识符 |

#### 查询参数

| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| points | int | 是 | 扣除的积分数量 |

#### 请求示例

```
POST /api/customers/{uuid}/points/deduct?points=30
```

#### 注意事项

- 只有会员客户可以使用积分
- 积分不足时会返回错误

---

### 22. 更新客户访问信息

**POST** `/api/customers/{uuid}/visit`

更新客户的访问记录，包括访问次数、最后访问时间、累计消费和积分。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 客户唯一标识符 |

#### 查询参数

| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| spentAmount | double | 是 | 本次消费金额 |

#### 请求示例

```
POST /api/customers/{uuid}/visit?spentAmount=150.50
```

#### 业务逻辑

1. 访问次数 +1
2. 更新最后访问时间为当前时间
3. 累计消费金额增加
4. 会员客户按1元=1积分的比例增加积分

#### 响应示例

**成功 (200 OK)**

返回更新后的客户信息。

---

### 23. 获取客户统计信息

**GET** `/api/customers/stats`

获取客户管理系统的统计信息。

#### 响应示例

```json
{
  "totalCustomers": 2580,
  "walkInCustomers": 1680,
  "memberCustomers": 900,
  "activeCustomers": 2350,
  "inactiveCustomers": 200,
  "blockedCustomers": 30,
  "bronzeMembers": 450,
  "silverMembers": 280,
  "goldMembers": 120,
  "platinumMembers": 40,
  "diamondMembers": 10
}
```

#### 响应字段说明

| 字段 | 类型 | 描述 |
|------|------|------|
| totalCustomers | Long | 总客户数量 |
| walkInCustomers | Long | 散客数量 |
| memberCustomers | Long | 会员数量 |
| activeCustomers | Long | 活跃客户数量 |
| inactiveCustomers | Long | 不活跃客户数量 |
| blockedCustomers | Long | 黑名单客户数量 |
| *Members | Long | 各等级会员数量 |

---

### 24. 检查客户是否存在

**GET** `/api/customers/{uuid}/exists`

检查指定UUID的客户是否存在。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 客户唯一标识符 |

#### 响应示例

```json
true
```

---

## 业务流程说明

### 客户类型管理

1. **散客 (WALK_IN)** - 临时客户，不享受会员权益
2. **会员 (MEMBER)** - 注册会员，享受积分、等级优惠等权益

### 会员等级体系

- **铜卡 (BRONZE)** - 基础会员
- **银卡 (SILVER)** - 进阶会员
- **金卡 (GOLD)** - 高级会员
- **白金卡 (PLATINUM)** - 白金会员
- **钻石卡 (DIAMOND)** - 最高等级会员

### 积分系统

- 只有会员客户可以累积和使用积分
- 消费时按1元=1积分的比例自动累积
- 支持手动增加和扣除积分
- 积分可用于兑换服务或产品

### 客户状态管理

- **活跃 (ACTIVE)** - 正常客户，可以享受所有服务
- **不活跃 (INACTIVE)** - 暂停服务的客户
- **黑名单 (BLOCKED)** - 禁止服务的客户

### 会员到期管理

- 支持设置会员有效期
- 提供即将到期会员查询功能
- 可以提前提醒续费

---

## 错误码说明

| HTTP状态码 | 错误类型 | 描述 |
|------------|----------|------|
| 400 | Bad Request | 请求参数验证失败或业务逻辑错误 |
| 404 | Not Found | 请求的资源不存在 |
| 500 | Internal Server Error | 服务器内部错误 |

## 常见错误信息

### 创建客户相关
- `客户姓名不能为空` - 未提供客户姓名
- `电话号码已被使用` - 电话号码重复
- `邮箱已被使用` - 邮箱地址重复
- `请输入有效的手机号码` - 电话号码格式错误
- `请输入有效的邮箱地址` - 邮箱格式错误
- `会员等级不能为空` - 会员客户未设置等级
- `会员结束日期不能早于开始日期` - 会员日期范围错误

### 积分操作相关
- `只有会员客户才能累积积分` - 散客无法使用积分功能
- `只有会员客户才能使用积分` - 散客无法扣除积分
- `积分不足` - 积分余额不够扣除

### 查询客户相关
- `客户未找到: {uuid}` - 指定的客户不存在
- `客户未找到: {customerNumber}` - 指定的客户编号不存在

## 注意事项

### 1. 数据唯一性
- 客户编号自动生成，保证全局唯一
- 电话号码和邮箱如果提供，必须保证唯一性
- 支持客户信息的重复性检查

### 2. 会员管理
- 会员必须设置等级和有效期
- 会员到期后仍保留积分和历史记录
- 支持会员等级升级和降级

### 3. 积分系统
- 积分只适用于会员客户
- 消费自动累积积分（1元=1积分）
- 积分不会过期，但可以手动调整

### 4. 隐私保护
- 客户敏感信息需要严格的访问控制
- 支持数据脱敏和匿名化处理
- 遵循数据保护法规要求

### 5. 数据分析
- 提供丰富的客户统计功能
- 支持客户活跃度分析
- 生日提醒和会员到期提醒

### 6. 性能优化
- 分页查询支持大量客户数据
- 建立适当的数据库索引
- 客户编号和联系方式查询已优化

### 7. 业务扩展
- 支持自定义客户字段
- 可扩展的会员等级系统
- 灵活的积分规则配置

这个客户管理系统为按摩店提供了完整的客户关系管理解决方案，支持从散客到高级会员的全生命周期管理，帮助提升客户满意度和忠诚度。