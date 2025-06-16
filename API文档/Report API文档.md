# Report API 文档

## 概述
Report API 提供完整的报表管理功能，包括多种类型报表的生成、管理、查询和统计分析功能。

## 基础信息
- **Base URL**: `/api/reports`
- **Content-Type**: `application/json`
- **支持跨域**: 是

---

## 1. 基础CRUD操作

### 1.1 创建报表
**POST** `/api/reports`

#### 请求体 (CreateReportRequest)
```json
{
  "reportName": "2024年1月财务汇总报表",    // Required: 报表名称
  "reportType": "FINANCIAL_SUMMARY",      // Required: 报表类型
  "periodType": "MONTHLY",               // Required: 周期类型
  "startDate": "2024-01-01",             // Required: 开始日期
  "endDate": "2024-01-31",               // Required: 结束日期
  "generatedByUuid": "550e8400-e29b-41d4-a716-446655440001", // Required: 生成者UUID
  "description": "1月份的财务汇总分析报表", // Optional: 报表描述
  "parameters": "{\"includeDetails\": true}" // Optional: 报表参数(JSON字符串)
}
```

#### 报表类型 (ReportType)
- `FINANCIAL_SUMMARY`: 财务汇总报表
- `REVENUE_ANALYSIS`: 收入分析报表
- `EMPLOYEE_PERFORMANCE`: 员工绩效报表
- `CUSTOMER_ANALYSIS`: 客户分析报表
- `SERVICE_POPULARITY`: 服务热度报表
- `DAILY_CUTOFF`: 日结报表
- `MONTHLY_SUMMARY`: 月度汇总报表
- `YEARLY_SUMMARY`: 年度汇总报表
- `CUSTOM`: 自定义报表

#### 周期类型 (PeriodType)
- `DAILY`: 日报
- `WEEKLY`: 周报
- `MONTHLY`: 月报
- `QUARTERLY`: 季报
- `YEARLY`: 年报
- `CUSTOM`: 自定义周期

#### 报表状态 (ReportStatus)
- `PENDING`: 待生成
- `GENERATING`: 生成中
- `COMPLETED`: 已完成
- `FAILED`: 生成失败

#### 响应示例
```json
{
  "uuid": "550e8400-e29b-41d4-a716-446655440002",
  "createdAt": "2024-02-01T10:00:00",
  "reportName": "2024年1月财务汇总报表",
  "reportType": "FINANCIAL_SUMMARY",
  "reportTypeDisplayName": "财务汇总报表",
  "periodType": "MONTHLY",
  "periodTypeDisplayName": "月报",
  "startDate": "2024-01-01",
  "endDate": "2024-01-31",
  "status": "PENDING",
  "generatedByUuid": "550e8400-e29b-41d4-a716-446655440001",
  "filePath": null,
  "description": "1月份的财务汇总分析报表",
  "parameters": "{\"includeDetails\": true}",
  "completedAt": null,
  "errorMessage": null,
  "generationTimeMs": null
}
```

### 1.2 获取所有报表 (分页)
**GET** `/api/reports?page=0&size=10&sortBy=createdAt&sortDir=desc`

#### 查询参数
- `page`: 页码 (默认0)
- `size`: 每页大小 (默认10)
- `sortBy`: 排序字段 (默认createdAt)
- `sortDir`: 排序方向 (asc/desc, 默认desc)

### 1.3 根据UUID获取报表
**GET** `/api/reports/{uuid}`

### 1.4 更新报表
**PUT** `/api/reports/{uuid}`

#### 请求体 (UpdateReportRequest)
```json
{
  "reportName": "更新后的报表名称",      // Optional: 更新报表名称
  "description": "更新后的描述",        // Optional: 更新描述
  "status": "COMPLETED",              // Optional: 更新状态
  "filePath": "/reports/file.pdf",    // Optional: 更新文件路径
  "errorMessage": null                // Optional: 错误信息
}
```

### 1.5 删除报表
**DELETE** `/api/reports/{uuid}`

---

## 2. 报表查询和筛选

