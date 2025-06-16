# Transaction API 文档

## 概述
Transaction API 提供完整的交易管理功能，包括收入支出记录、日结操作、交易统计和财务分析。

## 基础信息
- **Base URL**: `/api/transactions`
- **Content-Type**: `application/json`
- **支持跨域**: 是

---

## 1. 基础CRUD操作

### 1.1 创建交易
**POST** `/api/transactions`

#### 请求体 (CreateTransactionRequest)
```json
{
  "type": "INCOME",                 // Required: 交易类型 (INCOME/EXPENSE)
  "transactionDate": "2024-01-01",  // Required: 交易日期
  "description": "椅背按摩服务",     // Optional: 交易描述
  "notes": "VIP客户消费",           // Optional: 备注
  "relatedCustomerUuid": "550e8400-e29b-41d4-a716-446655440000", // Optional: 关联客户UUID
  "relatedUserUuid": "550e8400-e29b-41d4-a716-446655440001",     // Required: 操作员工UUID
  "paymentMethod": "CASH",          // Required: 支付方式
  "paymentReference": "CASH001",    // Optional: 支付参考号
  "items": [                        // Required: 交易明细列表 (至少1个)
    {
      "category": "CHAIR_MASSAGE_SERVICE", // Required: 交易分类
      "amount": 68.00,              // Required: 金额 (≥0.01)
      "description": "经典椅背按摩30分钟", // Optional: 明细描述
      "notes": "使用优惠券"          // Optional: 明细备注
    }
  ]
}
```

#### 交易类型 (TransactionType)
- `INCOME`: 收入
- `EXPENSE`: 支出

#### 支付方式 (PaymentMethod)
- `CASH`: 现金
- `CARD`: 刷卡
- `GIFT_CARD`: 礼品卡
- `BANK_TRANSFER`: 银行转账
- `OTHER`: 其他

#### 交易分类 (TransactionCategory)
**收入分类:**
- `CHAIR_MASSAGE_SERVICE`: 椅背按摩服务
- `FOOT_REFLEXOLOGY_SERVICE`: 足疗服务
- `BODY_MASSAGE_SERVICE`: 身体按摩服务
- `COMBINATION_MASSAGE_SERVICE`: 组合按摩服务
- `GIFT_CARD_SALE`: 礼品卡销售
- `MEMBERSHIP_FEE`: 会员费用
- `OTHER_INCOME`: 其他收入

**支出分类:**
- `RENT_EXPENSE`: 租金支出
- `SALARY_EXPENSE`: 工资支出
- `UTILITY_EXPENSE`: 水电费
- `SUPPLY_EXPENSE`: 用品采购
- `MARKETING_EXPENSE`: 营销费用
- `MAINTENANCE_EXPENSE`: 维护费用
- `OTHER_EXPENSE`: 其他支出

#### 响应示例
```json
{
  "uuid": "550e8400-e29b-41d4-a716-446655440002",
  "transactionNumber": "T202401010001",
  "createdAt": "2024-01-01T10:30:00",
  "transactionDate": "2024-01-01",
  "type": "INCOME",
  "totalAmount": 68.00,
  "description": "椅背按摩服务",
  "notes": "VIP客户消费",
  "relatedCustomerUuid": "550e8400-e29b-41d4-a716-446655440000",
  "relatedUserUuid": "550e8400-e29b-41d4-a716-446655440001",
  "paymentMethod": "CASH",
  "paymentReference": "CASH001",
  "status": "CONFIRMED",
  "isIncludedInCutOff": false,
  "cutOffDate": null,
  "items": [
    {
      "uuid": "550e8400-e29b-41d4-a716-446655440003",
      "category": "CHAIR_MASSAGE_SERVICE",
      "categoryDisplayName": "椅背按摩服务",
      "amount": 68.00,
      "description": "经典椅背按摩30分钟",
      "notes": "使用优惠券"
    }
  ]
}
```

