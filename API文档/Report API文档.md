# 报表管理模块 API 文档

## API 索引

| 序号 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 1 | POST | `/api/reports` | 创建报表 |
| 2 | GET | `/api/reports` | 获取所有报表（分页） |
| 3 | GET | `/api/reports/{uuid}` | 根据UUID获取报表 |
| 4 | GET | `/api/reports/type/{reportType}` | 根据报表类型获取报表 |
| 5 | GET | `/api/reports/status/{status}` | 根据报表状态获取报表 |
| 6 | GET | `/api/reports/user/{userUuid}` | 根据生成者获取报表 |
| 7 | GET | `/api/reports/pending` | 获取待生成的报表 |
| 8 | GET | `/api/reports/completed` | 获取已完成的报表 |
| 9 | GET | `/api/reports/failed` | 获取失败的报表 |
| 10 | GET | `/api/reports/search` | 搜索报表 |
| 11 | GET | `/api/reports/today` | 获取今日报表 |
| 12 | GET | `/api/reports/this-week` | 获取本周报表 |
| 13 | GET | `/api/reports/this-month` | 获取本月报表 |
| 14 | PUT | `/api/reports/{uuid}` | 更新报表 |
| 15 | DELETE | `/api/reports/{uuid}` | 删除报表 |
| 16 | POST | `/api/reports/{uuid}/regenerate` | 重新生成报表 |
| 17 | POST | `/api/reports/financial-summary` | 生成财务汇总报表 |
| 18 | POST | `/api/reports/financial-summary/today` | 生成今日财务汇总报表 |
| 19 | POST | `/api/reports/financial-summary/this-week` | 生成本周财务汇总报表 |
| 20 | POST | `/api/reports/financial-summary/this-month` | 生成本月财务汇总报表 |
| 21 | POST | `/api/reports/financial-summary/this-year` | 生成本年财务汇总报表 |
| 22 | POST | `/api/reports/quick/financial-summary` | 快速创建财务汇总报表 |
| 23 | POST | `/api/reports/quick/daily-cutoff` | 快速创建日结报表 |
| 24 | POST | `/api/reports/quick/monthly-summary` | 快速创建月度汇总报表 |
| 25 | POST | `/api/reports/quick/yearly-summary` | 快速创建年度汇总报表 |
| 26 | GET | `/api/reports/stats` | 获取报表统计 |
| 27 | GET | `/api/reports/{uuid}/exists` | 检查报表是否存在 |
| 28 | GET | `/api/reports/types` | 获取报表类型列表 |
| 29 | GET | `/api/reports/periods` | 获取周期类型列表 |

## 概述

报表管理模块提供按摩店业务数据的多维度分析和报表生成功能。支持财务汇总、营收分析、员工业绩、客户分析、服务热度等多种报表类型，并提供灵活的时间周期选择和异步生成机制。

**基础路径**: `/api/reports`

---

## 数据模型

### 报表实体 (Report)

| 字段 | 类型 | 必填 | 描述 |
|------|------|------|------|
| uuid | UUID | 是 | 报表唯一标识符 |
| reportName | String | 是 | 报表名称 |
| reportType | Enum | 是 | 报表类型 |
| periodType | Enum | 是 | 周期类型 |
| startDate | LocalDate | 是 | 开始日期 |
| endDate | LocalDate | 是 | 结束日期 |
| status | Enum | 是 | 报表状态：PENDING/GENERATING/COMPLETED/FAILED |
| generatedByUuid | UUID | 是 | 生成者UUID |
| filePath | String | 否 | 文件路径 |
| description | String | 否 | 报表描述 |
| parameters | String | 否 | 生成参数（JSON格式） |
| completedAt | LocalDateTime | 否 | 完成时间 |
| errorMessage | String | 否 | 错误信息 |
| generationTimeMs | Long | 否 | 生成耗时（毫秒） |
| createdAt | LocalDateTime | 是 | 创建时间（自动生成） |

### 枚举说明

#### 报表类型 (ReportType)
- `FINANCIAL_SUMMARY` - 财务汇总报表
- `REVENUE_ANALYSIS` - 营收分析报表
- `EMPLOYEE_PERFORMANCE` - 员工业绩报表
- `CUSTOMER_ANALYSIS` - 客户分析报表
- `SERVICE_POPULARITY` - 服务受欢迎度报表
- `DAILY_CUTOFF` - 日结报表
- `MONTHLY_SUMMARY` - 月度汇总报表
- `YEARLY_SUMMARY` - 年度汇总报表
- `CUSTOM` - 自定义报表