### 2.1 根据报表类型获取报表
**GET** `/api/reports/type/{reportType}`

#### 示例
```bash
GET /api/reports/type/FINANCIAL_SUMMARY   # 获取所有财务汇总报表
GET /api/reports/type/DAILY_CUTOFF        # 获取所有日结报表
```

### 2.2 根据报表状态获取报表
**GET** `/api/reports/status/{status}`

#### 示例
```bash
GET /api/reports/status/PENDING     # 获取待生成报表
GET /api/reports/status/COMPLETED   # 获取已完成报表
GET /api/reports/status/FAILED      # 获取失败报表
```

### 2.3 根据生成者获取报表
**GET** `/api/reports/user/{userUuid}`

### 2.4 获取待生成的报表
**GET** `/api/reports/pending`

### 2.5 获取已完成的报表
**GET** `/api/reports/completed`

### 2.6 获取失败的报表
**GET** `/api/reports/failed`

### 2.7 搜索报表
**GET** `/api/reports/search?keyword=财务`

#### 查询参数
- `keyword`: 搜索关键词 (匹配报表名称或描述)

### 2.8 获取今日报表
**GET** `/api/reports/today`

### 2.9 获取本周报表
**GET** `/api/reports/this-week`

### 2.10 获取本月报表
**GET** `/api/reports/this-month`

---

## 3. 报表业务操作

### 3.1 重新生成报表
**POST** `/api/reports/{uuid}/regenerate`

重新生成已存在的报表

### 3.2 生成财务汇总报表
**POST** `/api/reports/financial-summary?startDate=2024-01-01&endDate=2024-01-31`

#### 查询参数
- `startDate`: 开始日期
- `endDate`: 结束日期

#### 响应示例 (FinancialSummaryReportData)
```json
{
  "startDate": "2024-01-01",
  "endDate": "2024-01-31",
  "totalIncome": 25800.00,
  "totalExpense": 3800.00,
  "netProfit": 22000.00,
  "profitMargin": 85.27,
  "totalTransactions": 150,
  "paymentMethodBreakdown": [
    {
      "paymentMethod": "CASH",
      "amount": 12000.00,
      "transactionCount": 80,
      "percentage": 46.51
    },
    {
      "paymentMethod": "CARD", 
      "amount": 13800.00,
      "transactionCount": 70,
      "percentage": 53.49
    }
  ],
  "incomeBreakdown": [
    {
      "category": "CHAIR_MASSAGE_SERVICE",
      "categoryDisplayName": "椅背按摩服务",
      "amount": 10200.00,
      "transactionCount": 150,
      "percentage": 39.53
    },
    {
      "category": "FOOT_REFLEXOLOGY_SERVICE",
      "categoryDisplayName": "足疗服务",
      "amount": 15600.00,
      "transactionCount": 177,
      "percentage": 60.47
    }
  ],
  "expenseBreakdown": [
    {
      "category": "RENT_EXPENSE",
      "categoryDisplayName": "租金支出",
      "amount": 2000.00,
      "transactionCount": 1,
      "percentage": 52.63
    },
    {
      "category": "SUPPLY_EXPENSE",
      "categoryDisplayName": "用品采购",
      "amount": 1800.00,
      "transactionCount": 5,
      "percentage": 47.37
    }
  ]
}
```

### 3.3 快速生成预设时间段报表

#### 生成今日财务汇总报表
**POST** `/api/reports/financial-summary/today`

#### 生成本周财务汇总报表
**POST** `/api/reports/financial-summary/this-week`

#### 生成本月财务汇总报表
**POST** `/api/reports/financial-summary/this-month`

#### 生成本年财务汇总报表
**POST** `/api/reports/financial-summary/this-year`

### 3.4 快速创建报表

#### 快速创建财务汇总报表
**POST** `/api/reports/quick/financial-summary?startDate=2024-01-01&endDate=2024-01-31&generatedByUuid=550e8400-e29b-41d4-a716-446655440001`

#### 快速创建日结报表
**POST** `/api/reports/quick/daily-cutoff?date=2024-01-01&generatedByUuid=550e8400-e29b-41d4-a716-446655440001`

