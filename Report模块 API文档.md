# Report模块 API文档

## 概述

Report模块提供了完整的报表管理功能，包括报表的创建、生成、查询和统计。支持多种报表类型和周期，具有异步生成、状态跟踪等特性。

## 核心功能

### 📊 报表类型支持
- **财务汇总报表** (FINANCIAL_SUMMARY) - 收入支出汇总分析
- **营收分析报表** (REVENUE_ANALYSIS) - 营收趋势和分析
- **员工业绩报表** (EMPLOYEE_PERFORMANCE) - 员工工作表现统计
- **客户分析报表** (CUSTOMER_ANALYSIS) - 客户行为和偏好分析
- **服务受欢迎度报表** (SERVICE_POPULARITY) - 服务项目受欢迎程度
- **日结报表** (DAILY_CUTOFF) - 每日财务结算
- **月度汇总报表** (MONTHLY_SUMMARY) - 月度业务汇总
- **年度汇总报表** (YEARLY_SUMMARY) - 年度业务汇总
- **自定义报表** (CUSTOM) - 用户自定义的报表

### 🔄 报表状态管理
- **PENDING** - 待生成
- **GENERATING** - 生成中
- **COMPLETED** - 已完成
- **FAILED** - 生成失败

### ⏰ 周期类型
- **DAILY** - 日报
- **WEEKLY** - 周报
- **MONTHLY** - 月报
- **QUARTERLY** - 季报
- **YEARLY** - 年报
- **CUSTOM** - 自定义期间

## API接口

### 1. 报表基础操作

#### 创建报表
```http
POST /api/reports
Content-Type: application/json

{
  "reportName": "2024年3月财务汇总",
  "reportType": "FINANCIAL_SUMMARY",
  "periodType": "MONTHLY",
  "startDate": "2024-03-01",
  "endDate": "2024-03-31",
  "generatedByUuid": "550e8400-e29b-41d4-a716-446655440000",
  "description": "3月份完整财务数据汇总",
  "parameters": "{\"includeDetails\": true}"
}
```

#### 获取所有报表（分页）
```http
GET /api/reports?page=0&size=10&sortBy=createdAt&sortDir=desc
```

#### 根据UUID获取报表
```http
GET /api/reports/{uuid}
```

#### 更新报表
```http
PUT /api/reports/{uuid}
Content-Type: application/json

{
  "reportName": "更新后的报表名称",
  "description": "更新后的描述",
  "status": "COMPLETED"
}
```

#### 删除报表
```http
DELETE /api/reports/{uuid}
```

### 2. 报表查询操作

#### 根据类型查询
```http
GET /api/reports/type/FINANCIAL_SUMMARY
```

#### 根据状态查询
```http
GET /api/reports/status/COMPLETED
```

#### 根据生成者查询
```http
GET /api/reports/user/{userUuid}
```

#### 搜索报表
```http
GET /api/reports/search?keyword=财务
```

#### 时间范围查询
```http
GET /api/reports/today          # 今日报表
GET /api/reports/this-week      # 本周报表
GET /api/reports/this-month     # 本月报表
```

#### 状态分类查询
```http
GET /api/reports/pending        # 待生成报表
GET /api/reports/completed      # 已完成报表
GET /api/reports/failed         # 失败报表
```

### 3. 报表生成操作

#### 重新生成报表
```http
POST /api/reports/{uuid}/regenerate
```

#### 直接生成财务汇总数据
```http
POST /api/reports/financial-summary?startDate=2024-03-01&endDate=2024-03-31
```

#### 快速生成各类报表
```http
# 快速财务汇总
POST /api/reports/quick/financial-summary?startDate=2024-03-01&endDate=2024-03-31&generatedByUuid={uuid}

# 快速日结报表
POST /api/reports/quick/daily-cutoff?date=2024-03-15&generatedByUuid={uuid}

# 快速月度汇总
POST /api/reports/quick/monthly-summary?year=2024&month=3&generatedByUuid={uuid}

# 快速年度汇总
POST /api/reports/quick/yearly-summary?year=2024&generatedByUuid={uuid}
```

#### 预设时间段财务汇总
```http
POST /api/reports/financial-summary/today      # 今日财务汇总
POST /api/reports/financial-summary/this-week  # 本周财务汇总
POST /api/reports/financial-summary/this-month # 本月财务汇总
POST /api/reports/financial-summary/this-year  # 本年财务汇总
```

### 4. 统计和元数据

#### 获取报表统计
```http
GET /api/reports/stats
```

#### 检查报表是否存在
```http
GET /api/reports/{uuid}/exists
```

#### 获取枚举值
```http
GET /api/reports/types      # 报表类型列表
GET /api/reports/periods    # 周期类型列表
GET /api/reports/statuses   # 状态列表
```

## 响应数据示例

### 报表响应数据
```json
{
  "uuid": "550e8400-e29b-41d4-a716-446655440000",
  "createdAt": "2024-03-15T10:30:00",
  "reportName": "2024年3月财务汇总",
  "reportType": "FINANCIAL_SUMMARY",
  "reportTypeDisplayName": "财务汇总报表",
  "periodType": "MONTHLY",
  "periodTypeDisplayName": "月报",
  "startDate": "2024-03-01",
  "endDate": "2024-03-31",
  "status": "COMPLETED",
  "generatedByUuid": "550e8400-e29b-41d4-a716-446655440000",
  "filePath": "/reports/FINANCIAL_SUMMARY_MONTHLY_20240315_103000.json",
  "description": "3月份完整财务数据汇总",
  "parameters": "{\"includeDetails\": true}",
  "completedAt": "2024-03-15T10:35:00",
  "errorMessage": null,
  "generationTimeMs": 300000
}
```