#### 周期类型 (PeriodType)
- `DAILY` - 日报
- `WEEKLY` - 周报
- `MONTHLY` - 月报
- `QUARTERLY` - 季报
- `YEARLY` - 年报
- `CUSTOM` - 自定义期间

#### 报表状态 (ReportStatus)
- `PENDING` - 待生成
- `GENERATING` - 生成中
- `COMPLETED` - 已完成
- `FAILED` - 生成失败

---

## API 接口

### 1. 创建报表

**POST** `/api/reports`

创建新的报表生成任务。报表将异步生成。

#### 请求体

```json
{
  "reportName": "2024年6月财务汇总报表",
  "reportType": "FINANCIAL_SUMMARY",
  "periodType": "MONTHLY",
  "startDate": "2024-06-01",
  "endDate": "2024-06-30",
  "generatedByUuid": "550e8400-e29b-41d4-a716-446655440000",
  "description": "6月份完整的财务汇总分析",
  "parameters": "{\"includeCharts\": true, \"detailLevel\": \"high\"}"
}
```

#### 请求字段说明

| 字段 | 类型 | 必填 | 验证规则 | 描述 |
|------|------|------|----------|------|
| reportName | String | 是 | 不能为空 | 报表名称 |
| reportType | String | 是 | 枚举值 | 报表类型 |
| periodType | String | 是 | 枚举值 | 周期类型 |
| startDate | String | 是 | 日期格式 YYYY-MM-DD | 开始日期 |
| endDate | String | 是 | 日期格式 YYYY-MM-DD | 结束日期 |
| generatedByUuid | String | 是 | UUID格式 | 生成者UUID |
| description | String | 否 | - | 报表描述 |
| parameters | String | 否 | JSON格式 | 生成参数 |

#### 响应示例

**成功 (201 Created)**
```json
{
  "uuid": "660e8400-e29b-41d4-a716-446655440001",
  "createdAt": "2024-06-18T14:30:00",
  "reportName": "2024年6月财务汇总报表",
  "reportType": "FINANCIAL_SUMMARY",
  "reportTypeDisplayName": "财务汇总报表",
  "periodType": "MONTHLY",
  "periodTypeDisplayName": "月报",
  "startDate": "2024-06-01",
  "endDate": "2024-06-30",
  "status": "PENDING",
  "generatedByUuid": "550e8400-e29b-41d4-a716-446655440000",
  "filePath": null,
  "description": "6月份完整的财务汇总分析",
  "parameters": "{\"includeCharts\": true, \"detailLevel\": \"high\"}",
  "completedAt": null,
  "errorMessage": null,
  "generationTimeMs": null
}
```

**验证失败 (400 Bad Request)**
```json
{
  "message": "结束日期不能早于开始日期",
  "status": 400
}
```

**重复报表 (400 Bad Request)**
```json
{
  "message": "已存在相同类型和日期范围的报表",
  "status": 400
}
```

---

### 2. 获取所有报表（分页）

**GET** `/api/reports`

分页获取所有报表列表。

#### 查询参数

| 参数 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| page | int | 0 | 页码（从0开始） |
| size | int | 10 | 每页数量 |
| sortBy | String | createdAt | 排序字段 |
| sortDir | String | desc | 排序方向（asc/desc） |

#### 请求示例

```
GET /api/reports?page=0&size=20&sortBy=completedAt&sortDir=desc
```

#### 响应示例