#### 快速创建月度汇总报表
**POST** `/api/reports/quick/monthly-summary?year=2024&month=1&generatedByUuid=550e8400-e29b-41d4-a716-446655440001`

#### 快速创建年度汇总报表
**POST** `/api/reports/quick/yearly-summary?year=2024&generatedByUuid=550e8400-e29b-41d4-a716-446655440001`

---

## 4. 统计和元数据

### 4.1 获取报表统计信息
**GET** `/api/reports/stats`

#### 响应示例 (ReportStatsResponse)
```json
{
  "totalReports": 250,
  "pendingReports": 5,
  "generatingReports": 2,
  "completedReports": 240,
  "failedReports": 3,
  "financialSummaryReports": 50,
  "revenueAnalysisReports": 40,
  "employeePerformanceReports": 30,
  "customerAnalysisReports": 35,
  "servicePopularityReports": 25,
  "dailyCutoffReports": 60,
  "monthlySummaryReports": 8,
  "yearlySummaryReports": 2,
  "customReports": 0,
  "averageGenerationTimeMs": 2500.0,
  "longestGenerationTimeMs": 15000,
  "shortestGenerationTimeMs": 500,
  "successRate": 96.0
}
```

### 4.2 检查报表是否存在
**GET** `/api/reports/{uuid}/exists`

### 4.3 获取报表类型列表
**GET** `/api/reports/types`

#### 响应
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

### 4.4 获取周期类型列表
**GET** `/api/reports/periods`

#### 响应
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

## 使用示例

### 创建自定义报表
```javascript
// 创建季度财务汇总报表
const quarterlyReport = await fetch('/api/reports', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    reportName: '2024年Q1季度财务汇总',
    reportType: 'FINANCIAL_SUMMARY',
    periodType: 'QUARTERLY',
    startDate: '2024-01-01',
    endDate: '2024-03-31',
    generatedByUuid: '550e8400-e29b-41d4-a716-446655440001',
    description: '第一季度完整的财务分析报表',
    parameters: JSON.stringify({
      includeDetails: true,
      compareWithLastPeriod: true,
      includeForecast: false
    })
  })
});

const report = await quarterlyReport.json();
console.log('季度报表创建成功:', report.reportName);

// 监控报表生成状态
async function monitorReportGeneration(reportUuid) {
  let status = 'PENDING';
  while (status === 'PENDING' || status === 'GENERATING') {
    await new Promise(resolve => setTimeout(resolve, 3000)); // 等待3秒
    
    const response = await fetch(`/api/reports/${reportUuid}`);
    const reportData = await response.json();
    status = reportData.status;
    
    console.log(`报表状态: ${status}`);
    
    if (status === 'COMPLETED') {
      console.log(`报表生成完成，文件路径: ${reportData.filePath}`);
      console.log(`生成耗时: ${reportData.generationTimeMs}ms`);
      break;
    } else if (status === 'FAILED') {
      console.log(`报表生成失败: ${reportData.errorMessage}`);
      break;
    }
  }
}

await monitorReportGeneration(report.uuid);
```

### 批量生成报表
```javascript
// 为每个月生成财务汇总报表
async function generateMonthlyReports(year, userUuid) {
  console.log(`=== 生成${year}年月度报表 ===`);
  
  const reports = [];
  
  for (let month = 1; month <= 12; month++) {
    const startDate = new Date(year, month - 1, 1).toISOString().split('T')[0];
    const endDate = new Date(year, month, 0).toISOString().split('T')[0];
    
    try {
      const response = await fetch(`/api/reports/quick/monthly-summary?year=${year}&month=${month}&generatedByUuid=${userUuid}`, {
        method: 'POST'
      });
      
      const report = await response.json();
      reports.push(report);
      console.log(`✓ ${month}月报表创建成功: ${report.reportName}`);
    } catch (error) {
      console.log(`✗ ${month}月报表创建失败:`, error);
    }
  }
  
  return reports;
}

const yearlyReports = await generateMonthlyReports(2024, '550e8400-e29b-41d4-a716-446655440001');
```

