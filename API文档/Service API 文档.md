# 服务管理模块 API 文档

## API 索引

| 序号 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 1 | POST | `/api/services` | 创建服务 |
| 2 | GET | `/api/services` | 获取所有服务（分页） |
| 3 | GET | `/api/services/{uuid}` | 根据UUID获取服务 |
| 4 | GET | `/api/services/code/{serviceCode}` | 根据服务代码获取服务 |
| 5 | GET | `/api/services/active` | 获取所有可用服务 |
| 6 | GET | `/api/services/type/{serviceType}` | 根据服务类型获取服务 |
| 7 | GET | `/api/services/chair-massage` | 获取椅背按摩服务 |
| 8 | GET | `/api/services/foot-reflexology` | 获取足疗服务 |
| 9 | GET | `/api/services/body-massage` | 获取身体按摩服务 |
| 10 | GET | `/api/services/combination-massage` | 获取组合按摩服务 |
| 11 | GET | `/api/services/price-range` | 根据价格范围获取服务 |
| 12 | GET | `/api/services/duration-range` | 根据时长范围获取服务 |
| 13 | GET | `/api/services/search` | 搜索服务 |
| 14 | GET | `/api/services/quick` | 获取快速服务（≤30分钟） |
| 15 | GET | `/api/services/long` | 获取长时间服务（≥60分钟） |
| 16 | GET | `/api/services/menu` | 获取服务菜单（按类型分组） |
| 17 | PUT | `/api/services/{uuid}` | 更新服务 |
| 18 | DELETE | `/api/services/{uuid}` | 删除服务（软删除） |
| 19 | DELETE | `/api/services/{uuid}/hard` | 彻底删除服务 |
| 20 | POST | `/api/services/{uuid}/activate` | 激活服务 |
| 21 | POST | `/api/services/{uuid}/deactivate` | 停用服务 |
| 22 | PUT | `/api/services/batch-update-prices` | 批量更新价格 |
| 23 | GET | `/api/services/stats` | 获取服务统计 |
| 24 | POST | `/api/services/initialize` | 初始化默认服务 |
| 25 | GET | `/api/services/{uuid}/exists` | 检查服务是否存在 |

## 概述

服务管理模块提供按摩店所有服务项目的管理功能，包括椅背按摩、足疗、身体按摩和组合按摩等服务类型。支持服务的创建、查询、更新、状态管理、价格批量调整等完整的业务操作。

**基础路径**: `/api/services`

---

## 数据模型

### 服务实体 (Service)

| 字段 | 类型 | 必填 | 描述 |
|------|------|------|------|
| uuid | UUID | 是 | 服务唯一标识符 |
| serviceCode | String | 是 | 服务代码（唯一） |
| serviceName | String | 是 | 服务名称 |
| serviceType | Enum | 是 | 服务类型：CHAIR_MASSAGE/FOOT_REFLEXOLOGY/BODY_MASSAGE/COMBINATION_MASSAGE |
| durationMinutes | Integer | 是 | 服务时长（分钟） |
| price | BigDecimal | 是 | 服务价格 |
| description | String | 否 | 服务描述 |
| status | Enum | 是 | 服务状态：ACTIVE/INACTIVE/SEASONAL |
| isCombination | Boolean | 是 | 是否为组合服务 |
| combinationDescription | String | 否 | 组合服务描述 |
| displayOrder | Integer | 否 | 显示顺序 |
| notes | String | 否 | 备注 |
| createdAt | LocalDateTime | 是 | 创建时间（自动生成） |

### 枚举说明

#### 服务类型 (ServiceType)
- `CHAIR_MASSAGE` - 椅背按摩
- `FOOT_REFLEXOLOGY` - 足疗
- `BODY_MASSAGE` - 身体按摩
- `COMBINATION_MASSAGE` - 组合按摩

#### 服务状态 (ServiceStatus)
- `ACTIVE` - 可用
- `INACTIVE` - 停用
- `SEASONAL` - 季节性

---

## API 接口

### 1. 创建服务

**POST** `/api/services`

创建新的服务项目。

