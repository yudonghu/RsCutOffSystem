// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/entity/Customer.java
package com.relaxationspa.rscutoffsystem.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "customer_number", nullable = false, unique = true)
    private Long customerNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false)
    private CustomerType customerType;

    // 会员相关字段
    @Column(name = "membership_level")
    @Enumerated(EnumType.STRING)
    private MembershipLevel membershipLevel;

    @Column(name = "membership_start_date")
    private LocalDate membershipStartDate;

    @Column(name = "membership_end_date")
    private LocalDate membershipEndDate;

    @Column(name = "points", columnDefinition = "INTEGER DEFAULT 0")
    private Integer points = 0;

    @Column(name = "total_spent", columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private Double totalSpent = 0.0;

    @Column(name = "visit_count", columnDefinition = "INTEGER DEFAULT 0")
    private Integer visitCount = 0;

    @Column(name = "last_visit_date")
    private LocalDateTime lastVisitDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CustomerStatus status = CustomerStatus.ACTIVE;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // 枚举定义
    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum CustomerType {
        WALK_IN,    // 散客/非会员
        MEMBER      // 会员
    }

    public enum MembershipLevel {
        BRONZE,     // 铜卡
        SILVER,     // 银卡  
        GOLD,       // 金卡
        PLATINUM,   // 白金卡
        DIAMOND     // 钻石卡
    }

    public enum CustomerStatus {
        ACTIVE,     // 活跃
        INACTIVE,   // 不活跃
        BLOCKED     // 黑名单
    }

    // 构造函数
    public Customer() {}

    public Customer(String name, CustomerType customerType) {
        this.name = name;
        this.customerType = customerType;
        this.status = CustomerStatus.ACTIVE;
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

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public MembershipLevel getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(MembershipLevel membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public LocalDate getMembershipStartDate() {
        return membershipStartDate;
    }

    public void setMembershipStartDate(LocalDate membershipStartDate) {
        this.membershipStartDate = membershipStartDate;
    }

    public LocalDate getMembershipEndDate() {
        return membershipEndDate;
    }

    public void setMembershipEndDate(LocalDate membershipEndDate) {
        this.membershipEndDate = membershipEndDate;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(Double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public LocalDateTime getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(LocalDateTime lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}