### 报表分析和比较
```javascript
// 生成并比较多个时间段的财务报表
async function compareFinancialPeriods() {
  console.log('=== 财务期间对比分析 ===');
  
  const periods = [
    { name: '本月', startDate: '2024-01-01', endDate: '2024-01-31' },
    { name: '上月', startDate: '2023-12-01', endDate: '2023-12-31' },
    { name: '去年同期', startDate: '2023-01-01', endDate: '2023-01-31' }
  ];
  
  const reports = [];
  
  for (const period of periods) {
    const response = await fetch(
      `/api/reports/financial-summary?startDate=${period.startDate}&endDate=${period.endDate}`
    );
    const reportData = await response.json();
    
    reports.push({
      period: period.name,
      data: reportData
    });
  }
  
  // 对比分析
  console.log('\n=== 财务对比结果 ===');
  console.log('期间\t\t总收入\t\t总支出\t\t净利润\t\t利润率');
  console.log('─'.repeat(60));
  
  reports.forEach(report => {
    const { data } = report;
    console.log(`${report.period}\t\t¥${data.totalIncome}\t¥${data.totalExpense}\t¥${data.netProfit}\t${data.profitMargin}%`);
  });
  
  // 计算增长率
  if (reports.length >= 2) {
    const current = reports[0].data;
    const previous = reports[1].data;
    
    const incomeGrowth = ((current.totalIncome - previous.totalIncome) / previous.totalIncome * 100).toFixed(2);
    const profitGrowth = ((current.netProfit - previous.netProfit) / previous.netProfit * 100).toFixed(2);
    
    console.log(`\n=== 同比增长 ===`);
    console.log(`收入增长: ${incomeGrowth}%`);
    console.log(`利润增长: ${profitGrowth}%`);
  }
  
  return reports;
}

const comparison = await compareFinancialPeriods();
```

### 报表管理和维护
```javascript
// 报表系统维护和清理
async function maintainReportSystem() {
  console.log('=== 报表系统维护 ===');
  
  // 1. 获取系统统计
  const statsResponse = await fetch('/api/reports/stats');
  const stats = await statsResponse.json();
  
  console.log('系统统计:');
  console.log(`总报表数: ${stats.totalReports}`);
  console.log(`成功率: ${stats.successRate}%`);
  console.log(`平均生成时间: ${stats.averageGenerationTimeMs}ms`);
  
  // 2. 处理失败的报表
  const failedResponse = await fetch('/api/reports/failed');
  const failedReports = await failedResponse.json();
  
  if (failedReports.length > 0) {
    console.log(`\n发现 ${failedReports.length} 个失败报表，尝试重新生成...`);
    
    for (const report of failedReports) {
      try {
        await fetch(`/api/reports/${report.uuid}/regenerate`, {
          method: 'POST'
        });
        console.log(`✓ 重新生成报表: ${report.reportName}`);
      } catch (error) {
        console.log(`✗ 重新生成失败: ${report.reportName}`);
      }
    }
  }
  
  // 3. 清理老旧报表 (保留最近3个月)
  const allReportsResponse = await fetch('/api/reports?size=1000');
  const allReports = (await allReportsResponse.json()).content;
  
  const threeMonthsAgo = new Date();
  threeMonthsAgo.setMonth(threeMonthsAgo.getMonth() - 3);
  
  const oldReports = allReports.filter(report => 
    new Date(report.createdAt) < threeMonthsAgo && 
    report.status === 'COMPLETED'
  );
  
  console.log(`\n发现 ${oldReports.length} 个3个月前的报表`);
  console.log('建议清理以释放存储空间');
  
  // 可以根据需要添加自动清理逻辑
  // for (const report of oldReports) {
  //   await fetch(`/api/reports/${report.uuid}`, { method: 'DELETE' });
  // }
  
  return {
    stats,
    failedReports,
    oldReports
  };
}

const maintenanceResult = await maintainReportSystem();
```