### 1.2 获取所有交易 (分页)
**GET** `/api/transactions?page=0&size=10&sortBy=transactionDate&sortDir=desc`

#### 查询参数
- `page`: 页码 (默认0)
- `size`: 每页大小 (默认10)
- `sortBy`: 排序字段 (默认transactionDate)
- `sortDir`: 排序方向 (asc/desc, 默认desc)

### 1.3 根据UUID获取交易
**GET** `/api/transactions/{uuid}`

### 1.4 根据交易编号获取交易
**GET** `/api/transactions/number/{transactionNumber}`

#### 示例
```bash
GET /api/transactions/number/T202401010001
```

### 1.5 更新交易
**PUT** `/api/transactions/{uuid}`

#### 请求体 (UpdateTransactionRequest)
```json
{
  "transactionDate": "2024-01-02",  // Optional: 更新交易日期
  "description": "更新后的描述",     // Optional: 更新描述
  "paymentMethod": "CARD",          // Optional: 更新支付方式
  "status": "CONFIRMED",            // Optional: 更新状态
  "items": [                        // Optional: 更新交易明细
    {
      "category": "CHAIR_MASSAGE_SERVICE",
      "amount": 88.00,
      "description": "高级椅背按摩45分钟"
    }
  ]
}
```

### 1.6 删除交易
**DELETE** `/api/transactions/{uuid}`

---

## 2. 交易查询和检索

### 2.1 根据交易类型获取交易
**GET** `/api/transactions/type/{type}`

#### 路径参数
- `type`: 交易类型 (INCOME/EXPENSE)

### 2.2 根据交易状态获取交易
**GET** `/api/transactions/status/{status}`

#### 交易状态 (TransactionStatus)
- `PENDING`: 待确认
- `CONFIRMED`: 已确认
- `CANCELLED`: 已取消

### 2.3 根据交易日期获取交易
**GET** `/api/transactions/date/2024-01-01`

### 2.4 根据日期范围获取交易
**GET** `/api/transactions/date-range?startDate=2024-01-01&endDate=2024-01-31`

#### 查询参数
- `startDate`: 开始日期
- `endDate`: 结束日期

### 2.5 获取收入交易
**GET** `/api/transactions/income`

### 2.6 获取支出交易
**GET** `/api/transactions/expense`

### 2.7 获取待确认的交易
**GET** `/api/transactions/pending`

### 2.8 获取未处理的交易 (未包含在日结中)
**GET** `/api/transactions/unprocessed`

### 2.9 根据客户获取交易历史
**GET** `/api/transactions/customer/{customerUuid}`

### 2.10 根据员工获取交易历史
**GET** `/api/transactions/user/{userUuid}`

---

## 3. 交易业务操作

### 3.1 确认交易
**POST** `/api/transactions/{uuid}/confirm`

将交易状态设置为CONFIRMED

### 3.2 取消交易
**POST** `/api/transactions/{uuid}/cancel`

将交易状态设置为CANCELLED

### 3.3 执行日结
**POST** `/api/transactions/daily-cutoff?cutOffDate=2024-01-01`

#### 查询参数
- `cutOffDate`: 日结日期

#### 响应示例 (DailyCutOffReportResponse)
```json
{
  "cutOffDate": "2024-01-01",
  "totalIncome": 2580.00,
  "totalExpense": 380.00,
  "netProfit": 2200.00,
  "transactionCount": 15,
  "paymentMethodSummaries": [
    {
      "paymentMethod": "CASH",
      "amount": 1200.00,
      "count": 8
    },
    {
      "paymentMethod": "CARD",
      "amount": 1380.00,
      "count": 7
    }
  ],
  "incomeCategorySummaries": [
    {
      "category": "CHAIR_MASSAGE_SERVICE",
      "categoryDisplayName": "椅背按摩服务",
      "amount": 1020.00,
      "count": 15
    },
    {
      "category": "FOOT_REFLEXOLOGY_SERVICE",
      "categoryDisplayName": "足疗服务",
      "amount": 1560.00,
      "count": 18
    }
  ],
  "expenseCategorySummaries": [
    {
      "category": "SUPPLY_EXPENSE",
      "categoryDisplayName": "用品采购",
      "amount": 380.00,
      "count": 2
    }
  ]
}
```

