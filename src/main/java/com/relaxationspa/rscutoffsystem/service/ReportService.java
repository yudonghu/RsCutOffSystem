// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/service/ReportService.java
package com.relaxationspa.rscutoffsystem.service;

import com.relaxationspa.rscutoffsystem.dto.ReportDTO;
import com.relaxationspa.rscutoffsystem.entity.Report;
import com.relaxationspa.rscutoffsystem.entity.Transaction;
import com.relaxationspa.rscutoffsystem.entity.TransactionItem;
import com.relaxationspa.rscutoffsystem.exception.ResourceNotFoundException;
import com.relaxationspa.rscutoffsystem.exception.ValidationException;
import com.relaxationspa.rscutoffsystem.repository.ReportRepository;
import com.relaxationspa.rscutoffsystem.repository.TransactionRepository;
import com.relaxationspa.rscutoffsystem.repository.CustomerRepository;
import com.relaxationspa.rscutoffsystem.repository.ServiceRepository;
import com.relaxationspa.rscutoffsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 创建新报表
     */
    public ReportDTO.ReportResponse createReport(ReportDTO.CreateReportRequest request) {
        // 验证日期范围
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new ValidationException("结束日期不能早于开始日期");
        }

        // 检查是否存在重复的报表
        List<Report> duplicates = reportRepository.findDuplicateReports(
                request.getReportType(),
                request.getPeriodType(),
                request.getStartDate(),
                request.getEndDate()
        );

        if (!duplicates.isEmpty()) {
            throw new ValidationException("已存在相同类型和日期范围的报表");
        }

        // 创建报表实体
        Report report = new Report();
        report.setReportName(request.getReportName());
        report.setReportType(request.getReportType());
        report.setPeriodType(request.getPeriodType());
        report.setStartDate(request.getStartDate());
        report.setEndDate(request.getEndDate());
        report.setGeneratedByUuid(request.getGeneratedByUuid());
        report.setDescription(request.getDescription());
        report.setParameters(request.getParameters());
        report.setStatus(Report.ReportStatus.PENDING);

        Report savedReport = reportRepository.save(report);

        // 异步生成报表
        generateReportAsync(savedReport.getUuid());

        return new ReportDTO.ReportResponse(savedReport);
    }

    /**
     * 根据UUID获取报表
     */
    @Transactional(readOnly = true)
    public ReportDTO.ReportResponse getReportByUuid(UUID uuid) {
        Report report = reportRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("报表未找到: " + uuid));
        return new ReportDTO.ReportResponse(report);
    }

    /**
     * 获取所有报表（分页）
     */
    @Transactional(readOnly = true)
    public Page<ReportDTO.ReportResponse> getAllReports(Pageable pageable) {
        return reportRepository.findAll(pageable)
                .map(ReportDTO.ReportResponse::new);
    }

    /**
     * 根据报表类型获取报表
     */
    @Transactional(readOnly = true)
    public List<ReportDTO.ReportResponse> getReportsByType(Report.ReportType reportType) {
        return reportRepository.findByReportType(reportType).stream()
                .map(ReportDTO.ReportResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 根据报表状态获取报表
     */
    @Transactional(readOnly = true)
    public List<ReportDTO.ReportResponse> getReportsByStatus(Report.ReportStatus status) {
        return reportRepository.findByStatus(status).stream()
                .map(ReportDTO.ReportResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 根据生成者获取报表
     */
    @Transactional(readOnly = true)
    public List<ReportDTO.ReportResponse> getReportsByUser(UUID userUuid) {
        return reportRepository.findReportsByUser(userUuid).stream()
                .map(ReportDTO.ReportResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取待生成的报表
     */
    @Transactional(readOnly = true)
    public List<ReportDTO.ReportResponse> getPendingReports() {
        return reportRepository.findAllPendingReports().stream()
                .map(ReportDTO.ReportResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取已完成的报表
     */
    @Transactional(readOnly = true)
    public List<ReportDTO.ReportResponse> getCompletedReports() {
        return reportRepository.findAllCompletedReports().stream()
                .map(ReportDTO.ReportResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取失败的报表
     */
    @Transactional(readOnly = true)
    public List<ReportDTO.ReportResponse> getFailedReports() {
        return reportRepository.findAllFailedReports().stream()
                .map(ReportDTO.ReportResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 搜索报表
     */
    @Transactional(readOnly = true)
    public List<ReportDTO.ReportResponse> searchReports(String keyword) {
        return reportRepository.findByReportNameContainingIgnoreCase(keyword).stream()
                .map(ReportDTO.ReportResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取今日报表
     */
    @Transactional(readOnly = true)
    public List<ReportDTO.ReportResponse> getTodayReports() {
        return reportRepository.findTodayReports().stream()
                .map(ReportDTO.ReportResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取本周报表
     */
    @Transactional(readOnly = true)
    public List<ReportDTO.ReportResponse> getThisWeekReports() {
        return reportRepository.findThisWeekReports().stream()
                .map(ReportDTO.ReportResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取本月报表
     */
    @Transactional(readOnly = true)
    public List<ReportDTO.ReportResponse> getThisMonthReports() {
        return reportRepository.findThisMonthReports().stream()
                .map(ReportDTO.ReportResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 更新报表
     */
    public ReportDTO.ReportResponse updateReport(UUID uuid, ReportDTO.UpdateReportRequest request) {
        Report report = reportRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("报表未找到: " + uuid));

        // 更新基本信息
        if (request.getReportName() != null) {
            report.setReportName(request.getReportName());
        }

        if (request.getDescription() != null) {
            report.setDescription(request.getDescription());
        }

        if (request.getParameters() != null) {
            report.setParameters(request.getParameters());
        }

        if (request.getStatus() != null) {
            report.setStatus(request.getStatus());
            if (request.getStatus() == Report.ReportStatus.COMPLETED) {
                report.setCompletedAt(LocalDateTime.now());
            }
        }

        if (request.getFilePath() != null) {
            report.setFilePath(request.getFilePath());
        }

        if (request.getErrorMessage() != null) {
            report.setErrorMessage(request.getErrorMessage());
        }

        Report updatedReport = reportRepository.save(report);
        return new ReportDTO.ReportResponse(updatedReport);
    }

    /**
     * 删除报表
     */
    public void deleteReport(UUID uuid) {
        if (!reportRepository.existsById(uuid)) {
            throw new ResourceNotFoundException("报表未找到: " + uuid);
        }
        reportRepository.deleteById(uuid);
    }

    /**
     * 重新生成报表
     */
    public ReportDTO.ReportResponse regenerateReport(UUID uuid) {
        Report report = reportRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("报表未找到: " + uuid));

        // 重置报表状态
        report.setStatus(Report.ReportStatus.PENDING);
        report.setFilePath(null);
        report.setErrorMessage(null);
        report.setCompletedAt(null);

        Report savedReport = reportRepository.save(report);

        // 异步生成报表
        generateReportAsync(savedReport.getUuid());

        return new ReportDTO.ReportResponse(savedReport);
    }

    /**
     * 异步生成报表
     */
    @Async
    public void generateReportAsync(UUID reportUuid) {
        try {
            Report report = reportRepository.findById(reportUuid)
                    .orElseThrow(() -> new ResourceNotFoundException("报表未找到: " + reportUuid));

            // 更新状态为生成中
            report.setStatus(Report.ReportStatus.GENERATING);
            reportRepository.save(report);

            // 根据报表类型生成报表
            String filePath = generateReportFile(report);

            // 更新状态为已完成
            report.setStatus(Report.ReportStatus.COMPLETED);
            report.setFilePath(filePath);
            report.setCompletedAt(LocalDateTime.now());
            reportRepository.save(report);

        } catch (Exception e) {
            // 更新状态为失败
            Report report = reportRepository.findById(reportUuid).orElse(null);
            if (report != null) {
                report.setStatus(Report.ReportStatus.FAILED);
                report.setErrorMessage(e.getMessage());
                reportRepository.save(report);
            }
        }
    }

    /**
     * 生成报表文件
     */
    private String generateReportFile(Report report) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = String.format("%s_%s_%s.json",
                report.getReportType().name(),
                report.getPeriodType().name(),
                timestamp);

        String filePath = "/reports/" + fileName;

        // 根据报表类型生成不同的报表数据
        Object reportData = generateReportData(report);

        // 这里应该将reportData保存到文件系统或数据库
        // 为了演示，我们只返回文件路径
        return filePath;
    }

    /**
     * 根据报表类型生成报表数据
     */
    private Object generateReportData(Report report) {
        switch (report.getReportType()) {
            case FINANCIAL_SUMMARY:
                return generateFinancialSummaryReport(report.getStartDate(), report.getEndDate());
            case REVENUE_ANALYSIS:
                return generateRevenueAnalysisReport(report.getStartDate(), report.getEndDate());
            case CUSTOMER_ANALYSIS:
                return generateCustomerAnalysisReport(report.getStartDate(), report.getEndDate());
            case SERVICE_POPULARITY:
                return generateServicePopularityReport(report.getStartDate(), report.getEndDate());
            case DAILY_CUTOFF:
                return generateDailyCutoffReport(report.getStartDate());
            case MONTHLY_SUMMARY:
                return generateMonthlySummaryReport(report.getStartDate());
            case YEARLY_SUMMARY:
                return generateYearlySummaryReport(report.getStartDate());
            default:
                return generateCustomReport(report);
        }
    }

    /**
     * 生成财务汇总报表
     */
    public ReportDTO.FinancialSummaryReportData generateFinancialSummaryReport(LocalDate startDate, LocalDate endDate) {
        ReportDTO.FinancialSummaryReportData reportData = new ReportDTO.FinancialSummaryReportData();
        reportData.setStartDate(startDate);
        reportData.setEndDate(endDate);

        // 计算总收入和支出
        BigDecimal totalIncome = transactionRepository.calculateIncomeForPeriod(startDate, endDate);
        BigDecimal totalExpense = transactionRepository.calculateExpenseForPeriod(startDate, endDate);
        BigDecimal netProfit = totalIncome.subtract(totalExpense);

        reportData.setTotalIncome(totalIncome);
        reportData.setTotalExpense(totalExpense);
        reportData.setNetProfit(netProfit);

        // 计算利润率
        if (totalIncome.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal profitMargin = netProfit.divide(totalIncome, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            reportData.setProfitMargin(profitMargin);
        } else {
            reportData.setProfitMargin(BigDecimal.ZERO);
        }

        // 统计交易数量
        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(startDate, endDate);
        reportData.setTotalTransactions((long) transactions.size());

        // 支付方式汇总
        Map<Transaction.PaymentMethod, List<Transaction>> paymentGroups = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getPaymentMethod));

        List<ReportDTO.PaymentMethodSummary> paymentSummaries = new ArrayList<>();
        for (Map.Entry<Transaction.PaymentMethod, List<Transaction>> entry : paymentGroups.entrySet()) {
            BigDecimal amount = entry.getValue().stream()
                    .map(Transaction::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            double percentage = totalIncome.compareTo(BigDecimal.ZERO) > 0 ?
                    amount.divide(totalIncome, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue() : 0.0;

            paymentSummaries.add(new ReportDTO.PaymentMethodSummary(
                    entry.getKey().name(), amount, (long) entry.getValue().size(), percentage));
        }
        reportData.setPaymentMethodBreakdown(paymentSummaries);

        // 收入和支出分类汇总
        List<TransactionItem> allItems = transactions.stream()
                .flatMap(t -> t.getTransactionItems().stream())
                .collect(Collectors.toList());

        // 收入分类汇总
        List<ReportDTO.CategoryBreakdown> incomeBreakdown = generateCategoryBreakdown(allItems, true, totalIncome);
        reportData.setIncomeBreakdown(incomeBreakdown);

        // 支出分类汇总  
        List<ReportDTO.CategoryBreakdown> expenseBreakdown = generateCategoryBreakdown(allItems, false, totalExpense);
        reportData.setExpenseBreakdown(expenseBreakdown);

        return reportData;
    }

    /**
     * 生成分类汇总
     */
    private List<ReportDTO.CategoryBreakdown> generateCategoryBreakdown(
            List<TransactionItem> items, boolean isIncome, BigDecimal total) {

        Map<TransactionItem.TransactionCategory, List<TransactionItem>> categoryGroups = items.stream()
                .filter(item -> item.isIncomeCategory() == isIncome)
                .collect(Collectors.groupingBy(TransactionItem::getCategory));

        List<ReportDTO.CategoryBreakdown> breakdown = new ArrayList<>();
        for (Map.Entry<TransactionItem.TransactionCategory, List<TransactionItem>> entry : categoryGroups.entrySet()) {
            BigDecimal amount = entry.getValue().stream()
                    .map(TransactionItem::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            double percentage = total.compareTo(BigDecimal.ZERO) > 0 ?
                    amount.divide(total, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue() : 0.0;

            breakdown.add(new ReportDTO.CategoryBreakdown(
                    entry.getKey().name(),
                    entry.getKey().name(), // 这里应该是显示名称
                    amount,
                    (long) entry.getValue().size(),
                    percentage));
        }

        return breakdown.stream()
                .sorted((a, b) -> b.getAmount().compareTo(a.getAmount()))
                .collect(Collectors.toList());
    }

    /**
     * 生成营收分析报表
     */
    private Object generateRevenueAnalysisReport(LocalDate startDate, LocalDate endDate) {
        // 实现营收分析逻辑
        Map<String, Object> data = new HashMap<>();
        data.put("type", "REVENUE_ANALYSIS");
        data.put("startDate", startDate);
        data.put("endDate", endDate);
        // 添加具体的营收分析数据
        return data;
    }

    /**
     * 生成客户分析报表
     */
    private Object generateCustomerAnalysisReport(LocalDate startDate, LocalDate endDate) {
        // 实现客户分析逻辑
        Map<String, Object> data = new HashMap<>();
        data.put("type", "CUSTOMER_ANALYSIS");
        data.put("startDate", startDate);
        data.put("endDate", endDate);
        // 添加具体的客户分析数据
        return data;
    }

    /**
     * 生成服务受欢迎度报表
     */
    private Object generateServicePopularityReport(LocalDate startDate, LocalDate endDate) {
        // 实现服务受欢迎度分析逻辑
        Map<String, Object> data = new HashMap<>();
        data.put("type", "SERVICE_POPULARITY");
        data.put("startDate", startDate);
        data.put("endDate", endDate);
        // 添加具体的服务分析数据
        return data;
    }

    /**
     * 生成日结报表
     */
    private Object generateDailyCutoffReport(LocalDate date) {
        // 实现日结报表逻辑
        Map<String, Object> data = new HashMap<>();
        data.put("type", "DAILY_CUTOFF");
        data.put("date", date);
        // 添加具体的日结数据
        return data;
    }

    /**
     * 生成月度汇总报表
     */
    private Object generateMonthlySummaryReport(LocalDate date) {
        // 实现月度汇总逻辑
        Map<String, Object> data = new HashMap<>();
        data.put("type", "MONTHLY_SUMMARY");
        data.put("month", date.getYear() + "-" + date.getMonthValue());
        // 添加具体的月度数据
        return data;
    }

    /**
     * 生成年度汇总报表
     */
    private Object generateYearlySummaryReport(LocalDate date) {
        // 实现年度汇总逻辑
        Map<String, Object> data = new HashMap<>();
        data.put("type", "YEARLY_SUMMARY");
        data.put("year", date.getYear());
        // 添加具体的年度数据
        return data;
    }

    /**
     * 生成自定义报表
     */
    private Object generateCustomReport(Report report) {
        // 实现自定义报表逻辑
        Map<String, Object> data = new HashMap<>();
        data.put("type", "CUSTOM");
        data.put("parameters", report.getParameters());
        // 根据参数生成自定义数据
        return data;
    }

    /**
     * 获取报表统计
     */
    @Transactional(readOnly = true)
    public ReportDTO.ReportStatsResponse getReportStats() {
        ReportDTO.ReportStatsResponse stats = new ReportDTO.ReportStatsResponse();

        // 总报表数
        stats.setTotalReports(reportRepository.count());

        // 按状态统计
        List<Object[]> statusStats = reportRepository.countByStatus();
        for (Object[] stat : statusStats) {
            Report.ReportStatus status = (Report.ReportStatus) stat[0];
            Long count = (Long) stat[1];

            switch (status) {
                case PENDING:
                    stats.setPendingReports(count);
                    break;
                case GENERATING:
                    stats.setGeneratingReports(count);
                    break;
                case COMPLETED:
                    stats.setCompletedReports(count);
                    break;
                case FAILED:
                    stats.setFailedReports(count);
                    break;
            }
        }

        // 按类型统计
        List<Object[]> typeStats = reportRepository.countByReportType();
        for (Object[] stat : typeStats) {
            Report.ReportType type = (Report.ReportType) stat[0];
            Long count = (Long) stat[1];

            switch (type) {
                case FINANCIAL_SUMMARY:
                    stats.setFinancialSummaryReports(count);
                    break;
                case REVENUE_ANALYSIS:
                    stats.setRevenueAnalysisReports(count);
                    break;
                case EMPLOYEE_PERFORMANCE:
                    stats.setEmployeePerformanceReports(count);
                    break;
                case CUSTOMER_ANALYSIS:
                    stats.setCustomerAnalysisReports(count);
                    break;
                case SERVICE_POPULARITY:
                    stats.setServicePopularityReports(count);
                    break;
                case DAILY_CUTOFF:
                    stats.setDailyCutoffReports(count);
                    break;
                case MONTHLY_SUMMARY:
                    stats.setMonthlySummaryReports(count);
                    break;
                case YEARLY_SUMMARY:
                    stats.setYearlySummaryReports(count);
                    break;
                case CUSTOM:
                    stats.setCustomReports(count);
                    break;
            }
        }

        // 性能统计
        Double avgTime = reportRepository.calculateAverageGenerationTime();
        stats.setAverageGenerationTimeMs(avgTime);

        reportRepository.findLongestGenerationTimeReport()
                .ifPresent(report -> stats.setLongestGenerationTimeMs(report.getGenerationTime()));

        reportRepository.findShortestGenerationTimeReport()
                .ifPresent(report -> stats.setShortestGenerationTimeMs(report.getGenerationTime()));

        // 计算成功率
        Long completed = stats.getCompletedReports() != null ? stats.getCompletedReports() : 0L;
        Long failed = stats.getFailedReports() != null ? stats.getFailedReports() : 0L;
        Long total = completed + failed;

        if (total > 0) {
            double successRate = (double) completed / total * 100;
            stats.setSuccessRate(successRate);
        } else {
            stats.setSuccessRate(0.0);
        }

        return stats;
    }

    /**
     * 检查报表是否存在
     */
    @Transactional(readOnly = true)
    public boolean reportExists(UUID uuid) {
        return reportRepository.existsById(uuid);
    }
}