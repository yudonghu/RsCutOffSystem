// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/dto/ReportDTO.java
package com.relaxationspa.rscutoffsystem.dto;

import com.relaxationspa.rscutoffsystem.entity.Report;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ReportDTO {

    // 创建报表请求DTO
    public static class CreateReportRequest {

        @NotBlank(message = "报表名称不能为空")
        private String reportName;

        @NotNull(message = "报表类型不能为空")
        private Report.ReportType reportType;

        @NotNull(message = "周期类型不能为空")
        private Report.PeriodType periodType;

        @NotNull(message = "开始日期不能为空")
        private LocalDate startDate;

        @NotNull(message = "结束日期不能为空")
        private LocalDate endDate;

        @NotNull(message = "生成者不能为空")
        private UUID generatedByUuid;

        private String description;
        private String parameters;

        // Constructors
        public CreateReportRequest() {}

        // Getters and Setters
        public String getReportName() {
            return reportName;
        }

        public void setReportName(String reportName) {
            this.reportName = reportName;
        }

        public Report.ReportType getReportType() {
            return reportType;
        }

        public void setReportType(Report.ReportType reportType) {
            this.reportType = reportType;
        }

        public Report.PeriodType getPeriodType() {
            return periodType;
        }

        public void setPeriodType(Report.PeriodType periodType) {
            this.periodType = periodType;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public UUID getGeneratedByUuid() {
            return generatedByUuid;
        }

        public void setGeneratedByUuid(UUID generatedByUuid) {
            this.generatedByUuid = generatedByUuid;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getParameters() {
            return parameters;
        }

        public void setParameters(String parameters) {
            this.parameters = parameters;
        }
    }

    // 更新报表请求DTO
    public static class UpdateReportRequest {

        private String reportName;
        private String description;
        private String parameters;
        private Report.ReportStatus status;
        private String filePath;
        private String errorMessage;

        // Getters and Setters
        public String getReportName() {
            return reportName;
        }

        public void setReportName(String reportName) {
            this.reportName = reportName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getParameters() {
            return parameters;
        }

        public void setParameters(String parameters) {
            this.parameters = parameters;
        }

        public Report.ReportStatus getStatus() {
            return status;
        }

        public void setStatus(Report.ReportStatus status) {
            this.status = status;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    // 报表响应DTO
    public static class ReportResponse {

        private UUID uuid;
        private LocalDateTime createdAt;
        private String reportName;
        private Report.ReportType reportType;
        private String reportTypeDisplayName;
        private Report.PeriodType periodType;
        private String periodTypeDisplayName;
        private LocalDate startDate;
        private LocalDate endDate;
        private Report.ReportStatus status;
        private UUID generatedByUuid;
        private String filePath;
        private String description;
        private String parameters;
        private LocalDateTime completedAt;
        private String errorMessage;
        private Long generationTimeMs;

        // Constructors
        public ReportResponse() {}

        public ReportResponse(Report report) {
            this.uuid = report.getUuid();
            this.createdAt = report.getCreatedAt();
            this.reportName = report.getReportName();
            this.reportType = report.getReportType();
            this.reportTypeDisplayName = report.getReportTypeDisplayName();
            this.periodType = report.getPeriodType();
            this.periodTypeDisplayName = report.getPeriodTypeDisplayName();
            this.startDate = report.getStartDate();
            this.endDate = report.getEndDate();
            this.status = report.getStatus();
            this.generatedByUuid = report.getGeneratedByUuid();
            this.filePath = report.getFilePath();
            this.description = report.getDescription();
            this.parameters = report.getParameters();
            this.completedAt = report.getCompletedAt();
            this.errorMessage = report.getErrorMessage();
            this.generationTimeMs = report.getGenerationTime();
        }

        // Getters and Setters
        public UUID getUuid() {
            return uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public String getReportName() {
            return reportName;
        }

        public void setReportName(String reportName) {
            this.reportName = reportName;
        }

        public Report.ReportType getReportType() {
            return reportType;
        }

        public void setReportType(Report.ReportType reportType) {
            this.reportType = reportType;
        }

        public String getReportTypeDisplayName() {
            return reportTypeDisplayName;
        }

        public void setReportTypeDisplayName(String reportTypeDisplayName) {
            this.reportTypeDisplayName = reportTypeDisplayName;
        }

        public Report.PeriodType getPeriodType() {
            return periodType;
        }

        public void setPeriodType(Report.PeriodType periodType) {
            this.periodType = periodType;
        }

        public String getPeriodTypeDisplayName() {
            return periodTypeDisplayName;
        }

        public void setPeriodTypeDisplayName(String periodTypeDisplayName) {
            this.periodTypeDisplayName = periodTypeDisplayName;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public Report.ReportStatus getStatus() {
            return status;
        }

        public void setStatus(Report.ReportStatus status) {
            this.status = status;
        }

        public UUID getGeneratedByUuid() {
            return generatedByUuid;
        }

        public void setGeneratedByUuid(UUID generatedByUuid) {
            this.generatedByUuid = generatedByUuid;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getParameters() {
            return parameters;
        }

        public void setParameters(String parameters) {
            this.parameters = parameters;
        }

        public LocalDateTime getCompletedAt() {
            return completedAt;
        }

        public void setCompletedAt(LocalDateTime completedAt) {
            this.completedAt = completedAt;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public Long getGenerationTimeMs() {
            return generationTimeMs;
        }

        public void setGenerationTimeMs(Long generationTimeMs) {
            this.generationTimeMs = generationTimeMs;
        }
    }

    // 报表统计响应DTO
    public static class ReportStatsResponse {
        private Long totalReports;
        private Long pendingReports;
        private Long generatingReports;
        private Long completedReports;
        private Long failedReports;

        // 按类型统计
        private Long financialSummaryReports;
        private Long revenueAnalysisReports;
        private Long employeePerformanceReports;
        private Long customerAnalysisReports;
        private Long servicePopularityReports;
        private Long dailyCutoffReports;
        private Long monthlySummaryReports;
        private Long yearlySummaryReports;
        private Long customReports;

        // 性能统计
        private Double averageGenerationTimeMs;
        private Long longestGenerationTimeMs;
        private Long shortestGenerationTimeMs;
        private Double successRate;

        // Constructors
        public ReportStatsResponse() {}

        // Getters and Setters
        public Long getTotalReports() {
            return totalReports;
        }

        public void setTotalReports(Long totalReports) {
            this.totalReports = totalReports;
        }

        public Long getPendingReports() {
            return pendingReports;
        }

        public void setPendingReports(Long pendingReports) {
            this.pendingReports = pendingReports;
        }

        public Long getGeneratingReports() {
            return generatingReports;
        }

        public void setGeneratingReports(Long generatingReports) {
            this.generatingReports = generatingReports;
        }

        public Long getCompletedReports() {
            return completedReports;
        }

        public void setCompletedReports(Long completedReports) {
            this.completedReports = completedReports;
        }

        public Long getFailedReports() {
            return failedReports;
        }

        public void setFailedReports(Long failedReports) {
            this.failedReports = failedReports;
        }

        public Long getFinancialSummaryReports() {
            return financialSummaryReports;
        }

        public void setFinancialSummaryReports(Long financialSummaryReports) {
            this.financialSummaryReports = financialSummaryReports;
        }

        public Long getRevenueAnalysisReports() {
            return revenueAnalysisReports;
        }

        public void setRevenueAnalysisReports(Long revenueAnalysisReports) {
            this.revenueAnalysisReports = revenueAnalysisReports;
        }

        public Long getEmployeePerformanceReports() {
            return employeePerformanceReports;
        }

        public void setEmployeePerformanceReports(Long employeePerformanceReports) {
            this.employeePerformanceReports = employeePerformanceReports;
        }

        public Long getCustomerAnalysisReports() {
            return customerAnalysisReports;
        }

        public void setCustomerAnalysisReports(Long customerAnalysisReports) {
            this.customerAnalysisReports = customerAnalysisReports;
        }

        public Long getServicePopularityReports() {
            return servicePopularityReports;
        }

        public void setServicePopularityReports(Long servicePopularityReports) {
            this.servicePopularityReports = servicePopularityReports;
        }

        public Long getDailyCutoffReports() {
            return dailyCutoffReports;
        }

        public void setDailyCutoffReports(Long dailyCutoffReports) {
            this.dailyCutoffReports = dailyCutoffReports;
        }

        public Long getMonthlySummaryReports() {
            return monthlySummaryReports;
        }

        public void setMonthlySummaryReports(Long monthlySummaryReports) {
            this.monthlySummaryReports = monthlySummaryReports;
        }

        public Long getYearlySummaryReports() {
            return yearlySummaryReports;
        }

        public void setYearlySummaryReports(Long yearlySummaryReports) {
            this.yearlySummaryReports = yearlySummaryReports;
        }

        public Long getCustomReports() {
            return customReports;
        }

        public void setCustomReports(Long customReports) {
            this.customReports = customReports;
        }

        public Double getAverageGenerationTimeMs() {
            return averageGenerationTimeMs;
        }

        public void setAverageGenerationTimeMs(Double averageGenerationTimeMs) {
            this.averageGenerationTimeMs = averageGenerationTimeMs;
        }

        public Long getLongestGenerationTimeMs() {
            return longestGenerationTimeMs;
        }

        public void setLongestGenerationTimeMs(Long longestGenerationTimeMs) {
            this.longestGenerationTimeMs = longestGenerationTimeMs;
        }

        public Long getShortestGenerationTimeMs() {
            return shortestGenerationTimeMs;
        }

        public void setShortestGenerationTimeMs(Long shortestGenerationTimeMs) {
            this.shortestGenerationTimeMs = shortestGenerationTimeMs;
        }

        public Double getSuccessRate() {
            return successRate;
        }

        public void setSuccessRate(Double successRate) {
            this.successRate = successRate;
        }
    }

    // 财务汇总报表数据DTO
    public static class FinancialSummaryReportData {
        private LocalDate startDate;
        private LocalDate endDate;
        private BigDecimal totalIncome;
        private BigDecimal totalExpense;
        private BigDecimal netProfit;
        private BigDecimal profitMargin;
        private Long totalTransactions;
        private List<PaymentMethodSummary> paymentMethodBreakdown;
        private List<CategoryBreakdown> incomeBreakdown;
        private List<CategoryBreakdown> expenseBreakdown;

        // Constructors
        public FinancialSummaryReportData() {}

        // Getters and Setters
        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public BigDecimal getTotalIncome() {
            return totalIncome;
        }

        public void setTotalIncome(BigDecimal totalIncome) {
            this.totalIncome = totalIncome;
        }

        public BigDecimal getTotalExpense() {
            return totalExpense;
        }

        public void setTotalExpense(BigDecimal totalExpense) {
            this.totalExpense = totalExpense;
        }

        public BigDecimal getNetProfit() {
            return netProfit;
        }

        public void setNetProfit(BigDecimal netProfit) {
            this.netProfit = netProfit;
        }

        public BigDecimal getProfitMargin() {
            return profitMargin;
        }

        public void setProfitMargin(BigDecimal profitMargin) {
            this.profitMargin = profitMargin;
        }

        public Long getTotalTransactions() {
            return totalTransactions;
        }

        public void setTotalTransactions(Long totalTransactions) {
            this.totalTransactions = totalTransactions;
        }

        public List<PaymentMethodSummary> getPaymentMethodBreakdown() {
            return paymentMethodBreakdown;
        }

        public void setPaymentMethodBreakdown(List<PaymentMethodSummary> paymentMethodBreakdown) {
            this.paymentMethodBreakdown = paymentMethodBreakdown;
        }

        public List<CategoryBreakdown> getIncomeBreakdown() {
            return incomeBreakdown;
        }

        public void setIncomeBreakdown(List<CategoryBreakdown> incomeBreakdown) {
            this.incomeBreakdown = incomeBreakdown;
        }

        public List<CategoryBreakdown> getExpenseBreakdown() {
            return expenseBreakdown;
        }

        public void setExpenseBreakdown(List<CategoryBreakdown> expenseBreakdown) {
            this.expenseBreakdown = expenseBreakdown;
        }
    }

    // 支付方式汇总
    public static class PaymentMethodSummary {
        private String paymentMethod;
        private BigDecimal amount;
        private Long transactionCount;
        private Double percentage;

        public PaymentMethodSummary() {}

        public PaymentMethodSummary(String paymentMethod, BigDecimal amount, Long transactionCount, Double percentage) {
            this.paymentMethod = paymentMethod;
            this.amount = amount;
            this.transactionCount = transactionCount;
            this.percentage = percentage;
        }

        // Getters and Setters
        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public Long getTransactionCount() {
            return transactionCount;
        }

        public void setTransactionCount(Long transactionCount) {
            this.transactionCount = transactionCount;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }
    }

    // 分类汇总
    public static class CategoryBreakdown {
        private String category;
        private String categoryDisplayName;
        private BigDecimal amount;
        private Long transactionCount;
        private Double percentage;

        public CategoryBreakdown() {}

        public CategoryBreakdown(String category, String categoryDisplayName, BigDecimal amount,
                                 Long transactionCount, Double percentage) {
            this.category = category;
            this.categoryDisplayName = categoryDisplayName;
            this.amount = amount;
            this.transactionCount = transactionCount;
            this.percentage = percentage;
        }

        // Getters and Setters
        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCategoryDisplayName() {
            return categoryDisplayName;
        }

        public void setCategoryDisplayName(String categoryDisplayName) {
            this.categoryDisplayName = categoryDisplayName;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public Long getTransactionCount() {
            return transactionCount;
        }

        public void setTransactionCount(Long transactionCount) {
            this.transactionCount = transactionCount;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }
    }
}