### 3.4 获取日结报告
**GET** `/api/transactions/daily-cutoff/{cutOffDate}`

### 3.5 获取今日日结报告
**GET** `/api/transactions/daily-cutoff/today`

---

## 4. 统计和分析

### 4.1 获取交易统计
**GET** `/api/transactions/stats?startDate=2024-01-01&endDate=2024-01-31`

#### 查询参数
- `startDate`: 开始日期
- `endDate`: 结束日期

#### 响应示例 (TransactionStatsResponse)
```json
{
  "totalIncome": 25800.00,
  "totalExpense": 3800.00,
  "netProfit": 22000.00,
  "totalTransactions": 150,
  "pendingTransactions": 5,
  "confirmedTransactions": 140,
  "cancelledTransactions": 5,
  "cashAmount": 12000.00,
  "cardAmount": 13800.00,
  "giftCardAmount": 0.00,
  "bankTransferAmount": 0.00,
  "otherAmount": 0.00
}
```

### 4.2 获取今日交易统计
**GET** `/api/transactions/stats/today`

### 4.3 获取本周交易统计
**GET** `/api/transactions/stats/this-week`

### 4.4 获取本月交易统计
**GET** `/api/transactions/stats/this-month`

### 4.5 检查交易是否存在
**GET** `/api/transactions/{uuid}/exists`

---

## 使用示例

### 创建完整的服务交易
```javascript
// 创建一笔椅背按摩收入交易
const transaction = await fetch('/api/transactions', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    type: 'INCOME',
    transactionDate: '2024-01-01',
    description: '椅背按摩服务',
    relatedCustomerUuid: '550e8400-e29b-41d4-a716-446655440000',
    relatedUserUuid: '550e8400-e29b-41d4-a716-446655440001',
    paymentMethod: 'CASH',
    items: [
      {
        category: 'CHAIR_MASSAGE_SERVICE',
        amount: 68.00,
        description: '经典椅背按摩30分钟'
      }
    ]
  })
});

const result = await transaction.json();
console.log('交易创建成功:', result.transactionNumber);

// 立即确认交易
await fetch(`/api/transactions/${result.uuid}/confirm`, {
  method: 'POST'
});
```

### 创建组合服务交易
```javascript
// 创建椅背+足疗组合服务交易
const comboTransaction = await fetch('/api/transactions', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    type: 'INCOME',
    transactionDate: '2024-01-01',
    description: '椅背+足疗组合服务',
    relatedCustomerUuid: '550e8400-e29b-41d4-a716-446655440000',
    relatedUserUuid: '550e8400-e29b-41d4-a716-446655440001',
    paymentMethod: 'CARD',
    paymentReference: 'CARD20240101001',
    items: [
      {
        category: 'CHAIR_MASSAGE_SERVICE',
        amount: 68.00,
        description: '椅背按摩30分钟'
      },
      {
        category: 'FOOT_REFLEXOLOGY_SERVICE',
        amount: 88.00,
        description: '足疗45分钟'
      }
    ]
  })
});
```

### 记录支出交易
```javascript
// 记录用品采购支出
const expense = await fetch('/api/transactions', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    type: 'EXPENSE',
    transactionDate: '2024-01-01',
    description: '按摩用品采购',
    relatedUserUuid: '550e8400-e29b-41d4-a716-446655440001',
    paymentMethod: 'BANK_TRANSFER',
    paymentReference: 'TRANSFER20240101001',
    items: [
      {
        category: 'SUPPLY_EXPENSE',
        amount: 380.00,
        description: '按摩油、毛巾等用品'
      }
    ]
  })
});
```

