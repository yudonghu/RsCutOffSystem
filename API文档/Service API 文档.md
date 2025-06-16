# Service API 文档

## 概述
Service API 提供完整的服务管理功能，包括服务的CRUD操作、分类查询、价格管理和服务菜单功能。

## 基础信息
- **Base URL**: `/api/services`
- **Content-Type**: `application/json`
- **支持跨域**: 是

---

## 1. 基础CRUD操作

### 1.1 创建服务
**POST** `/api/services`

#### 请求体 (CreateServiceRequest)
```json
{
  "serviceCode": "CM001",           // Required: 服务代码 (唯一标识)
  "serviceName": "经典椅背按摩",      // Required: 服务名称
  "serviceType": "CHAIR_MASSAGE",   // Required: 服务类型
  "durationMinutes": 30,            // Required: 服务时长(分钟, ≥1)
  "price": 68.00,                   // Required: 服务价格 (≥0.01)
  "description": "专业椅背按摩服务",  // Optional: 服务描述
  "status": "ACTIVE",               // Optional: 服务状态 (默认ACTIVE)
  "isCombination": false,           // Optional: 是否组合服务 (默认false)
  "combinationDescription": null,    // Optional: 组合服务说明
  "displayOrder": 1,                // Optional: 显示顺序
  "notes": "热门服务"                // Optional: 备注
}
```

#### 服务类型 (ServiceType)
- `CHAIR_MASSAGE`: 椅背按摩
- `FOOT_REFLEXOLOGY`: 足疗反射
- `BODY_MASSAGE`: 身体按摩
- `COMBINATION_MASSAGE`: 组合按摩

#### 服务状态 (ServiceStatus)
- `ACTIVE`: 可用
- `INACTIVE`: 停用

#### 响应示例
```json
{
  "uuid": "550e8400-e29b-41d4-a716-446655440001",
  "createdAt": "2024-01-01T10:00:00",
  "serviceCode": "CM001",
  "serviceName": "经典椅背按摩",
  "serviceType": "CHAIR_MASSAGE",
  "serviceTypeDisplayName": "椅背按摩",
  "durationMinutes": 30,
  "price": 68.00,
  "formattedPrice": "¥68.00",
  "description": "专业椅背按摩服务",
  "status": "ACTIVE",
  "isCombination": false,
  "combinationDescription": null,
  "displayOrder": 1,
  "notes": "热门服务",
  "displayName": "经典椅背按摩 (30分钟)"
}
```

### 1.2 获取所有服务 (分页)
**GET** `/api/services?page=0&size=10&sortBy=displayOrder&sortDir=asc`

#### 查询参数
- `page`: 页码 (默认0)
- `size`: 每页大小 (默认10)
- `sortBy`: 排序字段 (默认displayOrder)
- `sortDir`: 排序方向 (asc/desc, 默认asc)

### 1.3 根据UUID获取服务
**GET** `/api/services/{uuid}`

### 1.4 根据服务代码获取服务
**GET** `/api/services/code/{serviceCode}`

#### 示例
```bash
GET /api/services/code/CM001
```

### 1.5 更新服务
**PUT** `/api/services/{uuid}`

#### 请求体 (UpdateServiceRequest)
```json
{
  "serviceName": "高级椅背按摩",      // Optional: 更新服务名称
  "price": 88.00,                   // Optional: 更新价格
  "durationMinutes": 45,            // Optional: 更新时长
  "status": "INACTIVE"              // Optional: 更新状态
  // 所有字段都是可选的
}
```

### 1.6 删除服务 (软删除)
**DELETE** `/api/services/{uuid}`

将服务状态设置为INACTIVE

### 1.7 彻底删除服务
**DELETE** `/api/services/{uuid}/hard`

从数据库中完全删除服务记录

---

## 2. 服务查询和分类

### 2.1 获取所有可用服务
**GET** `/api/services/active`

返回状态为ACTIVE的所有服务

### 2.2 根据服务类型获取服务
**GET** `/api/services/type/{serviceType}`

#### 路径参数
- `serviceType`: 服务类型

#### 示例
```bash
GET /api/services/type/CHAIR_MASSAGE
```

### 2.3 获取椅背按摩服务
**GET** `/api/services/chair-massage`

### 2.4 获取足疗服务
**GET** `/api/services/foot-reflexology`

### 2.5 获取身体按摩服务
**GET** `/api/services/body-massage`

### 2.6 获取组合按摩服务
**GET** `/api/services/combination-massage`

### 2.7 根据价格范围获取服务
**GET** `/api/services/price-range?minPrice=50&maxPrice=200`

#### 查询参数
- `minPrice`: 最低价格
- `maxPrice`: 最高价格

### 2.8 根据时长范围获取服务
**GET** `/api/services/duration-range?minDuration=30&maxDuration=90`

#### 查询参数
- `minDuration`: 最短时长(分钟)
- `maxDuration`: 最长时长(分钟)

### 2.9 搜索服务
**GET** `/api/services/search?keyword=按摩`

#### 查询参数
- `keyword`: 搜索关键词 (匹配服务名称或描述)

### 2.10 获取快速服务 (30分钟以下)
**GET** `/api/services/quick`

### 2.11 获取长时间服务 (60分钟以上)
**GET** `/api/services/long`

### 2.12 获取服务菜单 (按类型分组)
**GET** `/api/services/menu`

#### 响应示例 (ServiceMenuResponse[])
```json
[
  {
    "serviceType": "CHAIR_MASSAGE",
    "serviceTypeDisplayName": "椅背按摩",
    "services": [
      {
        "uuid": "...",
        "serviceName": "经典椅背按摩",
        "price": 68.00,
        "durationMinutes": 30
        // ... 其他字段
      }
    ]
  },
  {
    "serviceType": "FOOT_REFLEXOLOGY",
    "serviceTypeDisplayName": "足疗反射",
    "services": [
      // 足疗服务列表
    ]
  }
]
```

