# 交易管理模块 API 文档

## API 索引

| 序号 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 1 | POST | `/api/transactions` | 创建交易 |
| 2 | GET | `/api/transactions` | 获取所有交易（分页） |
| 3 | GET | `/api/transactions/{uuid}` | 根据UUID获取交易 |
| 4 | GET | `/api/transactions/number/{transactionNumber}` | 根据交易编号获取交易 |
| 5 | GET | `/api/transactions/type/{type}` | 根据交易类型获取交易 |
| 6 | GET | `/api/transactions/status/{status}` | 根据交易状态获取交易 |
| 7 | GET | `/api/transactions/date/{date}` | 根据交易日期获取交易 |
| 8 | GET | `/api/transactions/date-range` | 根据日期范围获取交易 |
| 9 | GET | `/api/transactions/income` | 获取收入交易 |
| 10 | GET | `/api/transactions/expense` | 获取支出交易 |
| 11 | GET | `/api/transactions/pending` | 获取待确认的交易 |
| 12 | GET | `/api/transactions/unprocessed` | 获取未处理的交易 |
| 13 | GET | `/api/transactions/customer/{customerUuid}` | 根据客户获取交易历史 |
| 14 | GET | `/api/transactions/user/{userUuid}` | 根据员工获取交易历史 |
| 15 | PUT | `/api/transactions/{uuid}` | 更新交易 |
| 16 | POST | `/api/transactions/{uuid}/confirm` | 确认交易 |
| 17 | POST | `/api/transactions/{uuid}/cancel` | 取消交易 |
| 18 | DELETE | `/api/transactions/{uuid}` | 删除交易 |
| 19 | POST | `/api/transactions/daily-cutoff` | 执行日结 |
| 20 | GET | `/api/transactions/daily-cutoff/{cutOffDate}` | 获取日结报告 |
| 21 | GET | `/api/transactions/daily-cutoff/today` | 获取今日日结报告 |
| 22 | GET | `/api/transactions/stats` | 获取交易统计 |
| 23 | GET | `/api/transactions/stats/today` | 获取今日交易统计 |
| 24 | GET | `/api/transactions/stats/this-week` | 获取本周交易统计 |
| 25 | GET | `/api/transactions/stats/this-month` | 获取本月交易统计 |
| 26 | GET | `/api/transactions/{uuid}/exists` | 检查交易是否存在 |
| 27 | GET | `/api/transactions/users/history` | 根据多个员工获取交易历史 |
| 28 | GET | `/api/transactions/users/{userUuid}/date/{date}` | 获取指定员工指定日期交易 |
| 29 | GET | `/api/transactions/{transactionUuid}/users/{userUuid}/exists` | 检查员工是否参与交易 |
| 30 | POST | `/api/transactions/{transactionUuid}/users/{userUuid}` | 向交易添加员工 |
| 31 | DELETE | `/api/transactions/{transactionUuid}/users/{userUuid}` | 从交易中移除员工 |
| 32 | GET | `/api/transactions/stats/count-by-user` | 按员工统计交易数量 |
| 33 | GET | `/api/transactions/stats/amount-by-user` | 按员工统计交易金额 |

## 概述

交易管理模块提供完整的按摩店财务交易管理功能，包括收入支出记录、多员工协作、日结统计、数据分析等功能。支持现金、刷卡、礼品卡等多种支付方式。

**基础路径**: `/api/transactions`

---

## 数据模型

### 交易实体 (Transaction)

| 字段 | 类型 | 必填 | 描述 |
|------|------|------|------|
| uuid | UUID | 是 | 交易唯一标识符 |
| transactionNumber | String | 是 | 交易编号（自动生成，格式：TXN{日期}{序号}） |
| transactionDate | LocalDate | 是 | 交易日期 |
| type | Enum | 是 | 交易类型：INCOME/EXPENSE |
| totalAmount | BigDecimal | 是 | 交易总金额 |
| description | String | 否 | 交易描述 |
| notes | String | 否 | 备注 |
| relatedCustomerUuid | UUID | 否 | 关联客户UUID |
| relatedUserUuids | Set<UUID> | 是 | 关联员工UUID集合 |
| paymentMethod | Enum | 是 | 支付方式：CASH/CARD/GIFT_CARD/BANK_TRANSFER/OTHER |
| paymentReference | String | 否 | 支付参考号 |
| status | Enum | 是 | 交易状态：PENDING/CONFIRMED/CANCELLED |
| isIncludedInCutOff | Boolean | 是 | 是否已包含在日结中 |
| cutOffDate | LocalDate | 否 | 日结日期 |
| createdAt | LocalDateTime | 是 | 创建时间（自动生成） |