### 财务汇总报表数据
```json
{
  "startDate": "2024-03-01",
  "endDate": "2024-03-31",
  "totalIncome": 15000.00,
  "totalExpense": 8000.00,
  "netProfit": 7000.00,
  "profitMargin": 46.67,
  "totalTransactions": 150,
  "paymentMethodBreakdown": [
    {
      "paymentMethod": "CASH",
      "amount": 9000.00,
      "transactionCount": 90,
      "percentage": 60.0
    },
    {
      "paymentMethod": "CARD",
      "amount": 6000.00,
      "transactionCount": 60,
      "percentage": 40.0
    }
  ],
  "incomeBreakdown": [
    {
      "category": "SERVICE_MASSAGE",
      "categoryDisplayName": "按摩服务",
      "amount": 12000.00,
      "transactionCount": 120,
      "percentage": 80.0
    },
    {
      "category": "SERVICE_TIP",
      "categoryDisplayName": "小费",
      "amount": 3000.00,
      "transactionCount": 80,
      "percentage": 20.0
    }
  ],
  "expenseBreakdown": [
    {
      "category": "SALARY_MASSAGE_THERAPIST",
      "categoryDisplayName": "按摩师工资",
      "amount": 5000.00,
      "transactionCount": 10,
      "percentage": 62.5
    },
    {
      "category": "RENT",
      "categoryDisplayName": "房租",
      "amount": 3000.00,
      "transactionCount": 1,
      "percentage": 37.5
    }
  ]
}
```

### 报表统计数据
```json
{
  "totalReports": 25,
  "pendingReports": 2,
  "generatingReports": 1,
  "completedReports": 20,
  "failedReports": 2,
  "financialSummaryReports": 8,
  "revenueAnalysisReports": 3,
  "employeePerformanceReports": 2,
  "customerAnalysisReports": 4,
  "servicePopularityReports": 2,
  "dailyCutoffReports": 4,
  "monthlySummaryReports": 1,
  "yearlySummaryReports": 1,
  "customReports": 0,
  "averageGenerationTimeMs": 180000.0,
  "longestGenerationTimeMs": 450000,
  "shortestGenerationTimeMs": 60000,
  "successRate": 90.91
}
```

## 使用场景示例

### 场景1：生成今日财务汇总
```javascript
// 1. 直接获取今日财务数据
fetch('/api/reports/financial-summary/today', {
    method: 'POST'
})
.then(response => response.json())
.then(data => {
    console.log('今日收入:', data.totalIncome);
    console.log('今日支出:', data.totalExpense);
    console.log('今日利润:', data.netProfit);
});

// 2. 或创建今日财务报表任务
fetch('/api/reports/quick/daily-cutoff?date=2024-03-15&generatedByUuid=' + userUuid, {
    method: 'POST'
})
.then(response => response.json())
.then(report => {
    console.log('报表创建成功:', report.uuid);
    // 轮询检查生成状态
    checkReportStatus(report.uuid);
});
```

### 场景2：监控报表生成进度
```javascript
function checkReportStatus(reportUuid) {
    const interval = setInterval(() => {
        fetch(`/api/reports/${reportUuid}`)
        .then(response => response.json())
        .then(report => {
            if (report.status === 'COMPLETED') {
                clearInterval(interval);
                console.log('报表生成完成:', report.filePath);
                downloadReport(report.filePath);
            } else if (report.status === 'FAILED') {
                clearInterval(interval);
                console.error('报表生成失败:', report.errorMessage);
            } else {
                console.log('报表生成中...', report.status);
            }
        });
    }, 5000); // 每5秒检查一次
}
```

### 场景3：批量创建月度报表
```javascript
const months = [1, 2, 3, 4, 5, 6]; // 1-6月
const year = 2024;
const userUuid = 'your-user-uuid';

Promise.all(months.map(month => 
    fetch(`/api/reports/quick/monthly-summary?year=${year}&month=${month}&generatedByUuid=${userUuid}`, {
        method: 'POST'
    }).then(response => response.json())
))
.then(reports => {
    console.log(`成功创建${reports.length}个月度报表`);
    reports.forEach(report => {
        console.log(`${report.reportName}: ${report.status}`);
    });
});
```

## 技术特性

### ✨ 异步生成
- 报表生成采用异步处理，避免阻塞用户操作
- 支持状态跟踪和进度监控
- 自动错误处理和重试机制

### 🔒 数据安全
- 完整的权限验证
- 防止重复报表生成
- 安全的文件路径管理

### 📈 性能优化
- 分页查询支持
- 索引优化的数据库查询
- 智能缓存机制

### 🛠 扩展性
- 支持自定义报表类型
- 参数化报表生成
- 插件式报表模板

## 集成建议

### 前端集成
1. **报表列表页面**：显示所有报表，支持筛选和搜索
2. **报表创建页面**：提供报表类型选择和参数配置
3. **报表详情页面**：显示报表内容和生成状态
4. **报表统计面板**：展示报表使用统计和性能指标

### 定时任务集成
```java
@Scheduled(cron = "0 0 1 * * ?") // 每天凌晨1点
public void generateDailyReports() {
    LocalDate yesterday = LocalDate.now().minusDays(1);
    // 自动生成昨日日结报表
    reportService.createDailyCutoffReport(yesterday);
}

@Scheduled(cron = "0 0 2 1 * ?") // 每月1号凌晨2点
public void generateMonthlyReports() {
    LocalDate lastMonth = LocalDate.now().minusMonths(1);
    // 自动生成上月汇总报表
    reportService.createMonthlySummaryReport(lastMonth);
}
```

这个Report模块完整实现了您系统的报表管理需求，与现有的Customer、Service、Transaction、User模块无缝集成，提供了强大的报表生成和管理功能！