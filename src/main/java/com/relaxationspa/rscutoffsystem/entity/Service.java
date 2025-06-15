// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/entity/Service.java
package com.relaxationspa.rscutoffsystem.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "service_code", nullable = false, unique = true)
    private String serviceCode;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ServiceStatus status = ServiceStatus.ACTIVE;

    @Column(name = "is_combination", nullable = false)
    private Boolean isCombination = false;

    @Column(name = "combination_description")
    private String combinationDescription;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "notes")
    private String notes;

    // 枚举定义
    public enum ServiceType {
        CHAIR_MASSAGE,          // 椅背按摩
        FOOT_REFLEXOLOGY,       // 足疗
        BODY_MASSAGE,           // 身体按摩
        COMBINATION_MASSAGE     // 组合按摩
    }

    public enum ServiceStatus {
        ACTIVE,     // 可用
        INACTIVE,   // 停用
        SEASONAL    // 季节性
    }

    // 构造函数
    public Service() {}

    public Service(String serviceName, ServiceType serviceType, Integer durationMinutes, BigDecimal price) {
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.status = ServiceStatus.ACTIVE;
        this.isCombination = serviceType == ServiceType.COMBINATION_MASSAGE;
    }

    // 业务方法
    public String getDisplayName() {
        return String.format("%s (%d分钟)", serviceName, durationMinutes);
    }

    public String getFormattedPrice() {
        return String.format("$%.2f", price);
    }

    public String getServiceTypeDisplayName() {
        return switch (serviceType) {
            case CHAIR_MASSAGE -> "椅背按摩";
            case FOOT_REFLEXOLOGY -> "足疗";
            case BODY_MASSAGE -> "身体按摩";
            case COMBINATION_MASSAGE -> "组合按摩";
        };
    }

    public boolean isAvailable() {
        return this.status == ServiceStatus.ACTIVE;
    }

    public BigDecimal calculatePriceWithDiscount(BigDecimal discountPercent) {
        if (discountPercent == null || discountPercent.compareTo(BigDecimal.ZERO) == 0) {
            return this.price;
        }
        BigDecimal discount = this.price.multiply(discountPercent).divide(BigDecimal.valueOf(100));
        return this.price.subtract(discount);
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

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public Boolean getIsCombination() {
        return isCombination;
    }

    public void setIsCombination(Boolean isCombination) {
        this.isCombination = isCombination;
    }

    public String getCombinationDescription() {
        return combinationDescription;
    }

    public void setCombinationDescription(String combinationDescription) {
        this.combinationDescription = combinationDescription;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}