### 交易明细实体 (TransactionItem)

| 字段 | 类型 | 必填 | 描述 |
|------|------|------|------|
| uuid | UUID | 是 | 明细唯一标识符 |
| category | Enum | 是 | 交易分类（详见枚举说明） |
| amount | BigDecimal | 是 | 明细金额 |
| description | String | 否 | 明细描述 |
| notes | String | 否 | 明细备注 |
| categoryDisplayName | String | 是 | 分类显示名称 |

### 枚举说明

#### 交易分类 (TransactionCategory)

**收入类别：**
- `SERVICE_MASSAGE` - 按摩服务收入
- `SERVICE_TIP` - 小费收入
- `SERVICE_OTHER` - 其他服务收入
- `PRODUCT_SALE` - 产品销售收入
- `MEMBERSHIP_FEE` - 会员费收入
- `DEPOSIT` - 押金收入
- `OTHER_INCOME` - 其他收入

**支出类别：**
- `SALARY_MASSAGE_THERAPIST` - 按摩师工资
- `SALARY_STAFF` - 其他员工工资
- `RENT` - 房租
- `UTILITIES` - 水电费
- `SUPPLIES` - 耗材采购
- `EQUIPMENT` - 设备采购
- `MARKETING` - 营销费用
- `TAX` - 税费
- `OTHER_EXPENSE` - 其他支出

---

## API 接口

### 1. 创建交易

**POST** `/api/transactions`

创建新的交易记录。

#### 请求体

```json
{
  "type": "INCOME",
  "transactionDate": "2024-06-18",
  "description": "按摩服务收入",
  "notes": "客户满意，给了小费",
  "relatedCustomerUuid": "550e8400-e29b-41d4-a716-446655440000",
  "relatedUserUuids": [
    "660e8400-e29b-41d4-a716-446655440001",
    "770e8400-e29b-41d4-a716-446655440002"
  ],
  "paymentMethod": "CASH",
  "paymentReference": "CASH001",
  "items": [
    {
      "category": "SERVICE_MASSAGE",
      "amount": 200.00,
      "description": "60分钟全身按摩",
      "notes": "使用精油"
    },
    {
      "category": "SERVICE_TIP",
      "amount": 50.00,
      "description": "客户小费",
      "notes": ""
    }
  ]
}
```

#### 请求字段说明

| 字段 | 类型 | 必填 | 验证规则 | 描述 |
|------|------|------|----------|------|
| type | String | 是 | INCOME/EXPENSE | 交易类型 |
| transactionDate | String | 是 | 日期格式 YYYY-MM-DD | 交易日期 |
| description | String | 否 | - | 交易描述 |
| notes | String | 否 | - | 备注信息 |
| relatedCustomerUuid | String | 否 | UUID格式 | 关联客户 |
| relatedUserUuids | Array | 是 | 不能为空 | 关联员工列表 |
| paymentMethod | String | 是 | 支付方式枚举 | 支付方式 |
| paymentReference | String | 否 | - | 支付参考号 |
| items | Array | 是 | 不能为空 | 交易明细列表 |

#### 交易明细字段说明

| 字段 | 类型 | 必填 | 验证规则 | 描述 |
|------|------|------|----------|------|
| category | String | 是 | 交易分类枚举 | 交易分类 |
| amount | Number | 是 | 必须大于0.01 | 明细金额 |
| description | String | 否 | - | 明细描述 |
| notes | String | 否 | - | 明细备注 |

#### 响应示例

```json
{
  "660e8400-e29b-41d4-a716-446655440001": 3500.00,
  "770e8400-e29b-41d4-a716-446655440002": 2800.00,
  "880e8400-e29b-41d4-a716-446655440003": 1200.00
}
```

---

## 业务流程说明

