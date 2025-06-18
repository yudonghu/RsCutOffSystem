// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/dto/TransactionDTO.java
package com.relaxationspa.rscutoffsystem.dto;

import com.relaxationspa.rscutoffsystem.entity.Transaction;
import com.relaxationspa.rscutoffsystem.entity.TransactionItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TransactionDTO {

    // 创建交易请求DTO
    public static class CreateTransactionRequest {

        @NotNull(message = "交易类型不能为空")
        private Transaction.TransactionType type;

        @NotNull(message = "交易日期不能为空")
        private LocalDate transactionDate;

        private String description;
        private String notes;

        private UUID relatedCustomerUuid;

        // 修改：从单个UUID改为Set<UUID>
        @NotEmpty(message = "操作员工不能为空")
        private Set<UUID> relatedUserUuids;

        @NotNull(message = "支付方式不能为空")
        private Transaction.PaymentMethod paymentMethod;

        private String paymentReference;

        @NotEmpty(message = "交易明细不能为空")
        @Valid
        private List<TransactionItemRequest> items;

        // Constructors
        public CreateTransactionRequest() {}

        // Getters and Setters
        public Transaction.TransactionType getType() {
            return type;
        }

        public void setType(Transaction.TransactionType type) {
            this.type = type;
        }

        public LocalDate getTransactionDate() {
            return transactionDate;
        }

        public void setTransactionDate(LocalDate transactionDate) {
            this.transactionDate = transactionDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public UUID getRelatedCustomerUuid() {
            return relatedCustomerUuid;
        }

        public void setRelatedCustomerUuid(UUID relatedCustomerUuid) {
            this.relatedCustomerUuid = relatedCustomerUuid;
        }

        public Set<UUID> getRelatedUserUuids() {
            return relatedUserUuids;
        }

        public void setRelatedUserUuids(Set<UUID> relatedUserUuids) {
            this.relatedUserUuids = relatedUserUuids;
        }

        // 保留旧方法以兼容现有代码（已标记为过时）
        @Deprecated
        public UUID getRelatedUserUuid() {
            return relatedUserUuids != null && !relatedUserUuids.isEmpty() ?
                    relatedUserUuids.iterator().next() : null;
        }

        @Deprecated
        public void setRelatedUserUuid(UUID relatedUserUuid) {
            this.relatedUserUuids = Set.of(relatedUserUuid);
        }

        public Transaction.PaymentMethod getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(Transaction.PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getPaymentReference() {
            return paymentReference;
        }

        public void setPaymentReference(String paymentReference) {
            this.paymentReference = paymentReference;
        }

        public List<TransactionItemRequest> getItems() {
            return items;
        }

        public void setItems(List<TransactionItemRequest> items) {
            this.items = items;
        }
    }

    // 交易明细请求DTO
    public static class TransactionItemRequest {

        @NotNull(message = "交易分类不能为空")
        private TransactionItem.TransactionCategory category;

        @NotNull(message = "金额不能为空")
        @DecimalMin(value = "0.01", message = "金额必须大于0")
        private BigDecimal amount;

        private String description;
        private String notes;

        // Constructors
        public TransactionItemRequest() {}

        // Getters and Setters
        public TransactionItem.TransactionCategory getCategory() {
            return category;
        }

        public void setCategory(TransactionItem.TransactionCategory category) {
            this.category = category;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

    // 更新交易请求DTO
    public static class UpdateTransactionRequest {

        private LocalDate transactionDate;
        private String description;
        private String notes;
        private UUID relatedCustomerUuid;
        private Transaction.PaymentMethod paymentMethod;
        private String paymentReference;
        private Transaction.TransactionStatus status;

        // 修改：从单个UUID改为Set<UUID>
        private Set<UUID> relatedUserUuids;

        @Valid
        private List<TransactionItemRequest> items;

        // Getters and Setters
        public LocalDate getTransactionDate() {
            return transactionDate;
        }

        public void setTransactionDate(LocalDate transactionDate) {
            this.transactionDate = transactionDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public UUID getRelatedCustomerUuid() {
            return relatedCustomerUuid;
        }

        public void setRelatedCustomerUuid(UUID relatedCustomerUuid) {
            this.relatedCustomerUuid = relatedCustomerUuid;
        }

        public Set<UUID> getRelatedUserUuids() {
            return relatedUserUuids;
        }

        public void setRelatedUserUuids(Set<UUID> relatedUserUuids) {
            this.relatedUserUuids = relatedUserUuids;
        }

        public Transaction.PaymentMethod getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(Transaction.PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getPaymentReference() {
            return paymentReference;
        }

        public void setPaymentReference(String paymentReference) {
            this.paymentReference = paymentReference;
        }

        public Transaction.TransactionStatus getStatus() {
            return status;
        }

        public void setStatus(Transaction.TransactionStatus status) {
            this.status = status;
        }

        public List<TransactionItemRequest> getItems() {
            return items;
        }

        public void setItems(List<TransactionItemRequest> items) {
            this.items = items;
        }
    }

    // 交易响应DTO
    public static class TransactionResponse {

        private UUID uuid;
        private String transactionNumber;
        private LocalDateTime createdAt;
        private LocalDate transactionDate;
        private Transaction.TransactionType type;
        private BigDecimal totalAmount;
        private String description;
        private String notes;
        private UUID relatedCustomerUuid;

        // 修改：从单个UUID改为Set<UUID>
        private Set<UUID> relatedUserUuids;

        private Transaction.PaymentMethod paymentMethod;
        private String paymentReference;
        private Transaction.TransactionStatus status;
        private Boolean isIncludedInCutOff;
        private LocalDate cutOffDate;
        private List<TransactionItemResponse> items;

        // Constructors
        public TransactionResponse() {}

        public TransactionResponse(Transaction transaction) {
            this.uuid = transaction.getUuid();
            this.transactionNumber = transaction.getTransactionNumber();
            this.createdAt = transaction.getCreatedAt();
            this.transactionDate = transaction.getTransactionDate();
            this.type = transaction.getType();
            this.totalAmount = transaction.getTotalAmount();
            this.description = transaction.getDescription();
            this.notes = transaction.getNotes();
            this.relatedCustomerUuid = transaction.getRelatedCustomerUuid();
            this.relatedUserUuids = transaction.getRelatedUserUuids();
            this.paymentMethod = transaction.getPaymentMethod();
            this.paymentReference = transaction.getPaymentReference();
            this.status = transaction.getStatus();
            this.isIncludedInCutOff = transaction.getIsIncludedInCutOff();
            this.cutOffDate = transaction.getCutOffDate();
            this.items = transaction.getTransactionItems().stream()
                    .map(TransactionItemResponse::new)
                    .toList();
        }

        // Getters and Setters
        public UUID getUuid() {
            return uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        public String getTransactionNumber() {
            return transactionNumber;
        }

        public void setTransactionNumber(String transactionNumber) {
            this.transactionNumber = transactionNumber;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public LocalDate getTransactionDate() {
            return transactionDate;
        }

        public void setTransactionDate(LocalDate transactionDate) {
            this.transactionDate = transactionDate;
        }

        public Transaction.TransactionType getType() {
            return type;
        }

        public void setType(Transaction.TransactionType type) {
            this.type = type;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public UUID getRelatedCustomerUuid() {
            return relatedCustomerUuid;
        }

        public void setRelatedCustomerUuid(UUID relatedCustomerUuid) {
            this.relatedCustomerUuid = relatedCustomerUuid;
        }

        public Set<UUID> getRelatedUserUuids() {
            return relatedUserUuids;
        }

        public void setRelatedUserUuids(Set<UUID> relatedUserUuids) {
            this.relatedUserUuids = relatedUserUuids;
        }

        // 保留旧方法以兼容现有代码（已标记为过时）
        @Deprecated
        public UUID getRelatedUserUuid() {
            return relatedUserUuids != null && !relatedUserUuids.isEmpty() ?
                    relatedUserUuids.iterator().next() : null;
        }

        @Deprecated
        public void setRelatedUserUuid(UUID relatedUserUuid) {
            this.relatedUserUuids = Set.of(relatedUserUuid);
        }

        public Transaction.PaymentMethod getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(Transaction.PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getPaymentReference() {
            return paymentReference;
        }

        public void setPaymentReference(String paymentReference) {
            this.paymentReference = paymentReference;
        }

        public Transaction.TransactionStatus getStatus() {
            return status;
        }

        public void setStatus(Transaction.TransactionStatus status) {
            this.status = status;
        }

        public Boolean getIsIncludedInCutOff() {
            return isIncludedInCutOff;
        }

        public void setIsIncludedInCutOff(Boolean isIncludedInCutOff) {
            this.isIncludedInCutOff = isIncludedInCutOff;
        }

        public LocalDate getCutOffDate() {
            return cutOffDate;
        }

        public void setCutOffDate(LocalDate cutOffDate) {
            this.cutOffDate = cutOffDate;
        }

        public List<TransactionItemResponse> getItems() {
            return items;
        }

        public void setItems(List<TransactionItemResponse> items) {
            this.items = items;
        }
    }

    // 交易明细响应DTO
    public static class TransactionItemResponse {

        private UUID uuid;
        private TransactionItem.TransactionCategory category;
        private BigDecimal amount;
        private String description;
        private String notes;
        private String categoryDisplayName;

        // Constructors
        public TransactionItemResponse() {}

        public TransactionItemResponse(TransactionItem item) {
            this.uuid = item.getUuid();
            this.category = item.getCategory();
            this.amount = item.getAmount();
            this.description = item.getDescription();
            this.notes = item.getNotes();
            this.categoryDisplayName = item.getCategoryDisplayName();
        }

        // Getters and Setters
        public UUID getUuid() {
            return uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        public TransactionItem.TransactionCategory getCategory() {
            return category;
        }

        public void setCategory(TransactionItem.TransactionCategory category) {
            this.category = category;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getCategoryDisplayName() {
            return categoryDisplayName;
        }

        public void setCategoryDisplayName(String categoryDisplayName) {
            this.categoryDisplayName = categoryDisplayName;
        }
    }

    // 交易统计响应DTO
    public static class TransactionStatsResponse {
        private BigDecimal totalIncome;
        private BigDecimal totalExpense;
        private BigDecimal netProfit;
        private Long totalTransactions;
        private Long pendingTransactions;
        private Long confirmedTransactions;
        private Long cancelledTransactions;

        // 按支付方式统计
        private BigDecimal cashAmount;
        private BigDecimal cardAmount;
        private BigDecimal giftCardAmount;
        private BigDecimal bankTransferAmount;
        private BigDecimal otherAmount;

        // Constructors
        public TransactionStatsResponse() {}

        // Getters and Setters
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

        public Long getTotalTransactions() {
            return totalTransactions;
        }

        public void setTotalTransactions(Long totalTransactions) {
            this.totalTransactions = totalTransactions;
        }

        public Long getPendingTransactions() {
            return pendingTransactions;
        }

        public void setPendingTransactions(Long pendingTransactions) {
            this.pendingTransactions = pendingTransactions;
        }

        public Long getConfirmedTransactions() {
            return confirmedTransactions;
        }

        public void setConfirmedTransactions(Long confirmedTransactions) {
            this.confirmedTransactions = confirmedTransactions;
        }

        public Long getCancelledTransactions() {
            return cancelledTransactions;
        }

        public void setCancelledTransactions(Long cancelledTransactions) {
            this.cancelledTransactions = cancelledTransactions;
        }

        public BigDecimal getCashAmount() {
            return cashAmount;
        }

        public void setCashAmount(BigDecimal cashAmount) {
            this.cashAmount = cashAmount;
        }

        public BigDecimal getCardAmount() {
            return cardAmount;
        }

        public void setCardAmount(BigDecimal cardAmount) {
            this.cardAmount = cardAmount;
        }

        public BigDecimal getGiftCardAmount() {
            return giftCardAmount;
        }

        public void setGiftCardAmount(BigDecimal giftCardAmount) {
            this.giftCardAmount = giftCardAmount;
        }

        public BigDecimal getBankTransferAmount() {
            return bankTransferAmount;
        }

        public void setBankTransferAmount(BigDecimal bankTransferAmount) {
            this.bankTransferAmount = bankTransferAmount;
        }

        public BigDecimal getOtherAmount() {
            return otherAmount;
        }

        public void setOtherAmount(BigDecimal otherAmount) {
            this.otherAmount = otherAmount;
        }
    }

    // 日结报告DTO
    public static class DailyCutOffReportResponse {
        private LocalDate cutOffDate;
        private BigDecimal totalIncome;
        private BigDecimal totalExpense;
        private BigDecimal netProfit;
        private Integer transactionCount;
        private List<PaymentMethodSummary> paymentMethodSummaries;
        private List<CategorySummary> incomeCategorySummaries;
        private List<CategorySummary> expenseCategorySummaries;

        // Constructors
        public DailyCutOffReportResponse() {}

        // Getters and Setters
        public LocalDate getCutOffDate() {
            return cutOffDate;
        }

        public void setCutOffDate(LocalDate cutOffDate) {
            this.cutOffDate = cutOffDate;
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

        public Integer getTransactionCount() {
            return transactionCount;
        }

        public void setTransactionCount(Integer transactionCount) {
            this.transactionCount = transactionCount;
        }

        public List<PaymentMethodSummary> getPaymentMethodSummaries() {
            return paymentMethodSummaries;
        }

        public void setPaymentMethodSummaries(List<PaymentMethodSummary> paymentMethodSummaries) {
            this.paymentMethodSummaries = paymentMethodSummaries;
        }

        public List<CategorySummary> getIncomeCategorySummaries() {
            return incomeCategorySummaries;
        }

        public void setIncomeCategorySummaries(List<CategorySummary> incomeCategorySummaries) {
            this.incomeCategorySummaries = incomeCategorySummaries;
        }

        public List<CategorySummary> getExpenseCategorySummaries() {
            return expenseCategorySummaries;
        }

        public void setExpenseCategorySummaries(List<CategorySummary> expenseCategorySummaries) {
            this.expenseCategorySummaries = expenseCategorySummaries;
        }
    }

    // 支付方式汇总
    public static class PaymentMethodSummary {
        private Transaction.PaymentMethod paymentMethod;
        private BigDecimal amount;
        private Integer count;

        public PaymentMethodSummary() {}

        public PaymentMethodSummary(Transaction.PaymentMethod paymentMethod, BigDecimal amount, Integer count) {
            this.paymentMethod = paymentMethod;
            this.amount = amount;
            this.count = count;
        }

        // Getters and Setters
        public Transaction.PaymentMethod getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(Transaction.PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }

    // 分类汇总
    public static class CategorySummary {
        private TransactionItem.TransactionCategory category;
        private String categoryDisplayName;
        private BigDecimal amount;
        private Integer count;

        public CategorySummary() {}

        public CategorySummary(TransactionItem.TransactionCategory category, BigDecimal amount, Integer count) {
            this.category = category;
            this.amount = amount;
            this.count = count;
        }

        // Getters and Setters
        public TransactionItem.TransactionCategory getCategory() {
            return category;
        }

        public void setCategory(TransactionItem.TransactionCategory category) {
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

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }
}