### 定制化报表生成
```javascript
// 高级定制化报表
async function generateCustomAnalysisReport(userUuid) {
  // 创建员工绩效分析报表
  const performanceReport = await fetch('/api/reports', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      reportName: '员工绩效深度分析报表',
      reportType: 'EMPLOYEE_PERFORMANCE',
      periodType: 'MONTHLY',
      startDate: '2024-01-01',
      endDate: '2024-01-31',
      generatedByUuid: userUuid,
      description: '包含服务质量、客户满意度、收入贡献等多维度分析',
      parameters: JSON.stringify({
        includeServiceBreakdown: true,
        includeCustomerFeedback: true,
        compareWithPrevious: true,
        includeRecommendations: true,
        detailLevel: 'comprehensive'
      })
    })
  });

  // 创建客户行为分析报表
  const customerReport = await fetch('/api/reports', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      reportName: '客户消费行为分析报表',
      reportType: 'CUSTOMER_ANALYSIS',
      periodType: 'QUARTERLY',
      startDate: '2024-01-01',
      endDate: '2024-03-31',
      generatedByUuid: userUuid,
      description: '分析客户消费模式、偏好和生命周期价值',
      parameters: JSON.stringify({
        segmentByMembership: true,
        includeRetentionAnalysis: true,
        includePredictiveModeling: true,
        analyzeServicePreferences: true
      })
    })
  });

  // 创建服务热度分析报表
  const serviceReport = await fetch('/api/reports', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      reportName: '服务项目热度与盈利能力分析',
      reportType: 'SERVICE_POPULARITY',
      periodType: 'MONTHLY',
      startDate: '2024-01-01',
      endDate: '2024-01-31',
      generatedByUuid: userUuid,
      description: '分析各服务项目的受欢迎程度和盈利贡献',
      parameters: JSON.stringify({
        includeSeasonalTrends: true,
        compareProfitability: true,
        analyzeMarketDemand: true,
        includeCompetitorAnalysis: false
      })
    })
  });

  const reports = await Promise.all([
    performanceReport.json(),
    customerReport.json(),
    serviceReport.json()
  ]);

  console.log('=== 定制化分析报表创建完成 ===');
  reports.forEach((report, index) => {
    const types = ['员工绩效', '客户行为', '服务热度'];
    console.log(`${types[index]}报表: ${report.reportName} (${report.uuid})`);
  });

  return reports;
}

// 使用示例
const customReports = await generateCustomAnalysisReport('550e8400-e29b-41d4-a716-446655440001');
```

### 报表导出和分发
```javascript
// 报表导出和自动分发系统
async function exportAndDistributeReports() {
  console.log('=== 报表导出分发系统 ===');
  
  // 1. 获取已完成的重要报表
  const completedResponse = await fetch('/api/reports/completed');
  const completedReports = await completedResponse.json();
  
  // 筛选需要分发的报表类型
  const importantReports = completedReports.filter(report => 
    ['FINANCIAL_SUMMARY', 'MONTHLY_SUMMARY', 'YEARLY_SUMMARY'].includes(report.reportType) &&
    report.completedAt && 
    new Date(report.completedAt) > new Date(Date.now() - 24 * 60 * 60 * 1000) // 24小时内完成的
  );
  
  console.log(`找到 ${importantReports.length} 个需要分发的重要报表`);
  
  // 2. 按报表类型分组
  const reportsByType = {};
  importantReports.forEach(report => {
    if (!reportsByType[report.reportType]) {
      reportsByType[report.reportType] = [];
    }
    reportsByType[report.reportType].push(report);
  });
  
  // 3. 生成分发列表
  const distributionList = {
    'FINANCIAL_SUMMARY': ['manager@spa.com', 'finance@spa.com', 'owner@spa.com'],
    'MONTHLY_SUMMARY': ['manager@spa.com', 'owner@spa.com'],
    'YEARLY_SUMMARY': ['owner@spa.com', 'board@spa.com'],
    'EMPLOYEE_PERFORMANCE': ['hr@spa.com', 'manager@spa.com'],
    'CUSTOMER_ANALYSIS': ['marketing@spa.com', 'manager@spa.com']
  };
  
  // 4. 模拟分发过程
  Object.entries(reportsByType).forEach(([type, reports]) => {
    const recipients = distributionList[type] || [];
    console.log(`\n${type} 报表分发:`);
    
    reports.forEach(report => {
      console.log(`  报表: ${report.reportName}`);
      console.log(`  文件: ${report.filePath || '未生成文件'}`);
      console.log(`  收件人: ${recipients.join(', ')}`);
      console.log(`  生成时间: ${report.completedAt}`);
      console.log('  ---');
    });
  });
  
  return {
    totalReports: importantReports.length,
    reportsByType,
    distributionList
  };
}

const distribution = await exportAndDistributeReports();
```