### 交易生命周期

1. **创建交易** - 交易初始状态为 `PENDING`（待确认）
2. **确认交易** - 将状态更改为 `CONFIRMED`（已确认）
3. **日结处理** - 确认的交易可以被包含在日结中
4. **取消交易** - 可以将交易状态更改为 `CANCELLED`（已取消）

### 交易编号规则

交易编号格式：`TXN{YYYYMMDD}{序号}`
- `TXN` - 固定前缀
- `YYYYMMDD` - 8位日期
- `序号` - 4位递增序号，每日从0001开始

**示例：** `TXN202406180001`（2024年6月18日第1笔交易）

### 多员工协作

- 每个交易可以关联多个员工
- 支持动态添加和移除员工
- 统计功能可以按员工维度分析业绩

### 日结机制

- 只有状态为 `CONFIRMED` 的交易才能参与日结
- 已日结的交易不能再被修改或取消
- 日结报告包含详细的分类统计和支付方式汇总

---

## 错误码说明

| HTTP状态码 | 错误类型 | 描述 |
|------------|----------|------|
| 400 | Bad Request | 请求参数验证失败或业务逻辑错误 |
| 404 | Not Found | 请求的资源不存在 |
| 500 | Internal Server Error | 服务器内部错误 |

## 常见错误信息

### 创建交易相关
- `操作员工不能为空` - 创建交易时未提供关联员工
- `交易明细不能为空` - 创建交易时未提供交易明细
- `交易金额必须大于0` - 交易明细金额不能为负数或零
- `交易分类与交易类型不匹配` - 收入交易使用了支出分类或反之

### 更新交易相关
- `已取消或已包含在日结中的交易不能修改` - 尝试修改不可变的交易
- `只有待确认的交易才能被确认` - 尝试确认非待确认状态的交易
- `已包含在日结中的交易不能取消` - 尝试取消已日结的交易
- `交易至少需要关联一个员工` - 尝试移除最后一个关联员工

### 日结相关
- `没有找到需要日结的交易` - 指定日期没有可日结的交易
- `交易未找到: {uuid}` - 指定的交易不存在
- `交易未找到: {transactionNumber}` - 指定的交易编号不存在

## 注意事项

### 1. 交易状态管理
- **PENDING** - 新创建的交易，可以修改和删除
- **CONFIRMED** - 已确认的交易，可以参与日结，修改受限
- **CANCELLED** - 已取消的交易，不能修改，不参与统计

### 2. 金额计算
- 交易总金额由交易明细自动计算得出
- 支持精确的小数计算（precision=10, scale=2）
- 所有金额统计都基于已确认的交易

### 3. 日期处理
- 所有日期使用 ISO 8601 格式 (YYYY-MM-DD)
- 统计功能支持跨日期范围查询
- 日结以交易日期为准，不是创建时间

### 4. 数据完整性
- 交易编号自动生成，保证唯一性
- 支持事务处理，确保数据一致性
- 删除交易会级联删除相关的交易明细

### 5. 性能考虑
- 分页查询默认每页10条记录
- 大量数据查询建议使用日期范围过滤
- 统计查询已优化，支持高并发访问

### 6. 安全性
- 所有接口支持跨域访问
- 敏感操作（删除、日结）需要额外权限验证
- 支持操作日志记录和审计

### 7. 扩展性
- 支付方式可以轻松扩展
- 交易分类支持业务需求变更
- 统计维度可以根据需要增加

这个交易管理系统为按摩店提供了完整的财务管理解决方案，支持日常运营、员工管理、财务统计等核心业务需求。

