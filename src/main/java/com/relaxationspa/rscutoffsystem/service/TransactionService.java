// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/service/TransactionService.java
package com.relaxationspa.rscutoffsystem.service;

import com.relaxationspa.rscutoffsystem.dto.TransactionDTO;
import com.relaxationspa.rscutoffsystem.entity.Transaction;
import com.relaxationspa.rscutoffsystem.entity.TransactionItem;
import com.relaxationspa.rscutoffsystem.exception.ResourceNotFoundException;
import com.relaxationspa.rscutoffsystem.exception.ValidationException;
import com.relaxationspa.rscutoffsystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * 创建新交易
     */
    public TransactionDTO.TransactionResponse createTransaction(TransactionDTO.CreateTransactionRequest request) {
        // 验证交易明细
        validateTransactionItems(request.getItems(), request.getType());

        // 计算总金额
        BigDecimal totalAmount = request.getItems().stream()
                .map(TransactionDTO.TransactionItemRequest::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 创建交易实体
        Transaction transaction = new Transaction();
        transaction.setTransactionNumber(generateTransactionNumber(request.getTransactionDate()));
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setType(request.getType());
        transaction.setTotalAmount(totalAmount);
        transaction.setDescription(request.getDescription());
        transaction.setNotes(request.getNotes());
        transaction.setRelatedCustomerUuid(request.getRelatedCustomerUuid());
        transaction.setRelatedUserUuid(request.getRelatedUserUuid());
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setPaymentReference(request.getPaymentReference());
        transaction.setStatus(Transaction.TransactionStatus.PENDING);

        // 创建交易明细
        List<TransactionItem> items = new ArrayList<>();
        for (TransactionDTO.TransactionItemRequest itemRequest : request.getItems()) {
            TransactionItem item = new TransactionItem();
            item.setCategory(itemRequest.getCategory());
            item.setAmount(itemRequest.getAmount());
            item.setDescription(itemRequest.getDescription());
            item.setNotes(itemRequest.getNotes());
            item.setTransaction(transaction);
            items.add(item);
        }
        transaction.setTransactionItems(items);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return new TransactionDTO.TransactionResponse(savedTransaction);
    }

    /**
     * 根据UUID获取交易
     */
    @Transactional(readOnly = true)
    public TransactionDTO.TransactionResponse getTransactionByUuid(UUID uuid) {
        Transaction transaction = transactionRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("交易未找到: " + uuid));
        return new TransactionDTO.TransactionResponse(transaction);
    }

    /**
     * 根据交易编号获取交易
     */
    @Transactional(readOnly = true)
    public TransactionDTO.TransactionResponse getTransactionByNumber(String transactionNumber) {
        Transaction transaction = transactionRepository.findByTransactionNumber(transactionNumber)
                .orElseThrow(() -> new ResourceNotFoundException("交易未找到: " + transactionNumber));
        return new TransactionDTO.TransactionResponse(transaction);
    }

    /**
     * 获取所有交易（分页）
     */
    @Transactional(readOnly = true)
    public Page<TransactionDTO.TransactionResponse> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable)
                .map(TransactionDTO.TransactionResponse::new);
    }

    /**
     * 根据交易类型获取交易
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO.TransactionResponse> getTransactionsByType(Transaction.TransactionType type) {
        return transactionRepository.findByType(type).stream()
                .map(TransactionDTO.TransactionResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 根据交易状态获取交易
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO.TransactionResponse> getTransactionsByStatus(Transaction.TransactionStatus status) {
        return transactionRepository.findByStatus(status).stream()
                .map(TransactionDTO.TransactionResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 根据交易日期获取交易
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO.TransactionResponse> getTransactionsByDate(LocalDate date) {
        return transactionRepository.findByTransactionDate(date).stream()
                .map(TransactionDTO.TransactionResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 根据日期范围获取交易
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO.TransactionResponse> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByTransactionDateBetween(startDate, endDate).stream()
                .map(TransactionDTO.TransactionResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 根据客户获取交易历史
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO.TransactionResponse> getCustomerTransactionHistory(UUID customerUuid) {
        return transactionRepository.findCustomerTransactionHistory(customerUuid).stream()
                .map(TransactionDTO.TransactionResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 根据员工获取交易历史
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO.TransactionResponse> getUserTransactionHistory(UUID userUuid) {
        return transactionRepository.findUserTransactionHistory(userUuid).stream()
                .map(TransactionDTO.TransactionResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取待确认的交易
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO.TransactionResponse> getPendingTransactions() {
        return transactionRepository.findAllPendingTransactions().stream()
                .map(TransactionDTO.TransactionResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取未处理的交易（未包含在日结中）
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO.TransactionResponse> getUnprocessedTransactions() {
        return transactionRepository.findUnprocessedTransactions().stream()
                .map(TransactionDTO.TransactionResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 更新交易
     */
    public TransactionDTO.TransactionResponse updateTransaction(UUID uuid, TransactionDTO.UpdateTransactionRequest request) {
        Transaction transaction = transactionRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("交易未找到: " + uuid));

        // 检查是否可以修改
        if (!transaction.canBeModified()) {
            throw new ValidationException("已取消或已包含在日结中的交易不能修改");
        }

        // 更新基本信息
        if (request.getTransactionDate() != null) {
            transaction.setTransactionDate(request.getTransactionDate());
        }

        if (request.getDescription() != null) {
            transaction.setDescription(request.getDescription());
        }

        if (request.getNotes() != null) {
            transaction.setNotes(request.getNotes());
        }

        if (request.getRelatedCustomerUuid() != null) {
            transaction.setRelatedCustomerUuid(request.getRelatedCustomerUuid());
        }

        if (request.getPaymentMethod() != null) {
            transaction.setPaymentMethod(request.getPaymentMethod());
        }

        if (request.getPaymentReference() != null) {
            transaction.setPaymentReference(request.getPaymentReference());
        }

        if (request.getStatus() != null) {
            transaction.setStatus(request.getStatus());
        }

        // 更新交易明细
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            validateTransactionItems(request.getItems(), transaction.getType());

            // 清除原有明细
            transaction.getTransactionItems().clear();

            // 添加新明细
            for (TransactionDTO.TransactionItemRequest itemRequest : request.getItems()) {
                TransactionItem item = new TransactionItem();
                item.setCategory(itemRequest.getCategory());
                item.setAmount(itemRequest.getAmount());
                item.setDescription(itemRequest.getDescription());
                item.setNotes(itemRequest.getNotes());
                transaction.addTransactionItem(item);
            }

            // 重新计算总金额
            BigDecimal newTotal = transaction.calculateTotalFromItems();
            transaction.setTotalAmount(newTotal);
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return new TransactionDTO.TransactionResponse(updatedTransaction);
    }

    /**
     * 确认交易
     */
    public TransactionDTO.TransactionResponse confirmTransaction(UUID uuid) {
        Transaction transaction = transactionRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("交易未找到: " + uuid));

        if (transaction.getStatus() != Transaction.TransactionStatus.PENDING) {
            throw new ValidationException("只有待确认的交易才能被确认");
        }

        transaction.setStatus(Transaction.TransactionStatus.CONFIRMED);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return new TransactionDTO.TransactionResponse(updatedTransaction);
    }

    /**
     * 取消交易
     */
    public TransactionDTO.TransactionResponse cancelTransaction(UUID uuid) {
        Transaction transaction = transactionRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("交易未找到: " + uuid));

        if (transaction.getIsIncludedInCutOff()) {
            throw new ValidationException("已包含在日结中的交易不能取消");
        }

        transaction.setStatus(Transaction.TransactionStatus.CANCELLED);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return new TransactionDTO.TransactionResponse(updatedTransaction);
    }

    /**
     * 删除交易（硬删除）
     */
    public void deleteTransaction(UUID uuid) {
        Transaction transaction = transactionRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("交易未找到: " + uuid));

        if (transaction.getIsIncludedInCutOff()) {
            throw new ValidationException("已包含在日结中的交易不能删除");
        }

        transactionRepository.delete(transaction);
    }

    /**
     * 执行日结
     */
    public TransactionDTO.DailyCutOffReportResponse performDailyCutOff(LocalDate cutOffDate) {
        // 获取指定日期的所有已确认且未日结的交易
        List<Transaction> transactions = transactionRepository.findConfirmedTransactionsByDate(cutOffDate)
                .stream()
                .filter(t -> !t.getIsIncludedInCutOff())
                .collect(Collectors.toList());

        if (transactions.isEmpty()) {
            throw new ValidationException("没有找到需要日结的交易");
        }

        // 标记交易为已日结
        for (Transaction transaction : transactions) {
            transaction.setIsIncludedInCutOff(true);
            transaction.setCutOffDate(cutOffDate);
        }
        transactionRepository.saveAll(transactions);

        // 生成日结报告
        return generateDailyCutOffReport(cutOffDate, transactions);
    }

    /**
     * 获取日结报告
     */
    @Transactional(readOnly = true)
    public TransactionDTO.DailyCutOffReportResponse getDailyCutOffReport(LocalDate cutOffDate) {
        List<Transaction> transactions = transactionRepository.findByCutOffDate(cutOffDate);
        return generateDailyCutOffReport(cutOffDate, transactions);
    }

    /**
     * 获取交易统计
     */
    @Transactional(readOnly = true)
    public TransactionDTO.TransactionStatsResponse getTransactionStats(LocalDate startDate, LocalDate endDate) {
        TransactionDTO.TransactionStatsResponse stats = new TransactionDTO.TransactionStatsResponse();

        // 计算总收入和支出
        BigDecimal totalIncome = transactionRepository.calculateIncomeForPeriod(startDate, endDate);
        BigDecimal totalExpense = transactionRepository.calculateExpenseForPeriod(startDate, endDate);

        stats.setTotalIncome(totalIncome);
        stats.setTotalExpense(totalExpense);
        stats.setNetProfit(totalIncome.subtract(totalExpense));

        // 统计交易数量
        List<Transaction> periodTransactions = transactionRepository.findByTransactionDateBetween(startDate, endDate);
        stats.setTotalTransactions((long) periodTransactions.size());

        Map<Transaction.TransactionStatus, Long> statusCounts = periodTransactions.stream()
                .collect(Collectors.groupingBy(Transaction::getStatus, Collectors.counting()));

        stats.setPendingTransactions(statusCounts.getOrDefault(Transaction.TransactionStatus.PENDING, 0L));
        stats.setConfirmedTransactions(statusCounts.getOrDefault(Transaction.TransactionStatus.CONFIRMED, 0L));
        stats.setCancelledTransactions(statusCounts.getOrDefault(Transaction.TransactionStatus.CANCELLED, 0L));

        return stats;
    }

    /**
     * 生成交易编号
     */
    private String generateTransactionNumber(LocalDate date) {
        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = transactionRepository.getTransactionCountForDate(date);
        return String.format("TXN%s%04d", dateStr, count + 1);
    }

    /**
     * 验证交易明细
     */
    private void validateTransactionItems(List<TransactionDTO.TransactionItemRequest> items, Transaction.TransactionType type) {
        if (items == null || items.isEmpty()) {
            throw new ValidationException("交易明细不能为空");
        }

        for (TransactionDTO.TransactionItemRequest item : items) {
            if (item.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("交易金额必须大于0");
            }

            // 验证分类与交易类型的一致性
            boolean isItemIncomeCategory = isIncomeCategory(item.getCategory());
            boolean isIncomeTransaction = type == Transaction.TransactionType.INCOME;

            if (isItemIncomeCategory != isIncomeTransaction) {
                throw new ValidationException("交易分类与交易类型不匹配");
            }
        }
    }

    /**
     * 判断是否为收入分类
     */
    private boolean isIncomeCategory(TransactionItem.TransactionCategory category) {
        return category == TransactionItem.TransactionCategory.SERVICE_MASSAGE ||
                category == TransactionItem.TransactionCategory.SERVICE_TIP ||
                category == TransactionItem.TransactionCategory.SERVICE_OTHER ||
                category == TransactionItem.TransactionCategory.PRODUCT_SALE ||
                category == TransactionItem.TransactionCategory.MEMBERSHIP_FEE ||
                category == TransactionItem.TransactionCategory.DEPOSIT ||
                category == TransactionItem.TransactionCategory.OTHER_INCOME;
    }

    /**
     * 生成日结报告
     */
    private TransactionDTO.DailyCutOffReportResponse generateDailyCutOffReport(LocalDate cutOffDate, List<Transaction> transactions) {
        TransactionDTO.DailyCutOffReportResponse report = new TransactionDTO.DailyCutOffReportResponse();
        report.setCutOffDate(cutOffDate);

        // 计算总收入和支出
        BigDecimal totalIncome = transactions.stream()
                .filter(Transaction::isIncome)
                .map(Transaction::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(Transaction::isExpense)
                .map(Transaction::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        report.setTotalIncome(totalIncome);
        report.setTotalExpense(totalExpense);
        report.setNetProfit(totalIncome.subtract(totalExpense));
        report.setTransactionCount(transactions.size());

        // 按支付方式汇总
        Map<Transaction.PaymentMethod, List<Transaction>> paymentMethodGroups = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getPaymentMethod));

        List<TransactionDTO.PaymentMethodSummary> paymentSummaries = new ArrayList<>();
        for (Map.Entry<Transaction.PaymentMethod, List<Transaction>> entry : paymentMethodGroups.entrySet()) {
            BigDecimal amount = entry.getValue().stream()
                    .map(Transaction::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            paymentSummaries.add(new TransactionDTO.PaymentMethodSummary(
                    entry.getKey(), amount, entry.getValue().size()));
        }
        report.setPaymentMethodSummaries(paymentSummaries);

        // 按分类汇总收入和支出
        List<TransactionItem> allItems = transactions.stream()
                .flatMap(t -> t.getTransactionItems().stream())
                .collect(Collectors.toList());

        Map<TransactionItem.TransactionCategory, List<TransactionItem>> categoryGroups = allItems.stream()
                .collect(Collectors.groupingBy(TransactionItem::getCategory));

        List<TransactionDTO.CategorySummary> incomeSummaries = new ArrayList<>();
        List<TransactionDTO.CategorySummary> expenseSummaries = new ArrayList<>();

        for (Map.Entry<TransactionItem.TransactionCategory, List<TransactionItem>> entry : categoryGroups.entrySet()) {
            BigDecimal amount = entry.getValue().stream()
                    .map(TransactionItem::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            TransactionDTO.CategorySummary summary = new TransactionDTO.CategorySummary(
                    entry.getKey(), amount, entry.getValue().size());

            if (entry.getValue().get(0).isIncomeCategory()) {
                incomeSummaries.add(summary);
            } else {
                expenseSummaries.add(summary);
            }
        }

        report.setIncomeCategorySummaries(incomeSummaries);
        report.setExpenseCategorySummaries(expenseSummaries);

        return report;
    }

    /**
     * 检查交易是否存在
     */
    @Transactional(readOnly = true)
    public boolean transactionExists(UUID uuid) {
        return transactionRepository.existsById(uuid);
    }
}