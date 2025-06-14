// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/entity/Transaction.java
package com.relaxationspa.rscutoffsystem.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @Column(name = "transaction_number", nullable = false, unique = true)
    private String transactionNumber;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "description")
    private String description;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // 关联信息
    @Column(name = "related_customer_uuid")
    private UUID relatedCustomerUuid;

    @Column(name = "related_user_uuid", nullable = false)
    private UUID relatedUserUuid;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;

    @Column(name = "is_included_in_cutoff", nullable = false)
    private Boolean isIncludedInCutOff = false;

    @Column(name = "cutoff_date")
    private LocalDate cutOffDate;

    // 交易明细
    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionItem> transactionItems = new ArrayList<>();

    // 枚举定义
    public enum TransactionType {
        INCOME,     // 收入
        EXPENSE     // 支出
    }

    public enum PaymentMethod {
        CASH,           // 现金
        CARD,           // 刷卡
        GIFT_CARD,      // 礼品卡
        BANK_TRANSFER,  // 银行转账
        OTHER           // 其他
    }

    public enum TransactionStatus {
        PENDING,    // 待确认
        CONFIRMED,  // 已确认
        CANCELLED   // 已取消
    }

    // 构造函数
    public Transaction() {}

    public Transaction(TransactionType type, BigDecimal totalAmount, String description) {
        this.type = type;
        this.totalAmount = totalAmount;
        this.description = description;
        this.transactionDate = LocalDate.now();
        this.status = TransactionStatus.PENDING;
        this.isIncludedInCutOff = false;
    }

    // 业务方法
    public void addTransactionItem(TransactionItem item) {
        transactionItems.add(item);
        item.setTransaction(this);
    }

    public void removeTransactionItem(TransactionItem item) {
        transactionItems.remove(item);
        item.setTransaction(null);
    }

    public BigDecimal calculateTotalFromItems() {
        return transactionItems.stream()
                .map(TransactionItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isIncome() {
        return this.type == TransactionType.INCOME;
    }

    public boolean isExpense() {
        return this.type == TransactionType.EXPENSE;
    }

    public boolean canBeModified() {
        return this.status != TransactionStatus.CANCELLED && !this.isIncludedInCutOff;
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

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
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

    public UUID getRelatedUserUuid() {
        return relatedUserUuid;
    }

    public void setRelatedUserUuid(UUID relatedUserUuid) {
        this.relatedUserUuid = relatedUserUuid;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
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

    public List<TransactionItem> getTransactionItems() {
        return transactionItems;
    }

    public void setTransactionItems(List<TransactionItem> transactionItems) {
        this.transactionItems = transactionItems;
    }
}