### 报表性能监控
```javascript
// 报表系统性能监控
async function monitorReportPerformance() {
  console.log('=== 报表性能监控 ===');
  
  // 1. 获取统计信息
  const statsResponse = await fetch('/api/reports/stats');
  const stats = await statsResponse.json();
  
  // 2. 性能分析
  console.log('=== 性能指标 ===');
  console.log(`总报表数: ${stats.totalReports}`);
  console.log(`成功率: ${stats.successRate}%`);
  console.log(`平均生成时间: ${stats.averageGenerationTimeMs}ms`);
  console.log(`最长生成时间: ${stats.longestGenerationTimeMs}ms`);
  console.log(`最短生成时间: ${stats.shortestGenerationTimeMs}ms`);
  
  // 3. 性能评级
  let performanceGrade = 'A';
  let recommendations = [];
  
  if (stats.successRate < 95) {
    performanceGrade = 'D';
    recommendations.push('成功率过低，需要检查报表生成逻辑');
  } else if (stats.successRate < 98) {
    performanceGrade = 'C';
    recommendations.push('成功率有待提升');
  }
  
  if (stats.averageGenerationTimeMs > 10000) {
    performanceGrade = performanceGrade === 'A' ? 'B' : 'D';
    recommendations.push('平均生成时间过长，建议优化算法');
  } else if (stats.averageGenerationTimeMs > 5000) {
    performanceGrade = performanceGrade === 'A' ? 'B' : performanceGrade;
    recommendations.push('生成时间可以进一步优化');
  }
  
  if (stats.longestGenerationTimeMs > 30000) {
    recommendations.push('存在极慢的报表生成，需要专项优化');
  }
  
  console.log(`\n=== 性能评级: ${performanceGrade} ===`);
  if (recommendations.length > 0) {
    console.log('改进建议:');
    recommendations.forEach((rec, index) => {
      console.log(`${index + 1}. ${rec}`);
    });
  } else {
    console.log('系统性能良好！');
  }
  
  // 4. 报表类型分析
  console.log('\n=== 报表类型分布 ===');
  const typeStats = [
    { type: '财务汇总', count: stats.financialSummaryReports },
    { type: '收入分析', count: stats.revenueAnalysisReports },
    { type: '员工绩效', count: stats.employeePerformanceReports },
    { type: '客户分析', count: stats.customerAnalysisReports },
    { type: '服务热度', count: stats.servicePopularityReports },
    { type: '日结报表', count: stats.dailyCutoffReports },
    { type: '月度汇总', count: stats.monthlySummaryReports },
    { type: '年度汇总', count: stats.yearlySummaryReports }
  ];
  
  typeStats.sort((a, b) => b.count - a.count);
  typeStats.forEach(stat => {
    const percentage = ((stat.count / stats.totalReports) * 100).toFixed(1);
    console.log(`${stat.type}: ${stat.count} (${percentage}%)`);
  });
  
  return {
    stats,
    performanceGrade,
    recommendations,
    typeDistribution: typeStats
  };
}

const monitoring = await monitorReportPerformance();
```