```json
{
  "content": [
    {
      "uuid": "660e8400-e29b-41d4-a716-446655440001",
      "createdAt": "2024-06-18T14:30:00",
      "reportName": "2024年6月财务汇总报表",
      "reportType": "FINANCIAL_SUMMARY",
      "reportTypeDisplayName": "财务汇总报表",
      "status": "COMPLETED",
      "completedAt": "2024-06-18T14:32:15",
      "generationTimeMs": 135000
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

### 3. 根据UUID获取报表

**GET** `/api/reports/{uuid}`

根据报表UUID获取报表详细信息。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 报表唯一标识符 |

#### 响应示例

**成功 (200 OK)**

返回完整的报表信息，格式参考"创建报表"的响应。

**报表不存在 (404 Not Found)**
```json
{
  "message": "报表未找到: 660e8400-e29b-41d4-a716-446655440001",
  "status": 404
}
```

---

### 4. 根据报表类型获取报表

**GET** `/api/reports/type/{reportType}`

根据报表类型获取报表列表。

#### 路径参数

| 参数 | 类型 | 可选值 | 描述 |
|------|------|--------|------|
| reportType | String | FINANCIAL_SUMMARY, REVENUE_ANALYSIS, EMPLOYEE_PERFORMANCE, CUSTOMER_ANALYSIS, SERVICE_POPULARITY, DAILY_CUTOFF, MONTHLY_SUMMARY, YEARLY_SUMMARY, CUSTOM | 报表类型 |

#### 请求示例

```
GET /api/reports/type/FINANCIAL_SUMMARY
```

---

### 5. 根据报表状态获取报表

**GET** `/api/reports/status/{status}`

根据报表状态获取报表列表。

#### 路径参数

| 参数 | 类型 | 可选值 | 描述 |
|------|------|--------|------|
| status | String | PENDING, GENERATING, COMPLETED, FAILED | 报表状态 |

---

### 6. 根据生成者获取报表

**GET** `/api/reports/user/{userUuid}`

获取指定用户生成的所有报表。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| userUuid | UUID | 用户唯一标识符 |

---

### 7. 获取待生成的报表

**GET** `/api/reports/pending`

获取所有状态为PENDING的报表，按创建时间升序排列。

---

### 8. 获取已完成的报表

**GET** `/api/reports/completed`

获取所有状态为COMPLETED的报表，按完成时间降序排列。

---

### 9. 获取失败的报表

**GET** `/api/reports/failed`

获取所有状态为FAILED的报表，按创建时间降序排列。

---

### 10. 搜索报表

**GET** `/api/reports/search`

根据关键词搜索报表名称。

#### 查询参数

| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| keyword | String | 是 | 搜索关键词 |

#### 请求示例

```
GET /api/reports/search?keyword=财务汇总
```

---

### 11. 获取今日报表

**GET** `/api/reports/today`

获取今天创建的所有报表。

---

### 12. 获取本周报表

**GET** `/api/reports/this-week`

获取本周创建的所有报表。

---

### 13. 获取本月报表

**GET** `/api/reports/this-month`

获取本月创建的所有报表。

---

### 14. 更新报表

**PUT** `/api/reports/{uuid}`

更新指定报表的信息。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 报表唯一标识符 |

#### 请求体

```json
{
  "reportName": "2024年6月财务汇总报表（修订版）",
  "description": "修订后的6月份财务汇总分析",
  "status": "COMPLETED",
  "filePath": "/reports/financial_summary_202406_v2.json",
  "errorMessage": null
}
```

#### 注意事项

- 所有字段都是可选的，只更新提供的字段
- 设置状态为COMPLETED时会自动设置完成时间

---

### 15. 删除报表

**DELETE** `/api/reports/{uuid}`

彻底删除指定的报表记录。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 报表唯一标识符 |

#### 响应示例

**成功 (204 No Content)**

无响应体。

---

### 16. 重新生成报表

**POST** `/api/reports/{uuid}/regenerate`

重新生成已存在的报表。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 报表唯一标识符 |

#### 响应示例

**成功 (200 OK)**

返回重置后的报表信息，状态会变为PENDING。

---

### 17. 生成财务汇总报表

**POST** `/api/reports/financial-summary`

直接生成财务汇总报表数据，不创建报表记录。

#### 查询参数

| 参数 | 类型 | 必填 | 格式 | 描述 |
|------|------|------|------|------|
| startDate | String | 是 | YYYY-MM-DD | 开始日期 |
| endDate | String | 是 | YYYY-MM-DD | 结束日期 |

#### 请求示例

```
POST /api/reports/financial-summary?startDate=2024-06-01&endDate=2024-06-30
```

#### 响应示例

**成功 (200 OK)**
```json
{
  "startDate": "2024-06-01",
  "endDate": "2024-06-30",
  "totalIncome": 15000.00,
  "totalExpense": 8000.00,
  "netProfit": 7000.00,
  "profitMargin": 46.67,
  "totalTransactions": 245,
  "paymentMethodBreakdown": [
    {
      "paymentMethod": "CASH",
      "amount": 8000.00,
      "transactionCount": 150,
      "percentage": 53.33
    },
    {
      "paymentMethod": "CARD",
      "amount": 6000.00,
      "transactionCount": 80,
      "percentage": 40.00
    },
    {
      "paymentMethod": "GIFT_CARD",
      "amount": 1000.00,
      "transactionCount": 15,
      "percentage": 6.67
    }
  ],
  "incomeBreakdown": [
    {
      "category": "SERVICE_MASSAGE",
      "categoryDisplayName": "按摩服务",
      "amount": 12000.00,
      "transactionCount": 180,
      "percentage": 80.00
    },
    {
      "category": "SERVICE_TIP",
      "categoryDisplayName": "小费",
      "amount": 2000.00,
      "transactionCount": 120,
      "percentage": 13.33
    },
    {
      "category": "PRODUCT_SALE",
      "categoryDisplayName": "产品销售",
      "amount": 1000.00,
      "transactionCount": 25,
      "percentage": 6.67
    }
  ],
  "expenseBreakdown": [
    {
      "category": "SALARY_MASSAGE_THERAPIST",
      "categoryDisplayName": "按摩师工资",
      "amount": 4000.00,
      "transactionCount": 8,
      "percentage": 50.00
    },
    {
      "category": "RENT",
      "categoryDisplayName": "房租",
      "amount": 2000.00,
      "transactionCount": 1,
      "percentage": 25.00
    },
    {
      "category": "SUPPLIES",
      "categoryDisplayName": "耗材采购",
      "amount": 1500.00,
      "transactionCount": 15,
      "percentage": 18.75
    },
    {
      "category": "UTILITIES",
      "categoryDisplayName": "水电费",
      "amount": 500.00,
      "transactionCount": 3,
      "percentage": 6.25
    }
  ]
}
```

---

### 18. 生成今日财务汇总报表

**POST** `/api/reports/financial-summary/today`

生成当天的财务汇总报表。

---

### 19. 生成本周财务汇总报表

**POST** `/api/reports/financial-summary/this-week`

生成本周的财务汇总报表。

---

### 20. 生成本月财务汇总报表

**POST** `/api/reports/financial-summary/this-month`

生成本月的财务汇总报表。

---

### 21. 生成本年财务汇总报表

**POST** `/api/reports/financial-summary/this-year`

生成本年的财务汇总报表。

---

### 22. 快速创建财务汇总报表

**POST** `/api/reports/quick/financial-summary`

快速创建财务汇总报表记录并异步生成。

#### 查询参数

| 参数 | 类型 | 必填 | 格式 | 描述 |
|------|------|------|------|------|
| startDate | String | 是 | YYYY-MM-DD | 开始日期 |
| endDate | String | 是 | YYYY-MM-DD | 结束日期 |
| generatedByUuid | String | 是 | UUID | 生成者UUID |

#### 请求示例

```
POST /api/reports/quick/financial-summary?startDate=2024-06-01&endDate=2024-06-30&generatedByUuid=550e8400-e29b-41d4-a716-446655440000
```

---

### 23. 快速创建日结报表

**POST** `/api/reports/quick/daily-cutoff`

快速创建日结报表记录。

#### 查询参数

| 参数 | 类型 | 必填 | 格式 | 描述 |
|------|------|------|------|------|
| date | String | 是 | YYYY-MM-DD | 日结日期 |
| generatedByUuid | String | 是 | UUID | 生成者UUID |

#### 请求示例

```
POST /api/reports/quick/daily-cutoff?date=2024-06-18&generatedByUuid=550e8400-e29b-41d4-a716-446655440000
```

---

### 24. 快速创建月度汇总报表

**POST** `/api/reports/quick/monthly-summary`

快速创建月度汇总报表记录。

#### 查询参数

| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| year | int | 是 | 年份 |
| month | int | 是 | 月份（1-12） |
| generatedByUuid | String | 是 | 生成者UUID |

#### 请求示例

```
POST /api/reports/quick/monthly-summary?year=2024&month=6&generatedByUuid=550e8400-e29b-41d4-a716-446655440000
```

---

### 25. 快速创建年度汇总报表

**POST** `/api/reports/quick/yearly-summary`

快速创建年度汇总报表记录。

#### 查询参数

| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| year | int | 是 | 年份 |
| generatedByUuid | String | 是 | 生成者UUID |

#### 请求示例

```
POST /api/reports/quick/yearly-summary?year=2024&generatedByUuid=550e8400-e29b-41d4-a716-446655440000
```

---

### 26. 获取报表统计

**GET** `/api/reports/stats`

获取报表系统的统计信息。

#### 响应示例

```json
{
  "totalReports": 156,
  "pendingReports": 3,
  "generatingReports": 1,
  "completedReports": 145,
  "failedReports": 7,
  "financialSummaryReports": 45,
  "revenueAnalysisReports": 20,
  "employeePerformanceReports": 15,
  "customerAnalysisReports": 12,
  "servicePopularityReports": 8,
  "dailyCutoffReports": 30,
  "monthlySummaryReports": 18,
  "yearlySummaryReports": 6,
  "customReports": 2,
  "averageGenerationTimeMs": 125000.5,
  "longestGenerationTimeMs": 450000,
  "shortestGenerationTimeMs": 15000,
  "successRate": 95.39
}
```

#### 响应字段说明

| 字段 | 类型 | 描述 |
|------|------|------|
| totalReports | Long | 总报表数量 |
| pendingReports | Long | 待生成报表数量 |
| generatingReports | Long | 生成中报表数量 |
| completedReports | Long | 已完成报表数量 |
| failedReports | Long | 失败报表数量 |
| *Reports | Long | 各类型报表数量 |
| averageGenerationTimeMs | Double | 平均生成时间（毫秒） |
| longestGenerationTimeMs | Long | 最长生成时间（毫秒） |
| shortestGenerationTimeMs | Long | 最短生成时间（毫秒） |
| successRate | Double | 成功率（百分比） |

---

### 27. 检查报表是否存在

**GET** `/api/reports/{uuid}/exists`

检查指定UUID的报表是否存在。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 报表唯一标识符 |

#### 响应示例

```json
true
```

---

### 28. 获取报表类型列表

**GET** `/api/reports/types`

获取所有可用的报表类型。

#### 响应示例

```json
[
  "FINANCIAL_SUMMARY",
  "REVENUE_ANALYSIS",
  "EMPLOYEE_PERFORMANCE",
  "CUSTOMER_ANALYSIS",
  "SERVICE_POPULARITY",
  "DAILY_CUTOFF",
  "MONTHLY_SUMMARY",
  "YEARLY_SUMMARY",
  "CUSTOM"
]
```

---

### 29. 获取周期类型列表

**GET** `/api/reports/periods`

获取所有可用的周期类型。

#### 响应示例

```json
[
  "DAILY",
  "WEEKLY",
  "MONTHLY",
  "QUARTERLY",
  "YEARLY",
  "CUSTOM"
]
```

---

## 业务流程说明

### 报表生成流程

1. **创建报表** - 提交报表生成请求，状态为 `PENDING`
2. **异步生成** - 系统异步处理报表，状态变为 `GENERATING`
3. **完成生成** - 生成成功后状态变为 `COMPLETED`，记录文件路径和生成时间
4. **处理失败** - 生成失败时状态变为 `FAILED`，记录错误信息

### 报表类型说明

1. **财务汇总报表** - 提供完整的收入支出分析，包括利润率、支付方式分布等
2. **营收分析报表** - 深入分析营收趋势和构成
3. **员工业绩报表** - 统计员工的工作表现和业绩数据
4. **客户分析报表** - 分析客户行为和消费模式
5. **服务受欢迎度报表** - 统计各项服务的受欢迎程度
6. **日结报表** - 每日营业数据汇总
7. **月度汇总报表** - 月度业务数据统计
8. **年度汇总报表** - 年度业务数据总结
9. **自定义报表** - 根据特定参数生成的定制化报表

### 异步处理机制

- 报表生成采用异步处理，避免阻塞API响应
- 支持批量生成和定时任务
- 提供生成进度查询和错误重试机制

### 数据安全

- 报表文件存储在安全的服务器路径
- 支持权限控制，只有授权用户可以访问
- 敏感数据支持加密存储

---

## 错误码说明

| HTTP状态码 | 错误类型 | 描述 |
|------------|----------|------|
| 400 | Bad Request | 请求参数验证失败或业务逻辑错误 |
| 404 | Not Found | 请求的资源不存在 |
| 500 | Internal Server Error | 服务器内部错误 |

## 常见错误信息

### 创建报表相关
- `报表名称不能为空` - 未提供报表名称
- `结束日期不能早于开始日期` - 日期范围无效
- `已存在相同类型和日期范围的报表` - 重复的报表请求
- `生成者不能为空` - 未指定报表生成者

### 查询报表相关
- `报表未找到: {uuid}` - 指定的报表不存在

### 生成报表相关
- `报表生成失败: 数据源异常` - 生成过程中数据访问出错
- `报表生成超时` - 生成时间过长导致超时

## 注意事项

### 1. 异步处理
- 报表生成采用异步模式，创建报表后立即返回，后台异步处理
- 可通过查询接口监控报表生成状态
- 大型报表可能需要几分钟到几十分钟的生成时间
- 支持重新生成失败的报表

### 2. 数据一致性
- 报表数据基于生成时刻的数据快照
- 财务数据只统计已确认的交易
- 支持指定时间范围的精确数据统计

### 3. 性能优化
- 复杂报表采用分页查询和批处理
- 缓存机制减少重复计算
- 数据库查询已针对报表场景优化

### 4. 文件管理
- 报表文件统一存储在指定目录
- 支持多种文件格式（JSON、PDF、Excel等）
- 自动清理过期的报表文件

### 5. 权限控制
- 只有授权用户可以创建和查看报表
- 支持按部门和角色控制报表访问权限
- 敏感财务数据需要特殊权限

### 6. 监控告警
- 监控报表生成成功率和耗时
- 失败报表自动告警和重试机制
- 提供详细的错误日志和诊断信息

### 7. 扩展性
- 支持自定义报表类型和模板
- 可配置的报表参数和筛选条件
- 支持多语言和多时区

### 8. 数据导出
- 支持多种格式的数据导出
- 提供API接口直接获取报表数据
- 支持定时自动生成和发送报表

## 财务汇总报表详细说明

财务汇总报表是系统最重要的报表类型，提供全面的财务分析数据：

### 主要指标
- **总收入** - 指定期间内的所有收入交易总和
- **总支出** - 指定期间内的所有支出交易总和
- **净利润** - 总收入减去总支出
- **利润率** - 净利润占总收入的百分比
- **交易数量** - 统计期间内的交易笔数

### 分析维度
1. **支付方式分析** - 按现金、刷卡、礼品卡等方式分类统计
2. **收入分类分析** - 按服务类型、产品销售等分类统计
3. **支出分类分析** - 按工资、房租、耗材等分类统计
4. **时间趋势分析** - 显示收支随时间的变化趋势

### 使用场景
- 月度财务审查和决策支持
- 年度财务报告和税务申报
- 投资者汇报和业务分析
- 成本控制和利润优化

## 快速开始示例

### 1. 创建财务汇总报表
```bash
curl -X POST "http://localhost:8080/api/reports" \
  -H "Content-Type: application/json" \
  -d '{
    "reportName": "6月财务汇总",
    "reportType": "FINANCIAL_SUMMARY", 
    "periodType": "MONTHLY",
    "startDate": "2024-06-01",
    "endDate": "2024-06-30",
    "generatedByUuid": "550e8400-e29b-41d4-a716-446655440000",
    "description": "月度财务分析报表"
  }'