---

## 3. 服务业务操作

### 3.1 激活服务
**POST** `/api/services/{uuid}/activate`

将服务状态设置为ACTIVE

### 3.2 停用服务
**POST** `/api/services/{uuid}/deactivate`

将服务状态设置为INACTIVE

### 3.3 批量更新价格
**PUT** `/api/services/batch-update-prices`

#### 请求体
```json
{
  "550e8400-e29b-41d4-a716-446655440001": 88.00,
  "550e8400-e29b-41d4-a716-446655440002": 128.00,
  "550e8400-e29b-41d4-a716-446655440003": 188.00
}
```

#### 响应
返回更新后的服务列表

### 3.4 初始化默认服务
**POST** `/api/services/initialize`

创建系统预设的默认服务项目

#### 响应
```json
"默认服务初始化完成"
```

---

## 4. 统计和分析

### 4.1 获取服务统计信息
**GET** `/api/services/stats`

#### 响应示例 (ServiceStatsResponse)
```json
{
  "totalServices": 20,
  "activeServices": 18,
  "inactiveServices": 2,
  "chairMassageServices": 5,
  "footReflexologyServices": 6,
  "bodyMassageServices": 4,
  "combinationServices": 5,
  "averagePrice": 128.50,
  "highestPrice": 299.00,
  "lowestPrice": 58.00
}
```

### 4.2 检查服务是否存在
**GET** `/api/services/{uuid}/exists`

#### 响应
```json
true
```

---

## 错误响应

### 400 Bad Request - 验证失败
```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "服务名称不能为空",
  "path": "/api/services"
}
```

### 409 Conflict - 服务代码重复
```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "服务代码已存在",
  "path": "/api/services"
}
```

---

## 使用示例

### 创建完整的服务套餐
```javascript
// 1. 创建椅背按摩服务
const chairMassage = await fetch('/api/services', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    serviceCode: 'CM001',
    serviceName: '经典椅背按摩',
    serviceType: 'CHAIR_MASSAGE',
    durationMinutes: 30,
    price: 68.00,
    description: '专业椅背放松按摩',
    displayOrder: 1
  })
});

// 2. 创建足疗服务
const footMassage = await fetch('/api/services', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    serviceCode: 'FR001',
    serviceName: '经典足疗',
    serviceType: 'FOOT_REFLEXOLOGY',
    durationMinutes: 45,
    price: 88.00,
    description: '传统足部反射疗法',
    displayOrder: 2
  })
});

// 3. 创建组合服务
const comboService = await fetch('/api/services', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    serviceCode: 'COMBO001',
    serviceName: '椅背+足疗套餐',
    serviceType: 'COMBINATION_MASSAGE',
    durationMinutes: 75,
    price: 138.00,
    isCombination: true,
    combinationDescription: '椅背按摩30分钟 + 足疗45分钟',
    displayOrder: 10
  })
});
```

### 获取服务菜单并显示
```javascript
const menuResponse = await fetch('/api/services/menu');
const menu = await menuResponse.json();

menu.forEach(category => {
  console.log(`\n=== ${category.serviceTypeDisplayName} ===`);
  category.services.forEach(service => {
    console.log(`${service.serviceName} - ${service.formattedPrice} (${service.durationMinutes}分钟)`);
  });
});

// 输出示例:
// === 椅背按摩 ===
// 经典椅背按摩 - ¥68.00 (30分钟)
// 高级椅背按摩 - ¥88.00 (45分钟)
// 
// === 足疗反射 ===
// 经典足疗 - ¥88.00 (45分钟)
// 豪华足疗 - ¥128.00 (60分钟)
```

### 价格调整和服务管理
```javascript
// 1. 搜索需要调价的服务
const searchResponse = await fetch('/api/services/search?keyword=按摩');
const services = await searchResponse.json();

// 2. 批量调价 (所有按摩服务涨价10%)
const priceUpdates = {};
services.forEach(service => {
  priceUpdates[service.uuid] = Math.round(service.price * 1.1 * 100) / 100;
});

const updateResponse = await fetch('/api/services/batch-update-prices', {
  method: 'PUT',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(priceUpdates)
});

const updatedServices = await updateResponse.json();
console.log('价格更新完成:', updatedServices.length, '项服务');

// 3. 获取统计信息
const statsResponse = await fetch('/api/services/stats');
const stats = await statsResponse.json();
console.log('服务统计:', {
  总服务数: stats.totalServices,
  平均价格: stats.averagePrice,
  价格区间: `${stats.lowestPrice} - ${stats.highestPrice}`
});
```

### 根据客户需求推荐服务
```javascript
async function recommendServices(budget, timeLimit) {
  // 根据预算和时间限制推荐服务
  const priceResponse = await fetch(
    `/api/services/price-range?minPrice=0&maxPrice=${budget}`
  );
  const durationResponse = await fetch(
    `/api/services/duration-range?minDuration=0&maxDuration=${timeLimit}`
  );
  
  const affordableServices = await priceResponse.json();
  const timeAppropriateServices = await durationResponse.json();
  
  // 找到既符合预算又符合时间的服务
  const recommendations = affordableServices.filter(service =>
    timeAppropriateServices.some(timeService => 
      timeService.uuid === service.uuid
    )
  );
  
  return recommendations.sort((a, b) => b.price - a.price); // 按价格降序
}

// 使用示例
const recommendations = await recommendServices(100, 60); // 预算100元，时间60分钟内
recommendations.forEach(service => {
  console.log(`推荐: ${service.serviceName} - ${service.formattedPrice} (${service.durationMinutes}分钟)`);
});
```