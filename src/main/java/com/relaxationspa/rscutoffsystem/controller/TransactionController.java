// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/controller/TransactionController.java
package com.relaxationspa.rscutoffsystem.controller;

import com.relaxationspa.rscutoffsystem.dto.TransactionDTO;
import com.relaxationspa.rscutoffsystem.entity.Transaction;
import com.relaxationspa.rscutoffsystem.service.TransactionService;
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
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * 创建新交易
     */
    @PostMapping
    public ResponseEntity<TransactionDTO.TransactionResponse> createTransaction(@Valid @RequestBody TransactionDTO.CreateTransactionRequest request) {
        TransactionDTO.TransactionResponse response = transactionService.createTransaction(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 获取所有交易（分页）
     */
    @GetMapping
    public ResponseEntity<Page<TransactionDTO.TransactionResponse>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<TransactionDTO.TransactionResponse> transactions = transactionService.getAllTransactions(pageable);

        return ResponseEntity.ok(transactions);
    }

    /**
     * 根据UUID获取交易
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<TransactionDTO.TransactionResponse> getTransactionByUuid(@PathVariable UUID uuid) {
        TransactionDTO.TransactionResponse response = transactionService.getTransactionByUuid(uuid);
        return ResponseEntity.ok(response);
    }

    /**
     * 根据交易编号获取交易
     */
    @GetMapping("/number/{transactionNumber}")
    public ResponseEntity<TransactionDTO.TransactionResponse> getTransactionByNumber(@PathVariable String transactionNumber) {
        TransactionDTO.TransactionResponse response = transactionService.getTransactionByNumber(transactionNumber);
        return ResponseEntity.ok(response);
    }

    /**
     * 根据交易类型获取交易
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<TransactionDTO.TransactionResponse>> getTransactionsByType(@PathVariable Transaction.TransactionType type) {
        List<TransactionDTO.TransactionResponse> transactions = transactionService.getTransactionsByType(type);
        return ResponseEntity.ok(transactions);
    }

    /**
     * 根据交易状态获取交易
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TransactionDTO.TransactionResponse>> getTransactionsByStatus(@PathVariable Transaction.TransactionStatus status) {
        List<TransactionDTO.TransactionResponse> transactions = transactionService.getTransactionsByStatus(status);
        return ResponseEntity.ok(transactions);
    }

    /**
     * 根据交易日期获取交易
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<TransactionDTO.TransactionResponse>> getTransactionsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TransactionDTO.TransactionResponse> transactions = transactionService.getTransactionsByDate(date);
        return ResponseEntity.ok(transactions);
    }

    /**
     * 根据日期范围获取交易
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<TransactionDTO.TransactionResponse>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TransactionDTO.TransactionResponse> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    /**
     * 获取收入交易
     */
    @GetMapping("/income")
    public ResponseEntity<List<TransactionDTO.TransactionResponse>> getIncomeTransactions() {
        List<TransactionDTO.TransactionResponse> transactions = transactionService.getTransactionsByType(Transaction.TransactionType.INCOME);
        return ResponseEntity.ok(transactions);
    }

    /**
     * 获取支出交易
     */
    @GetMapping("/expense")
    public ResponseEntity<List<TransactionDTO.TransactionResponse>> getExpenseTransactions() {
        List<TransactionDTO.TransactionResponse> transactions = transactionService.getTransactionsByType(Transaction.TransactionType.EXPENSE);
        return ResponseEntity.ok(transactions);
    }

    /**
     * 获取待确认的交易
     */
    @GetMapping("/pending")
    public ResponseEntity<List<TransactionDTO.TransactionResponse>> getPendingTransactions() {
        List<TransactionDTO.TransactionResponse> transactions = transactionService.getPendingTransactions();
        return ResponseEntity.ok(transactions);
    }

    /**
     * 获取未处理的交易（未包含在日结中）
     */
    @GetMapping("/unprocessed")
    public ResponseEntity<List<TransactionDTO.TransactionResponse>> getUnprocessedTransactions() {
        List<TransactionDTO.TransactionResponse> transactions = transactionService.getUnprocessedTransactions();
        return ResponseEntity.ok(transactions);
    }

    /**
     * 根据客户获取交易历史
     */
    @GetMapping("/customer/{customerUuid}")
    public ResponseEntity<List<TransactionDTO.TransactionResponse>> getCustomerTransactionHistory(@PathVariable UUID customerUuid) {
        List<TransactionDTO.TransactionResponse> transactions = transactionService.getCustomerTransactionHistory(customerUuid);
        return ResponseEntity.ok(transactions);
    }

    /**
     * 根据员工获取交易历史
     */
    @GetMapping("/user/{userUuid}")
    public ResponseEntity<List<TransactionDTO.TransactionResponse>> getUserTransactionHistory(@PathVariable UUID userUuid) {
        List<TransactionDTO.TransactionResponse> transactions = transactionService.getUserTransactionHistory(userUuid);
        return ResponseEntity.ok(transactions);
    }

    /**
     * 更新交易
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<TransactionDTO.TransactionResponse> updateTransaction(
            @PathVariable UUID uuid,
            @Valid @RequestBody TransactionDTO.UpdateTransactionRequest request) {
        TransactionDTO.TransactionResponse response = transactionService.updateTransaction(uuid, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 确认交易
     */
    @PostMapping("/{uuid}/confirm")
    public ResponseEntity<TransactionDTO.TransactionResponse> confirmTransaction(@PathVariable UUID uuid) {
        TransactionDTO.TransactionResponse response = transactionService.confirmTransaction(uuid);
        return ResponseEntity.ok(response);
    }

    /**
     * 取消交易
     */
    @PostMapping("/{uuid}/cancel")
    public ResponseEntity<TransactionDTO.TransactionResponse> cancelTransaction(@PathVariable UUID uuid) {
        TransactionDTO.TransactionResponse response = transactionService.cancelTransaction(uuid);
        return ResponseEntity.ok(response);
    }

    /**
     * 删除交易
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID uuid) {
        transactionService.deleteTransaction(uuid);
        return ResponseEntity.noContent().build();
    }

    /**
     * 执行日结
     */
    @PostMapping("/daily-cutoff")
    public ResponseEntity<TransactionDTO.DailyCutOffReportResponse> performDailyCutOff(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate cutOffDate) {
        TransactionDTO.DailyCutOffReportResponse report = transactionService.performDailyCutOff(cutOffDate);
        return ResponseEntity.ok(report);
    }

    /**
     * 获取日结报告
     */
    @GetMapping("/daily-cutoff/{cutOffDate}")
    public ResponseEntity<TransactionDTO.DailyCutOffReportResponse> getDailyCutOffReport(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate cutOffDate) {
        TransactionDTO.DailyCutOffReportResponse report = transactionService.getDailyCutOffReport(cutOffDate);
        return ResponseEntity.ok(report);
    }

    /**
     * 获取今日日结报告
     */
    @GetMapping("/daily-cutoff/today")
    public ResponseEntity<TransactionDTO.DailyCutOffReportResponse> getTodayDailyCutOffReport() {
        TransactionDTO.DailyCutOffReportResponse report = transactionService.getDailyCutOffReport(LocalDate.now());
        return ResponseEntity.ok(report);
    }

    /**
     * 获取交易统计
     */
    @GetMapping("/stats")
    public ResponseEntity<TransactionDTO.TransactionStatsResponse> getTransactionStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        TransactionDTO.TransactionStatsResponse stats = transactionService.getTransactionStats(startDate, endDate);
        return ResponseEntity.ok(stats);
    }

    /**
     * 获取今日交易统计
     */
    @GetMapping("/stats/today")
    public ResponseEntity<TransactionDTO.TransactionStatsResponse> getTodayTransactionStats() {
        LocalDate today = LocalDate.now();
        TransactionDTO.TransactionStatsResponse stats = transactionService.getTransactionStats(today, today);
        return ResponseEntity.ok(stats);
    }

    /**
     * 获取本周交易统计
     */
    @GetMapping("/stats/this-week")
    public ResponseEntity<TransactionDTO.TransactionStatsResponse> getThisWeekTransactionStats() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        TransactionDTO.TransactionStatsResponse stats = transactionService.getTransactionStats(startOfWeek, today);
        return ResponseEntity.ok(stats);
    }

    /**
     * 获取本月交易统计
     */
    @GetMapping("/stats/this-month")
    public ResponseEntity<TransactionDTO.TransactionStatsResponse> getThisMonthTransactionStats() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        TransactionDTO.TransactionStatsResponse stats = transactionService.getTransactionStats(startOfMonth, today);
        return ResponseEntity.ok(stats);
    }

    /**
     * 检查交易是否存在
     */
    @GetMapping("/{uuid}/exists")
    public ResponseEntity<Boolean> transactionExists(@PathVariable UUID uuid) {
        boolean exists = transactionService.transactionExists(uuid);
        return ResponseEntity.ok(exists);
    }
}