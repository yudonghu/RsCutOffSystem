// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/controller/ReportController.java
package com.relaxationspa.rscutoffsystem.controller;

import com.relaxationspa.rscutoffsystem.dto.ReportDTO;
import com.relaxationspa.rscutoffsystem.entity.Report;
import com.relaxationspa.rscutoffsystem.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 创建新报表
     */
    @PostMapping
    public ResponseEntity<ReportDTO.ReportResponse> createReport(@Valid @RequestBody ReportDTO.CreateReportRequest request) {
        ReportDTO.ReportResponse response = reportService.createReport(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 获取所有报表（分页）
     */
    @GetMapping
    public ResponseEntity<Page<ReportDTO.ReportResponse>> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ReportDTO.ReportResponse> reports = reportService.getAllReports(pageable);

        return ResponseEntity.ok(reports);
    }

    /**
     * 根据UUID获取报表
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<ReportDTO.ReportResponse> getReportByUuid(@PathVariable UUID uuid) {
        ReportDTO.ReportResponse response = reportService.getReportByUuid(uuid);
        return ResponseEntity.ok(response);
    }

    /**
     * 根据报表类型获取报表
     */
    @GetMapping("/type/{reportType}")
    public ResponseEntity<List<ReportDTO.ReportResponse>> getReportsByType(@PathVariable Report.ReportType reportType) {
        List<ReportDTO.ReportResponse> reports = reportService.getReportsByType(reportType);
        return ResponseEntity.ok(reports);
    }

    /**
     * 根据报表状态获取报表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReportDTO.ReportResponse>> getReportsByStatus(@PathVariable Report.ReportStatus status) {
        List<ReportDTO.ReportResponse> reports = reportService.getReportsByStatus(status);
        return ResponseEntity.ok(reports);
    }

    /**
     * 根据生成者获取报表
     */
    @GetMapping("/user/{userUuid}")
    public ResponseEntity<List<ReportDTO.ReportResponse>> getReportsByUser(@PathVariable UUID userUuid) {
        List<ReportDTO.ReportResponse> reports = reportService.getReportsByUser(userUuid);
        return ResponseEntity.ok(reports);
    }

    /**
     * 获取待生成的报表
     */
    @GetMapping("/pending")
    public ResponseEntity<List<ReportDTO.ReportResponse>> getPendingReports() {
        List<ReportDTO.ReportResponse> reports = reportService.getPendingReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * 获取已完成的报表
     */
    @GetMapping("/completed")
    public ResponseEntity<List<ReportDTO.ReportResponse>> getCompletedReports() {
        List<ReportDTO.ReportResponse> reports = reportService.getCompletedReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * 获取失败的报表
     */
    @GetMapping("/failed")
    public ResponseEntity<List<ReportDTO.ReportResponse>> getFailedReports() {
        List<ReportDTO.ReportResponse> reports = reportService.getFailedReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * 搜索报表
     */
    @GetMapping("/search")
    public ResponseEntity<List<ReportDTO.ReportResponse>> searchReports(@RequestParam String keyword) {
        List<ReportDTO.ReportResponse> reports = reportService.searchReports(keyword);
        return ResponseEntity.ok(reports);
    }

    /**
     * 获取今日报表
     */
    @GetMapping("/today")
    public ResponseEntity<List<ReportDTO.ReportResponse>> getTodayReports() {
        List<ReportDTO.ReportResponse> reports = reportService.getTodayReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * 获取本周报表
     */
    @GetMapping("/this-week")
    public ResponseEntity<List<ReportDTO.ReportResponse>> getThisWeekReports() {
        List<ReportDTO.ReportResponse> reports = reportService.getThisWeekReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * 获取本月报表
     */
    @GetMapping("/this-month")
    public ResponseEntity<List<ReportDTO.ReportResponse>> getThisMonthReports() {
        List<ReportDTO.ReportResponse> reports = reportService.getThisMonthReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * 更新报表
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<ReportDTO.ReportResponse> updateReport(
            @PathVariable UUID uuid,
            @Valid @RequestBody ReportDTO.UpdateReportRequest request) {
        ReportDTO.ReportResponse response = reportService.updateReport(uuid, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 删除报表
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteReport(@PathVariable UUID uuid) {
        reportService.deleteReport(uuid);
        return ResponseEntity.noContent().build();
    }

    /**
     * 重新生成报表
     */
    @PostMapping("/{uuid}/regenerate")
    public ResponseEntity<ReportDTO.ReportResponse> regenerateReport(@PathVariable UUID uuid) {
        ReportDTO.ReportResponse response = reportService.regenerateReport(uuid);
        return ResponseEntity.ok(response);
    }

    /**
     * 生成财务汇总报表
     */
    @PostMapping("/financial-summary")
    public ResponseEntity<ReportDTO.FinancialSummaryReportData> generateFinancialSummaryReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        ReportDTO.FinancialSummaryReportData reportData = reportService.generateFinancialSummaryReport(startDate, endDate);
        return ResponseEntity.ok(reportData);
    }

    /**
     * 生成今日财务汇总报表
     */
    @PostMapping("/financial-summary/today")
    public ResponseEntity<ReportDTO.FinancialSummaryReportData> generateTodayFinancialSummaryReport() {
        LocalDate today = LocalDate.now();
        ReportDTO.FinancialSummaryReportData reportData = reportService.generateFinancialSummaryReport(today, today);
        return ResponseEntity.ok(reportData);
    }

    /**
     * 生成本周财务汇总报表
     */
    @PostMapping("/financial-summary/this-week")
    public ResponseEntity<ReportDTO.FinancialSummaryReportData> generateThisWeekFinancialSummaryReport() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        ReportDTO.FinancialSummaryReportData reportData = reportService.generateFinancialSummaryReport(startOfWeek, today);
        return ResponseEntity.ok(reportData);
    }

    /**
     * 生成本月财务汇总报表
     */
    @PostMapping("/financial-summary/this-month")
    public ResponseEntity<ReportDTO.FinancialSummaryReportData> generateThisMonthFinancialSummaryReport() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        ReportDTO.FinancialSummaryReportData reportData = reportService.generateFinancialSummaryReport(startOfMonth, today);
        return ResponseEntity.ok(reportData);
    }

    /**
     * 生成本年财务汇总报表
     */
    @PostMapping("/financial-summary/this-year")
    public ResponseEntity<ReportDTO.FinancialSummaryReportData> generateThisYearFinancialSummaryReport() {
        LocalDate today = LocalDate.now();
        LocalDate startOfYear = today.withDayOfYear(1);
        ReportDTO.FinancialSummaryReportData reportData = reportService.generateFinancialSummaryReport(startOfYear, today);
        return ResponseEntity.ok(reportData);
    }

    /**
     * 快速创建财务汇总报表
     */
    @PostMapping("/quick/financial-summary")
    public ResponseEntity<ReportDTO.ReportResponse> createQuickFinancialSummaryReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam UUID generatedByUuid) {

        ReportDTO.CreateReportRequest request = new ReportDTO.CreateReportRequest();
        request.setReportName("财务汇总报表 " + startDate + " 至 " + endDate);
        request.setReportType(Report.ReportType.FINANCIAL_SUMMARY);
        request.setPeriodType(Report.PeriodType.CUSTOM);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setGeneratedByUuid(generatedByUuid);
        request.setDescription("快速生成的财务汇总报表");

        ReportDTO.ReportResponse response = reportService.createReport(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 快速创建日结报表
     */
    @PostMapping("/quick/daily-cutoff")
    public ResponseEntity<ReportDTO.ReportResponse> createQuickDailyCutoffReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam UUID generatedByUuid) {

        ReportDTO.CreateReportRequest request = new ReportDTO.CreateReportRequest();
        request.setReportName("日结报表 " + date);
        request.setReportType(Report.ReportType.DAILY_CUTOFF);
        request.setPeriodType(Report.PeriodType.DAILY);
        request.setStartDate(date);
        request.setEndDate(date);
        request.setGeneratedByUuid(generatedByUuid);
        request.setDescription("快速生成的日结报表");

        ReportDTO.ReportResponse response = reportService.createReport(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 快速创建月度汇总报表
     */
    @PostMapping("/quick/monthly-summary")
    public ResponseEntity<ReportDTO.ReportResponse> createQuickMonthlySummaryReport(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam UUID generatedByUuid) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        ReportDTO.CreateReportRequest request = new ReportDTO.CreateReportRequest();
        request.setReportName("月度汇总报表 " + year + "年" + month + "月");
        request.setReportType(Report.ReportType.MONTHLY_SUMMARY);
        request.setPeriodType(Report.PeriodType.MONTHLY);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setGeneratedByUuid(generatedByUuid);
        request.setDescription("快速生成的月度汇总报表");

        ReportDTO.ReportResponse response = reportService.createReport(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 快速创建年度汇总报表
     */
    @PostMapping("/quick/yearly-summary")
    public ResponseEntity<ReportDTO.ReportResponse> createQuickYearlySummaryReport(
            @RequestParam int year,
            @RequestParam UUID generatedByUuid) {

        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        ReportDTO.CreateReportRequest request = new ReportDTO.CreateReportRequest();
        request.setReportName("年度汇总报表 " + year + "年");
        request.setReportType(Report.ReportType.YEARLY_SUMMARY);
        request.setPeriodType(Report.PeriodType.YEARLY);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setGeneratedByUuid(generatedByUuid);
        request.setDescription("快速生成的年度汇总报表");

        ReportDTO.ReportResponse response = reportService.createReport(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 获取报表统计
     */
    @GetMapping("/stats")
    public ResponseEntity<ReportDTO.ReportStatsResponse> getReportStats() {
        ReportDTO.ReportStatsResponse stats = reportService.getReportStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * 检查报表是否存在
     */
    @GetMapping("/{uuid}/exists")
    public ResponseEntity<Boolean> reportExists(@PathVariable UUID uuid) {
        boolean exists = reportService.reportExists(uuid);
        return ResponseEntity.ok(exists);
    }

    /**
     * 获取报表类型列表
     */
    @GetMapping("/types")
    public ResponseEntity<Report.ReportType[]> getReportTypes() {
        return ResponseEntity.ok(Report.ReportType.values());
    }

    /**
     * 获取周期类型列表
     */
    @GetMapping("/periods")
    public ResponseEntity<Report.PeriodType[]> getPeriodTypes() {
        return ResponseEntity.ok(Report.PeriodType.values());
    }

}