```

### 2. 查询报表状态
```bash
curl -X GET "http://localhost:8080/api/reports/{uuid}"
```

### 3. 直接获取财务数据
```bash
curl -X POST "http://localhost:8080/api/reports/financial-summary?startDate=2024-06-01&endDate=2024-06-30"
```

### 4. 快速创建日结报表
```bash
curl -X POST "http://localhost:8080/api/reports/quick/daily-cutoff?date=2024-06-18&generatedByUuid=550e8400-e29b-41d4-a716-446655440000"
```

## 最佳实践

### 1. 报表命名规范
- 使用描述性的报表名称，包含日期和类型
- 例如：`财务汇总报表 2024-06-01 至 2024-06-30`
- 避免使用特殊字符和过长的名称

### 2. 时间范围选择
- 日报：选择单日时间范围
- 周报：选择周一到周日的完整周期
- 月报：选择月初到月末的完整月份
- 年报：选择年初到年末的完整年度

### 3. 参数配置
- 使用JSON格式配置报表参数
- 包含必要的筛选条件和显示选项
- 记录参数含义便于后续维护

### 4. 错误处理
- 定期检查失败的报表并重新生成
- 分析错误原因并优化报表逻辑
- 建立报表质量监控机制

### 5. 性能优化
- 避免生成过大时间范围的详细报表
- 合理使用缓存减少重复计算
- 在业务低峰期安排大型报表生成

这个报表管理系统为按摩店提供了强大的数据分析和业务洞察能力，支持多维度的财务分析和业务监控，帮助管理者做出更好的经营决策。