// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/entity/TransactionItem.java
package com.relaxationspa.rscutoffsystem.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "transaction_items")
public class TransactionItem {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_uuid", nullable = false)
    private Transaction transaction;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private TransactionCategory category;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @Column(name = "notes")
    private String notes;

    // 枚举定义
    public enum TransactionCategory {
        // 收入类别
        SERVICE_MASSAGE,        // 按摩服务收入
        SERVICE_TIP,           // 小费收入
        SERVICE_OTHER,         // 其他服务收入
        PRODUCT_SALE,          // 产品销售收入
        MEMBERSHIP_FEE,        // 会员费收入
        DEPOSIT,               // 押金收入
        OTHER_INCOME,          // 其他收入

        // 支出类别
        SALARY_MASSAGE_THERAPIST,  // 按摩师工资
        SALARY_STAFF,             // 其他员工工资
        RENT,                     // 房租
        UTILITIES,                // 水电费
        SUPPLIES,                 // 耗材采购
        EQUIPMENT,                // 设备采购
        MARKETING,                // 营销费用
        TAX,                      // 税费
        OTHER_EXPENSE             // 其他支出
    }

    // 构造函数
    public TransactionItem() {}

    public TransactionItem(TransactionCategory category, BigDecimal amount, String description) {
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    // 业务方法
    public boolean isIncomeCategory() {
        return category == TransactionCategory.SERVICE_MASSAGE ||
                category == TransactionCategory.SERVICE_TIP ||
                category == TransactionCategory.SERVICE_OTHER ||
                category == TransactionCategory.PRODUCT_SALE ||
                category == TransactionCategory.MEMBERSHIP_FEE ||
                category == TransactionCategory.DEPOSIT ||
                category == TransactionCategory.OTHER_INCOME;
    }

    public boolean isExpenseCategory() {
        return !isIncomeCategory();
    }

    public String getCategoryDisplayName() {
        return switch (category) {
            case SERVICE_MASSAGE -> "按摩服务";
            case SERVICE_TIP -> "小费";
            case SERVICE_OTHER -> "其他服务";
            case PRODUCT_SALE -> "产品销售";
            case MEMBERSHIP_FEE -> "会员费";
            case DEPOSIT -> "押金";
            case OTHER_INCOME -> "其他收入";
            case SALARY_MASSAGE_THERAPIST -> "按摩师工资";
            case SALARY_STAFF -> "员工工资";
            case RENT -> "房租";
            case UTILITIES -> "水电费";
            case SUPPLIES -> "耗材采购";
            case EQUIPMENT -> "设备采购";
            case MARKETING -> "营销费用";
            case TAX -> "税费";
            case OTHER_EXPENSE -> "其他支出";
        };
    }

    // Getters and Setters
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(TransactionCategory category) {
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