**成功 (201 Created)**
```json
{
  "uuid": "550e8400-e29b-41d4-a716-446655440000",
  "transactionNumber": "TXN202406180001",
  "createdAt": "2024-06-18T14:30:00",
  "transactionDate": "2024-06-18",
  "type": "INCOME",
  "totalAmount": 250.00,
  "description": "按摩服务收入",
  "notes": "客户满意，给了小费",
  "relatedCustomerUuid": "550e8400-e29b-41d4-a716-446655440000",
  "relatedUserUuids": [
    "660e8400-e29b-41d4-a716-446655440001",
    "770e8400-e29b-41d4-a716-446655440002"
  ],
  "paymentMethod": "CASH",
  "paymentReference": "CASH001",
  "status": "PENDING",
  "isIncludedInCutOff": false,
  "cutOffDate": null,
  "items": [
    {
      "uuid": "880e8400-e29b-41d4-a716-446655440003",
      "category": "SERVICE_MASSAGE",
      "amount": 200.00,
      "description": "60分钟全身按摩",
      "notes": "使用精油",
      "categoryDisplayName": "按摩服务"
    },
    {
      "uuid": "990e8400-e29b-41d4-a716-446655440004",
      "category": "SERVICE_TIP",
      "amount": 50.00,
      "description": "客户小费",
      "notes": "",
      "categoryDisplayName": "小费"
    }
  ]
}
```

**验证失败 (400 Bad Request)**
```json
{
  "message": "操作员工不能为空",
  "status": 400
}
```

---

### 2. 获取所有交易（分页）

**GET** `/api/transactions`

分页获取所有交易列表。

#### 查询参数

| 参数 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| page | int | 0 | 页码（从0开始） |
| size | int | 10 | 每页数量 |
| sortBy | String | transactionDate | 排序字段 |
| sortDir | String | desc | 排序方向（asc/desc） |

#### 请求示例

```
GET /api/transactions?page=0&size=20&sortBy=totalAmount&sortDir=desc
```

#### 响应示例