#### 请求体

```json
{
  "serviceCode": "BM060",
  "serviceName": "身体按摩 60分钟",
  "serviceType": "BODY_MASSAGE",
  "durationMinutes": 60,
  "price": 60.00,
  "description": "专业的60分钟全身按摩服务",
  "status": "ACTIVE",
  "isCombination": false,
  "combinationDescription": null,
  "displayOrder": 10,
  "notes": "使用天然精油"
}
```

#### 请求字段说明

| 字段 | 类型 | 必填 | 验证规则 | 描述 |
|------|------|------|----------|------|
| serviceCode | String | 是 | 不能为空，唯一 | 服务代码 |
| serviceName | String | 是 | 不能为空，唯一 | 服务名称 |
| serviceType | String | 是 | 枚举值 | 服务类型 |
| durationMinutes | Integer | 是 | 必须大于0 | 服务时长（分钟） |
| price | Number | 是 | 必须大于0.01 | 服务价格 |
| description | String | 否 | - | 服务描述 |
| status | String | 否 | 默认ACTIVE | 服务状态 |
| isCombination | Boolean | 否 | 默认false | 是否为组合服务 |
| combinationDescription | String | 否 | - | 组合服务描述 |
| displayOrder | Integer | 否 | 自动分配 | 显示顺序 |
| notes | String | 否 | - | 备注信息 |

#### 响应示例

**成功 (201 Created)**
```json
{
  "uuid": "550e8400-e29b-41d4-a716-446655440000",
  "createdAt": "2024-06-18T14:30:00",
  "serviceCode": "BM060",
  "serviceName": "身体按摩 60分钟",
  "serviceType": "BODY_MASSAGE",
  "serviceTypeDisplayName": "身体按摩",
  "durationMinutes": 60,
  "price": 60.00,
  "formattedPrice": "$60.00",
  "description": "专业的60分钟全身按摩服务",
  "status": "ACTIVE",
  "isCombination": false,
  "combinationDescription": null,
  "displayOrder": 10,
  "notes": "使用天然精油",
  "displayName": "身体按摩 60分钟 (60分钟)"
}
```

**验证失败 (400 Bad Request)**
```json
{
  "message": "服务代码已存在",
  "status": 400
}
```

---

### 2. 获取所有服务（分页）

**GET** `/api/services`

分页获取所有服务列表。

#### 查询参数

| 参数 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| page | int | 0 | 页码（从0开始） |
| size | int | 10 | 每页数量 |
| sortBy | String | displayOrder | 排序字段 |
| sortDir | String | asc | 排序方向（asc/desc） |

#### 请求示例

```
GET /api/services?page=0&size=20&sortBy=price&sortDir=asc
```

#### 响应示例

