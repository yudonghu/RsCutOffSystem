// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/entity/Report.java
package com.relaxationspa.rscutoffsystem.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "report_name", nullable = false)
    private String reportName;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false)
    private ReportType reportType;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false)
    private PeriodType periodType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReportStatus status = ReportStatus.PENDING;

    @Column(name = "generated_by_uuid", nullable = false)
    private UUID generatedByUuid;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "description")
    private String description;

    @Column(name = "parameters", columnDefinition = "TEXT")
    private String parameters;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "error_message")
    private String errorMessage;

    // 枚举定义
    public enum ReportType {
        FINANCIAL_SUMMARY,      // 财务汇总报表
        REVENUE_ANALYSIS,       // 营收分析报表
        EMPLOYEE_PERFORMANCE,   // 员工业绩报表
        CUSTOMER_ANALYSIS,      // 客户分析报表
        SERVICE_POPULARITY,     // 服务受欢迎度报表
        DAILY_CUTOFF,          // 日结报表
        MONTHLY_SUMMARY,       // 月度汇总报表
        YEARLY_SUMMARY,        // 年度汇总报表
        CUSTOM                 // 自定义报表
    }

    public enum PeriodType {
        DAILY,      // 日报
        WEEKLY,     // 周报
        MONTHLY,    // 月报
        QUARTERLY,  // 季报
        YEARLY,     // 年报
        CUSTOM      // 自定义期间
    }

    public enum ReportStatus {
        PENDING,    // 待生成
        GENERATING, // 生成中
        COMPLETED,  // 已完成
        FAILED      // 生成失败
    }

    // 构造函数
    public Report() {}

    public Report(String reportName, ReportType reportType, PeriodType periodType,
                  LocalDate startDate, LocalDate endDate, UUID generatedByUuid) {
        this.reportName = reportName;
        this.reportType = reportType;
        this.periodType = periodType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.generatedByUuid = generatedByUuid;
        this.status = ReportStatus.PENDING;
    }

    // 业务方法
    public String getReportTypeDisplayName() {
        return switch (reportType) {
            case FINANCIAL_SUMMARY -> "财务汇总报表";
            case REVENUE_ANALYSIS -> "营收分析报表";
            case EMPLOYEE_PERFORMANCE -> "员工业绩报表";
            case CUSTOMER_ANALYSIS -> "客户分析报表";
            case SERVICE_POPULARITY -> "服务受欢迎度报表";
            case DAILY_CUTOFF -> "日结报表";
            case MONTHLY_SUMMARY -> "月度汇总报表";
            case YEARLY_SUMMARY -> "年度汇总报表";
            case CUSTOM -> "自定义报表";
        };
    }

    public String getPeriodTypeDisplayName() {
        return switch (periodType) {
            case DAILY -> "日报";
            case WEEKLY -> "周报";
            case MONTHLY -> "月报";
            case QUARTERLY -> "季报";
            case YEARLY -> "年报";
            case CUSTOM -> "自定义期间";
        };
    }

    public boolean isCompleted() {
        return this.status == ReportStatus.COMPLETED;
    }

    public boolean isFailed() {
        return this.status == ReportStatus.FAILED;
    }

    public long getGenerationTime() {
        if (completedAt != null && createdAt != null) {
            return java.time.Duration.between(createdAt, completedAt).toMillis();
        }
        return 0;
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

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public void setPeriodType(PeriodType periodType) {
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

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
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
}