```json
{
  "content": [
    {
      "uuid": "550e8400-e29b-41d4-a716-446655440000",
      "transactionNumber": "TXN202406180001",
      "createdAt": "2024-06-18T14:30:00",
      "transactionDate": "2024-06-18",
      "type": "INCOME",
      "totalAmount": 250.00,
      "description": "按摩服务收入",
      "status": "CONFIRMED",
      "paymentMethod": "CASH",
      "isIncludedInCutOff": false
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

### 3. 根据UUID获取交易

**GET** `/api/transactions/{uuid}`

根据交易UUID获取交易详细信息。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 交易唯一标识符 |

#### 响应示例

**成功 (200 OK)**

返回完整的交易信息，格式参考"创建交易"的响应。

**交易不存在 (404 Not Found)**
```json
{
  "message": "交易未找到: 550e8400-e29b-41d4-a716-446655440000",
  "status": 404
}
```

---

### 4. 根据交易编号获取交易

**GET** `/api/transactions/number/{transactionNumber}`

根据交易编号获取交易信息。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| transactionNumber | String | 交易编号 |

#### 请求示例

```
GET /api/transactions/number/TXN202406180001
```

---

### 5. 根据交易类型获取交易

**GET** `/api/transactions/type/{type}`

根据交易类型获取交易列表。

#### 路径参数

| 参数 | 类型 | 可选值 | 描述 |
|------|------|--------|------|
| type | String | INCOME, EXPENSE | 交易类型 |

#### 请求示例

```
GET /api/transactions/type/INCOME
```

---

### 6. 根据交易状态获取交易

**GET** `/api/transactions/status/{status}`

根据交易状态获取交易列表。

#### 路径参数

| 参数 | 类型 | 可选值 | 描述 |
|------|------|--------|------|
| status | String | PENDING, CONFIRMED, CANCELLED | 交易状态 |

---

### 7. 根据交易日期获取交易

**GET** `/api/transactions/date/{date}`

根据指定日期获取交易列表。

#### 路径参数

| 参数 | 类型 | 格式 | 描述 |
|------|------|------|------|
| date | String | YYYY-MM-DD | 交易日期 |

#### 请求示例

```
GET /api/transactions/date/2024-06-18
```

---

### 8. 根据日期范围获取交易

**GET** `/api/transactions/date-range`

根据日期范围获取交易列表。

#### 查询参数

| 参数 | 类型 | 必填 | 格式 | 描述 |
|------|------|------|------|------|
| startDate | String | 是 | YYYY-MM-DD | 开始日期 |
| endDate | String | 是 | YYYY-MM-DD | 结束日期 |

#### 请求示例

```
GET /api/transactions/date-range?startDate=2024-06-01&endDate=2024-06-30
```

---

### 9. 获取收入交易

**GET** `/api/transactions/income`

获取所有收入类型的交易。

---

### 10. 获取支出交易

**GET** `/api/transactions/expense`

获取所有支出类型的交易。

---

### 11. 获取待确认的交易

**GET** `/api/transactions/pending`

获取所有待确认状态的交易。

---

### 12. 获取未处理的交易

**GET** `/api/transactions/unprocessed`

获取所有已确认但未包含在日结中的交易。

---

### 13. 根据客户获取交易历史

**GET** `/api/transactions/customer/{customerUuid}`

获取指定客户的所有交易记录。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| customerUuid | UUID | 客户唯一标识符 |

---

### 14. 根据员工获取交易历史

**GET** `/api/transactions/user/{userUuid}`

获取指定员工参与的所有交易记录。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| userUuid | UUID | 员工唯一标识符 |

---

### 15. 更新交易

**PUT** `/api/transactions/{uuid}`

更新指定交易的信息。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 交易唯一标识符 |

#### 请求体

```json
{
  "transactionDate": "2024-06-19",
  "description": "更新后的描述",
  "notes": "更新备注",
  "relatedCustomerUuid": "550e8400-e29b-41d4-a716-446655440000",
  "relatedUserUuids": [
    "660e8400-e29b-41d4-a716-446655440001"
  ],
  "paymentMethod": "CARD",
  "paymentReference": "CARD001",
  "status": "CONFIRMED",
  "items": [
    {
      "category": "SERVICE_MASSAGE",
      "amount": 300.00,
      "description": "90分钟深层按摩",
      "notes": "使用热石"
    }
  ]
}
```

#### 注意事项

- 所有字段都是可选的，只更新提供的字段
- 已取消或已包含在日结中的交易不能修改
- 更新交易明细时会重新计算总金额

---

### 16. 确认交易

**POST** `/api/transactions/{uuid}/confirm`

确认指定的待确认交易。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 交易唯一标识符 |

#### 响应示例

**成功 (200 OK)**

返回确认后的交易信息。

**业务逻辑错误 (400 Bad Request)**
```json
{
  "message": "只有待确认的交易才能被确认",
  "status": 400
}
```

---

### 17. 取消交易

**POST** `/api/transactions/{uuid}/cancel`

取消指定的交易。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 交易唯一标识符 |

#### 注意事项

- 已包含在日结中的交易不能取消

---

### 18. 删除交易

**DELETE** `/api/transactions/{uuid}`

彻底删除指定的交易。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 交易唯一标识符 |

#### 响应示例

**成功 (204 No Content)**

无响应体。

---

### 19. 执行日结

**POST** `/api/transactions/daily-cutoff`

执行指定日期的日结操作。

#### 查询参数

| 参数 | 类型 | 必填 | 格式 | 描述 |
|------|------|------|------|------|
| cutOffDate | String | 是 | YYYY-MM-DD | 日结日期 |

#### 请求示例

```
POST /api/transactions/daily-cutoff?cutOffDate=2024-06-18
```

#### 响应示例

**成功 (200 OK)**
```json
{
  "cutOffDate": "2024-06-18",
  "totalIncome": 1500.00,
  "totalExpense": 800.00,
  "netProfit": 700.00,
  "transactionCount": 8,
  "paymentMethodSummaries": [
    {
      "paymentMethod": "CASH",
      "amount": 800.00,
      "count": 5
    },
    {
      "paymentMethod": "CARD",
      "amount": 700.00,
      "count": 3
    }
  ],
  "incomeCategorySummaries": [
    {
      "category": "SERVICE_MASSAGE",
      "categoryDisplayName": "按摩服务",
      "amount": 1200.00,
      "count": 6
    },
    {
      "category": "SERVICE_TIP",
      "categoryDisplayName": "小费",
      "amount": 300.00,
      "count": 4
    }
  ],
  "expenseCategorySummaries": [
    {
      "category": "SUPPLIES",
      "categoryDisplayName": "耗材采购",
      "amount": 500.00,
      "count": 2
    },
    {
      "category": "UTILITIES",
      "categoryDisplayName": "水电费",
      "amount": 300.00,
      "count": 1
    }
  ]
}
```

---

### 20. 获取日结报告

**GET** `/api/transactions/daily-cutoff/{cutOffDate}`

获取指定日期的日结报告。

#### 路径参数

| 参数 | 类型 | 格式 | 描述 |
|------|------|------|------|
| cutOffDate | String | YYYY-MM-DD | 日结日期 |

#### 响应示例

格式参考"执行日结"的响应。

---

### 21. 获取今日日结报告

**GET** `/api/transactions/daily-cutoff/today`

获取当天的日结报告。

---

### 22. 获取交易统计

**GET** `/api/transactions/stats`

获取指定日期范围的交易统计信息。

#### 查询参数

| 参数 | 类型 | 必填 | 格式 | 描述 |
|------|------|------|------|------|
| startDate | String | 是 | YYYY-MM-DD | 开始日期 |
| endDate | String | 是 | YYYY-MM-DD | 结束日期 |

#### 请求示例

```
GET /api/transactions/stats?startDate=2024-06-01&endDate=2024-06-30
```

#### 响应示例

```json
{
  "totalIncome": 15000.00,
  "totalExpense": 8000.00,
  "netProfit": 7000.00,
  "totalTransactions": 45,
  "pendingTransactions": 3,
  "confirmedTransactions": 40,
  "cancelledTransactions": 2,
  "cashAmount": 8000.00,
  "cardAmount": 6000.00,
  "giftCardAmount": 1000.00,
  "bankTransferAmount": 0.00,
  "otherAmount": 0.00
}
```

---

### 23. 获取今日交易统计

**GET** `/api/transactions/stats/today`

获取当天的交易统计信息。

---

### 24. 获取本周交易统计

**GET** `/api/transactions/stats/this-week`

获取本周的交易统计信息。

---

### 25. 获取本月交易统计

**GET** `/api/transactions/stats/this-month`

获取本月的交易统计信息。

---

### 26. 检查交易是否存在

**GET** `/api/transactions/{uuid}/exists`

检查指定UUID的交易是否存在。

#### 响应示例

```json
true
```

---

### 27. 根据多个员工获取交易历史

**GET** `/api/transactions/users/history`

获取多个员工参与的交易历史。

#### 查询参数

| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| userUuids | Array | 是 | 员工UUID列表 |

#### 请求示例

```
GET /api/transactions/users/history?userUuids=660e8400-e29b-41d4-a716-446655440001,770e8400-e29b-41d4-a716-446655440002
```

---

### 28. 获取指定员工指定日期交易

**GET** `/api/transactions/users/{userUuid}/date/{date}`

获取指定员工在指定日期参与的所有交易。

#### 路径参数

| 参数 | 类型 | 格式 | 描述 |
|------|------|------|------|
| userUuid | UUID | - | 员工唯一标识符 |
| date | String | YYYY-MM-DD | 交易日期 |

---

### 29. 检查员工是否参与交易

**GET** `/api/transactions/{transactionUuid}/users/{userUuid}/exists`

检查指定员工是否参与了指定交易。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| transactionUuid | UUID | 交易唯一标识符 |
| userUuid | UUID | 员工唯一标识符 |

#### 响应示例

```json
true
```

---

### 30. 向交易添加员工

**POST** `/api/transactions/{transactionUuid}/users/{userUuid}`

向指定交易添加员工。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| transactionUuid | UUID | 交易唯一标识符 |
| userUuid | UUID | 员工唯一标识符 |

#### 响应示例

返回更新后的交易信息。

---

### 31. 从交易中移除员工

**DELETE** `/api/transactions/{transactionUuid}/users/{userUuid}`

从指定交易中移除员工。

#### 注意事项

- 交易至少需要关联一个员工
- 已取消或已包含在日结中的交易不能修改

---

### 32. 按员工统计交易数量

**GET** `/api/transactions/stats/count-by-user`

按员工统计指定时间范围内的交易数量。

#### 查询参数

| 参数 | 类型 | 必填 | 格式 | 描述 |
|------|------|------|------|------|
| startDate | String | 是 | YYYY-MM-DD | 开始日期 |
| endDate | String | 是 | YYYY-MM-DD | 结束日期 |

#### 响应示例

```json
{
  "660e8400-e29b-41d4-a716-446655440001": 15,
  "770e8400-e29b-41d4-a716-446655440002": 12,
  "880e8400-e29b-41d4-a716-446655440003": 8
}
```

---

### 33. 按员工统计交易金额


## 接口信息

**方法:** GET  
**路径:** `/api/transactions/stats/amount-by-user`  
**描述:** 按员工统计指定时间范围内的交易金额

---

## 查询参数

| 参数 | 类型 | 必填 | 格式 | 描述 |
|------|------|------|------|------|
| startDate | String | 是 | YYYY-MM-DD | 开始日期 |
| endDate | String | 是 | YYYY-MM-DD | 结束日期 |

---

## 请求示例

```http
GET /api/transactions/stats/amount-by-user?startDate=2024-06-01&endDate=2024-06-30
```

### cURL 示例

```bash
curl -X GET "http://localhost:8080/api/transactions/stats/amount-by-user?startDate=2024-06-01&endDate=2024-06-30" \
  -H "Content-Type: application/json"