```json
{
  "content": [
    {
      "uuid": "550e8400-e29b-41d4-a716-446655440000",
      "serviceCode": "CM012",
      "serviceName": "椅背按摩 12分钟",
      "serviceType": "CHAIR_MASSAGE",
      "serviceTypeDisplayName": "椅背按摩",
      "durationMinutes": 12,
      "price": 12.00,
      "formattedPrice": "$12.00",
      "status": "ACTIVE",
      "displayOrder": 1,
      "displayName": "椅背按摩 12分钟 (12分钟)"
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

### 3. 根据UUID获取服务

**GET** `/api/services/{uuid}`

根据服务UUID获取服务详细信息。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 服务唯一标识符 |

#### 响应示例

**成功 (200 OK)**

返回完整的服务信息，格式参考"创建服务"的响应。

**服务不存在 (404 Not Found)**
```json
{
  "message": "服务未找到: 550e8400-e29b-41d4-a716-446655440000",
  "status": 404
}
```

---

### 4. 根据服务代码获取服务

**GET** `/api/services/code/{serviceCode}`

根据服务代码获取服务信息。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| serviceCode | String | 服务代码 |

#### 请求示例

```
GET /api/services/code/BM060
```

---

### 5. 获取所有可用服务

**GET** `/api/services/active`

获取所有状态为ACTIVE的服务，按显示顺序排序。

#### 响应示例

```json
[
  {
    "uuid": "550e8400-e29b-41d4-a716-446655440000",
    "serviceCode": "CM012",
    "serviceName": "椅背按摩 12分钟",
    "serviceType": "CHAIR_MASSAGE",
    "serviceTypeDisplayName": "椅背按摩",
    "durationMinutes": 12,
    "price": 12.00,
    "formattedPrice": "$12.00",
    "status": "ACTIVE",
    "displayOrder": 1
  }
]
```

---

### 6. 根据服务类型获取服务

**GET** `/api/services/type/{serviceType}`

根据服务类型获取可用的服务列表。

#### 路径参数

| 参数 | 类型 | 可选值 | 描述 |
|------|------|--------|------|
| serviceType | String | CHAIR_MASSAGE, FOOT_REFLEXOLOGY, BODY_MASSAGE, COMBINATION_MASSAGE | 服务类型 |

#### 请求示例

```
GET /api/services/type/CHAIR_MASSAGE
```

---

### 7. 获取椅背按摩服务

**GET** `/api/services/chair-massage`

获取所有可用的椅背按摩服务，按时长排序。

---

### 8. 获取足疗服务

**GET** `/api/services/foot-reflexology`

获取所有可用的足疗服务，按时长排序。

---

### 9. 获取身体按摩服务

**GET** `/api/services/body-massage`

获取所有可用的身体按摩服务，按时长排序。

---

### 10. 获取组合按摩服务

**GET** `/api/services/combination-massage`

获取所有可用的组合按摩服务，按时长排序。

---

### 11. 根据价格范围获取服务

**GET** `/api/services/price-range`

根据价格范围获取服务列表。

#### 查询参数

| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| minPrice | BigDecimal | 是 | 最低价格 |
| maxPrice | BigDecimal | 是 | 最高价格 |

#### 请求示例

```
GET /api/services/price-range?minPrice=20.00&maxPrice=50.00
```

---

### 12. 根据时长范围获取服务

**GET** `/api/services/duration-range`

根据服务时长范围获取服务列表。

#### 查询参数

| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| minDuration | Integer | 是 | 最短时长（分钟） |
| maxDuration | Integer | 是 | 最长时长（分钟） |

#### 请求示例

```
GET /api/services/duration-range?minDuration=30&maxDuration=60
```

---

### 13. 搜索服务

**GET** `/api/services/search`

根据关键词搜索服务名称。

#### 查询参数

| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| keyword | String | 是 | 搜索关键词 |

#### 请求示例

```
GET /api/services/search?keyword=按摩
```

---

### 14. 获取快速服务

**GET** `/api/services/quick`

获取所有30分钟以下的快速服务。

---

### 15. 获取长时间服务

**GET** `/api/services/long`

获取所有60分钟以上的长时间服务。

---

### 16. 获取服务菜单

**GET** `/api/services/menu`

获取按服务类型分组的服务菜单，适用于客户端展示。

#### 响应示例

```json
[
  {
    "serviceType": "CHAIR_MASSAGE",
    "serviceTypeDisplayName": "椅背按摩",
    "services": [
      {
        "uuid": "550e8400-e29b-41d4-a716-446655440000",
        "serviceCode": "CM012",
        "serviceName": "椅背按摩 12分钟",
        "durationMinutes": 12,
        "price": 12.00,
        "formattedPrice": "$12.00",
        "displayName": "椅背按摩 12分钟 (12分钟)"
      },
      {
        "uuid": "660e8400-e29b-41d4-a716-446655440001",
        "serviceCode": "CM020",
        "serviceName": "椅背按摩 20分钟",
        "durationMinutes": 20,
        "price": 20.00,
        "formattedPrice": "$20.00",
        "displayName": "椅背按摩 20分钟 (20分钟)"
      }
    ]
  },
  {
    "serviceType": "FOOT_REFLEXOLOGY",
    "serviceTypeDisplayName": "足疗",
    "services": [
      {
        "uuid": "770e8400-e29b-41d4-a716-446655440002",
        "serviceCode": "FR030",
        "serviceName": "足疗 30分钟",
        "durationMinutes": 30,
        "price": 30.00,
        "formattedPrice": "$30.00",
        "displayName": "足疗 30分钟 (30分钟)"
      }
    ]
  }
]
```

---

### 17. 更新服务

**PUT** `/api/services/{uuid}`

更新指定服务的信息。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 服务唯一标识符 |

#### 请求体

```json
{
  "serviceName": "身体按摩 60分钟（升级版）",
  "durationMinutes": 65,
  "price": 65.00,
  "description": "升级版的60分钟全身按摩服务，使用进口精油",
  "status": "ACTIVE",
  "displayOrder": 15,
  "notes": "使用进口天然精油"
}
```

#### 注意事项

- 所有字段都是可选的，只更新提供的字段
- 服务代码不能修改
- 更新服务名称时会检查唯一性

---

### 18. 删除服务（软删除）

**DELETE** `/api/services/{uuid}`

将服务状态设置为INACTIVE，实现软删除。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 服务唯一标识符 |

#### 响应示例

**成功 (204 No Content)**

无响应体。

---

### 19. 彻底删除服务

**DELETE** `/api/services/{uuid}/hard`

从数据库中彻底删除服务记录。

#### 注意事项

- 此操作不可恢复
- 建议先确认该服务没有被其他业务数据引用

---

### 20. 激活服务

**POST** `/api/services/{uuid}/activate`

激活指定的服务，将状态设置为ACTIVE。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 服务唯一标识符 |

#### 响应示例

**成功 (200 OK)**

返回激活后的服务信息。

---

### 21. 停用服务

**POST** `/api/services/{uuid}/deactivate`

停用指定的服务，将状态设置为INACTIVE。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 服务唯一标识符 |

---

### 22. 批量更新价格

**PUT** `/api/services/batch-update-prices`

批量更新多个服务的价格。

#### 请求体

```json
{
  "550e8400-e29b-41d4-a716-446655440000": 15.00,
  "660e8400-e29b-41d4-a716-446655440001": 25.00,
  "770e8400-e29b-41d4-a716-446655440002": 35.00
}
```

#### 响应示例

**成功 (200 OK)**
```json
[
  {
    "uuid": "550e8400-e29b-41d4-a716-446655440000",
    "serviceCode": "CM012",
    "serviceName": "椅背按摩 12分钟",
    "price": 15.00,
    "formattedPrice": "$15.00"
  },
  {
    "uuid": "660e8400-e29b-41d4-a716-446655440001",
    "serviceCode": "CM020",
    "serviceName": "椅背按摩 20分钟",
    "price": 25.00,
    "formattedPrice": "$25.00"
  }
]
```

---

### 23. 获取服务统计

**GET** `/api/services/stats`

获取服务的统计信息。

#### 响应示例

```json
{
  "totalServices": 15,
  "activeServices": 12,
  "inactiveServices": 3,
  "chairMassageServices": 4,
  "footReflexologyServices": 3,
  "bodyMassageServices": 3,
  "combinationServices": 2,
  "averagePrice": 42.50,
  "highestPrice": 70.00,
  "lowestPrice": 12.00
}
```

#### 响应字段说明

| 字段 | 类型 | 描述 |
|------|------|------|
| totalServices | Long | 总服务数量 |
| activeServices | Long | 可用服务数量 |
| inactiveServices | Long | 停用服务数量 |
| chairMassageServices | Long | 椅背按摩服务数量 |
| footReflexologyServices | Long | 足疗服务数量 |
| bodyMassageServices | Long | 身体按摩服务数量 |
| combinationServices | Long | 组合按摩服务数量 |
| averagePrice | BigDecimal | 平均价格 |
| highestPrice | BigDecimal | 最高价格 |
| lowestPrice | BigDecimal | 最低价格 |

---

### 24. 初始化默认服务

**POST** `/api/services/initialize`

初始化系统默认的服务数据。

#### 响应示例

**成功 (200 OK)**
```json
"默认服务初始化完成"
```

#### 默认服务列表

系统会创建以下默认服务：

**椅背按摩：**
- CM012: 椅背按摩 12分钟 ($12.00)
- CM017: 椅背按摩 17分钟 ($17.00)
- CM020: 椅背按摩 20分钟 ($20.00)
- CM030: 椅背按摩 30分钟 ($30.00)

**足疗：**
- FR030: 足疗 30分钟 ($30.00)
- FR040: 足疗 40分钟 ($40.00)
- FR050: 足疗 50分钟 ($48.00)

**身体按摩：**
- BM030: 身体按摩 30分钟 ($30.00)
- BM040: 身体按摩 40分钟 ($40.00)
- BM060: 身体按摩 60分钟 ($60.00)

**组合按摩：**
- COMB030: 组合按摩 30分钟 ($30.00)
- COMB040: 组合按摩 40分钟 ($40.00)
- COMB060: 组合按摩 60分钟 ($60.00)
- COMB070: 组合按摩 70分钟 ($70.00)

---

### 25. 检查服务是否存在

**GET** `/api/services/{uuid}/exists`

检查指定UUID的服务是否存在。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 服务唯一标识符 |

#### 响应示例

```json
true
```

---

## 业务流程说明

### 服务类型说明

1. **椅背按摩 (CHAIR_MASSAGE)** - 在专用按摩椅上进行的按摩服务
2. **足疗 (FOOT_REFLEXOLOGY)** - 足部反射疗法和按摩
3. **身体按摩 (BODY_MASSAGE)** - 全身或部分身体按摩
4. **组合按摩 (COMBINATION_MASSAGE)** - 多种按摩方式的组合

### 服务状态管理

- **ACTIVE** - 服务可用，客户可以预订
- **INACTIVE** - 服务停用，不对客户开放
- **SEASONAL** - 季节性服务，特定时期可用

### 价格格式

- 所有价格以美元为单位
- 支持两位小数精度
- 提供格式化价格显示（如 $60.00）

### 显示顺序

- 服务可以通过 displayOrder 字段控制在菜单中的显示顺序
- 较小的数值会优先显示
- 如果未指定，系统会自动分配下一个可用序号

---

## 错误码说明

| HTTP状态码 | 错误类型 | 描述 |
|------------|----------|------|
| 400 | Bad Request | 请求参数验证失败或业务逻辑错误 |
| 404 | Not Found | 请求的资源不存在 |
| 500 | Internal Server Error | 服务器内部错误 |

## 常见错误信息

### 创建服务相关
- `服务代码已存在` - 尝试使用已存在的服务代码
- `服务名称已存在` - 尝试使用已存在的服务名称
- `服务代码不能为空` - 未提供服务代码
- `服务名称不能为空` - 未提供服务名称
- `服务时长必须大于0分钟` - 服务时长无效
- `服务价格必须大于0` - 服务价格无效

### 查询服务相关
- `服务未找到: {uuid}` - 指定的服务不存在
- `服务未找到: {serviceCode}` - 指定的服务代码不存在

## 注意事项

### 1. 服务代码规范
- 椅背按摩：CM + 时长（如 CM012, CM020）
- 足疗：FR + 时长（如 FR030, FR040）
- 身体按摩：BM + 时长（如 BM030, BM060）
- 组合按摩：COMB + 时长（如 COMB030, COMB060）

### 2. 数据完整性
- 服务代码和服务名称必须唯一
- 删除服务建议使用软删除（停用）而非硬删除
- 批量操作支持事务处理

### 3. 业务规则
- 组合服务的 isCombination 字段会根据服务类型自动设置
- 价格支持两位小数精度
- 时长以分钟为单位，必须为正整数

### 4. 性能优化
- 分页查询支持多种排序方式
- 按服务类型查询已建立索引
- 统计查询使用数据库聚合函数优化

### 5. 扩展功能
- 支持季节性服务管理
- 支持服务组合描述
- 支持批量价格调整
- 支持服务菜单分类展示

这个服务管理系统为按摩店提供了完整的服务项目管理功能，支持多种服务类型、灵活的价格管理和便捷的查询筛选功能。