### 报表数据透视分析
```javascript
// 报表数据深度分析
async function performDataPivotAnalysis(startDate, endDate) {
  console.log('=== 数据透视分析 ===');
  
  // 1. 生成基础财务报表
  const financialResponse = await fetch(
    `/api/reports/financial-summary?startDate=${startDate}&endDate=${endDate}`
  );
  const financial = await financialResponse.json();
  
  console.log('=== 财务概览 ===');
  console.log(`分析期间: ${startDate} 至 ${endDate}`);
  console.log(`总收入: ¥${financial.totalIncome.toLocaleString()}`);
  console.log(`总支出: ¥${financial.totalExpense.toLocaleString()}`);
  console.log(`净利润: ¥${financial.netProfit.toLocaleString()}`);
  console.log(`利润率: ${financial.profitMargin.toFixed(2)}%`);
  
  // 2. 支付方式分析
  console.log('\n=== 支付方式分析 ===');
  financial.paymentMethodBreakdown.forEach(payment => {
    console.log(`${payment.paymentMethod}: ¥${payment.amount.toLocaleString()} (${payment.percentage.toFixed(1)}%) - ${payment.transactionCount}笔`);
  });
  
  // 3. 收入结构分析
  console.log('\n=== 收入结构分析 ===');
  financial.incomeBreakdown.forEach(income => {
    const avgTransaction = income.amount / income.transactionCount;
    console.log(`${income.categoryDisplayName}:`);
    console.log(`  收入: ¥${income.amount.toLocaleString()} (${income.percentage.toFixed(1)}%)`);
    console.log(`  交易次数: ${income.transactionCount}`);
    console.log(`  平均单价: ¥${avgTransaction.toFixed(2)}`);
  });
  
  // 4. 成本结构分析
  console.log('\n=== 成本结构分析 ===');
  financial.expenseBreakdown.forEach(expense => {
    console.log(`${expense.categoryDisplayName}: ¥${expense.amount.toLocaleString()} (${expense.percentage.toFixed(1)}%)`);
  });
  
  // 5. 关键指标计算
  const keyMetrics = {
    averageTransactionValue: financial.totalIncome / financial.totalTransactions,
    costRatio: (financial.totalExpense / financial.totalIncome) * 100,
    transactionEfficiency: financial.totalTransactions / getDaysBetween(startDate, endDate),
    profitPerTransaction: financial.netProfit / financial.totalTransactions
  };
  
  console.log('\n=== 关键经营指标 ===');
  console.log(`平均交易金额: ¥${keyMetrics.averageTransactionValue.toFixed(2)}`);
  console.log(`成本率: ${keyMetrics.costRatio.toFixed(2)}%`);
  console.log(`日均交易量: ${keyMetrics.transactionEfficiency.toFixed(1)}笔/天`);
  console.log(`单笔交易利润: ¥${keyMetrics.profitPerTransaction.toFixed(2)}`);
  
  return {
    financial,
    keyMetrics,
    insights: generateBusinessInsights(financial, keyMetrics)
  };
}

// 辅助函数
function getDaysBetween(startDate, endDate) {
  const start = new Date(startDate);
  const end = new Date(endDate);
  return Math.ceil((end - start) / (1000 * 60 * 60 * 24)) + 1;
}

function generateBusinessInsights(financial, metrics) {
  const insights = [];
  
  if (financial.profitMargin > 80) {
    insights.push('利润率优异，业务健康度很高');
  } else if (financial.profitMargin > 60) {
    insights.push('利润率良好，有进一步优化空间');
  } else {
    insights.push('利润率偏低，需要重点关注成本控制');
  }
  
  if (metrics.averageTransactionValue > 150) {
    insights.push('客单价较高，客户消费能力强');
  } else if (metrics.averageTransactionValue < 80) {
    insights.push('客单价偏低，可考虑推广高价值服务');
  }
  
  const cashRatio = financial.paymentMethodBreakdown.find(p => p.paymentMethod === 'CASH')?.percentage || 0;
  if (cashRatio > 60) {
    insights.push('现金支付比例较高，建议推广电子支付');
  }
  
  return insights;
}

// 使用示例
const analysis = await performDataPivotAnalysis('2024-01-01', '2024-01-31');
console.log('\n=== 业务洞察 ===');
analysis.insights.forEach((insight, index) => {
  console.log(`${index + 1}. ${insight}`);
});
```