```

---

## 响应示例

### 成功响应 (200 OK)

```json
{
  "660e8400-e29b-41d4-a716-446655440001": 3500.00,
  "770e8400-e29b-41d4-a716-446655440002": 2800.00,
  "880e8400-e29b-41d4-a716-446655440003": 1200.00
}
```

### 无数据响应 (200 OK)

```json
{}
```

### 错误响应

**参数验证失败 (400 Bad Request)**

```json
{
  "message": "开始日期不能为空",
  "status": 400
}
```

**日期格式错误 (400 Bad Request)**

```json
{
  "message": "日期格式错误，请使用 YYYY-MM-DD 格式",
  "status": 400
}
```

---

## 响应字段说明

| 字段 | 类型 | 描述 |
|------|------|------|
| {userUuid} | BigDecimal | 员工UUID作为键，对应的交易总金额作为值 |

### 响应特点

- 返回一个对象，键为员工UUID，值为该员工在指定时间范围内参与的交易总金额
- 只统计状态为 `CONFIRMED`（已确认）的交易
- 如果某个员工在指定时间范围内没有交易，则不会出现在结果中
- 金额保留两位小数
- 金额单位根据系统配置（通常为人民币元）

---

## 业务逻辑

### 统计规则

1. **时间范围**: 查询指定日期范围内（包含起始和结束日期）的所有交易
2. **交易状态**: 只统计状态为 `CONFIRMED`（已确认）的交易
3. **员工关联**: 按参与的员工分组统计交易金额
4. **金额分配**: 一个交易如果有多个员工参与，每个员工都会计入该交易的全部金额
5. **结果排序**: 按员工UUID自然排序返回

### 计算方式

```sql
-- 对应的SQL逻辑（仅供理解）
SELECT 
    user_uuid,
    SUM(total_amount) as total_amount