### 执行日结操作
```javascript
// 执行今日日结
const cutOffDate = new Date().toISOString().split('T')[0]; // 今日日期
const cutOffResponse = await fetch(
  `/api/transactions/daily-cutoff?cutOffDate=${cutOffDate}`,
  { method: 'POST' }
);
const cutOffReport = await cutOffResponse.json();

console.log('=== 日结报告 ===');
console.log(`日期: ${cutOffReport.cutOffDate}`);
console.log(`总收入: ¥${cutOffReport.totalIncome}`);
console.log(`总支出: ¥${cutOffReport.totalExpense}`);
console.log(`净利润: ¥${cutOffReport.netProfit}`);
console.log(`交易笔数: ${cutOffReport.transactionCount}`);

console.log('\n=== 支付方式明细 ===');
cutOffReport.paymentMethodSummaries.forEach(summary => {
  console.log(`${summary.paymentMethod}: ¥${summary.amount} (${summary.count}笔)`);
});

console.log('\n=== 收入分类明细 ===');
cutOffReport.incomeCategorySummaries.forEach(summary => {
  console.log(`${summary.categoryDisplayName}: ¥${summary.amount} (${summary.count}笔)`);
});
```

### 获取客户消费历史
```javascript
async function getCustomerSpendingHistory(customerUuid) {
  const response = await fetch(`/api/transactions/customer/${customerUuid}`);
  const transactions = await response.json();
  
  // 只显示收入交易（客户消费）
  const spendings = transactions.filter(t => t.type === 'INCOME');
  
  let totalSpent = 0;
  console.log('=== 客户消费历史 ===');
  spendings.forEach(transaction => {
    console.log(`${transaction.transactionDate}: ${transaction.description} - ¥${transaction.totalAmount}`);
    totalSpent += transaction.totalAmount;
  });
  console.log(`总消费: ¥${totalSpent}`);
  
  return { transactions: spendings, totalSpent };
}

// 使用示例
const customerHistory = await getCustomerSpendingHistory('550e8400-e29b-41d4-a716-446655440000');
```

### 生成财务报表
```javascript
async function generateFinancialReport(startDate, endDate) {
  // 获取统计数据
  const statsResponse = await fetch(
    `/api/transactions/stats?startDate=${startDate}&endDate=${endDate}`
  );
  const stats = await statsResponse.json();
  
  // 获取详细交易
  const transactionsResponse = await fetch(
    `/api/transactions/date-range?startDate=${startDate}&endDate=${endDate}`
  );
  const transactions = await transactionsResponse.json();
  
  console.log('=== 财务报表 ===');
  console.log(`报表期间: ${startDate} 至 ${endDate}`);
  console.log(`总收入: ¥${stats.totalIncome}`);
  console.log(`总支出: ¥${stats.totalExpense}`);
  console.log(`净利润: ¥${stats.netProfit}`);
  console.log(`利润率: ${((stats.netProfit / stats.totalIncome) * 100).toFixed(2)}%`);
  console.log(`交易总数: ${stats.totalTransactions}`);
  
  console.log('\n=== 支付方式分析 ===');
  const totalPayments = stats.cashAmount + stats.cardAmount + stats.giftCardAmount;
  console.log(`现金: ¥${stats.cashAmount} (${(stats.cashAmount/totalPayments*100).toFixed(1)}%)`);
  console.log(`刷卡: ¥${stats.cardAmount} (${(stats.cardAmount/totalPayments*100).toFixed(1)}%)`);
  
  return { stats, transactions };
}

// 生成本月报表
const thisMonth = new Date();
const startDate = new Date(thisMonth.getFullYear(), thisMonth.getMonth(), 1)
  .toISOString().split('T')[0];
const endDate = thisMonth.toISOString().split('T')[0];

const report = await generateFinancialReport(startDate, endDate);
```