FROM transactions t 
JOIN transaction_users tu ON t.uuid = tu.transaction_uuid
WHERE t.transaction_date BETWEEN :startDate AND :endDate 
  AND t.status = 'CONFIRMED'
GROUP BY user_uuid
```

---

## 使用场景

### 业务应用

- **员工业绩统计**: 评估员工在特定时期的业务贡献
- **月度/季度分析**: 定期统计员工表现
- **薪酬计算参考**: 为绩效工资提供数据支持
- **员工工作量评估**: 了解员工参与的业务规模

### 数据分析

- **业绩排名**: 根据交易金额对员工进行排序
- **趋势分析**: 对比不同时期员工的业绩变化
- **团队分析**: 分析团队成员的贡献分布
- **目标达成**: 跟踪员工业绩目标完成情况

---

## 注意事项

### 数据准确性

- 统计基于交易的参与员工，不是创建者
- 一笔交易的多个参与员工都会获得该笔交易的全额统计
- 只有已确认的交易才会被统计，待确认和已取消的交易不计入

### 性能考虑

- 建议查询时间范围不超过一年
- 大量数据查询可能需要较长响应时间
- 可以通过缓存机制提高查询效率

### 业务理解

- 统计结果反映的是员工参与的业务总量，而非个人独立完成的业务
- 适用于团队协作较多的业务场景
- 可以结合其他指标（如交易数量）进行综合评估

---
ons/stats) - 获取整体交易统计信息