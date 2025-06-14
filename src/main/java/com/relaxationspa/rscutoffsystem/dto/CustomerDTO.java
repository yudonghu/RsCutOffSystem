// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/dto/CustomerDTO.java
package com.relaxationspa.rscutoffsystem.dto;

import com.relaxationspa.rscutoffsystem.entity.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class CustomerDTO {

    // 创建客户请求DTO
    public static class CreateCustomerRequest {

        @NotBlank(message = "客户姓名不能为空")
        private String name;

        @Pattern(regexp = "^[1-9]\\d{10}$", message = "请输入有效的手机号码")
        private String phone;

        @Email(message = "请输入有效的邮箱地址")
        private String email;

        private Customer.Gender gender;

        private LocalDate birthday;

        @NotNull(message = "客户类型不能为空")
        private Customer.CustomerType customerType;

        // 会员相关字段（仅当customerType为MEMBER时需要）
        private Customer.MembershipLevel membershipLevel;
        private LocalDate membershipStartDate;
        private LocalDate membershipEndDate;

        @Min(value = 0, message = "积分不能为负数")
        private Integer points = 0;

        private Customer.CustomerStatus status = Customer.CustomerStatus.ACTIVE;

        private String notes;

        // Constructors
        public CreateCustomerRequest() {}

        // Getters and Setters
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

        public Customer.Gender getGender() {
            return gender;
        }

        public void setGender(Customer.Gender gender) {
            this.gender = gender;
        }

        public LocalDate getBirthday() {
            return birthday;
        }

        public void setBirthday(LocalDate birthday) {
            this.birthday = birthday;
        }

        public Customer.CustomerType getCustomerType() {
            return customerType;
        }

        public void setCustomerType(Customer.CustomerType customerType) {
            this.customerType = customerType;
        }

        public Customer.MembershipLevel getMembershipLevel() {
            return membershipLevel;
        }

        public void setMembershipLevel(Customer.MembershipLevel membershipLevel) {
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

        public Customer.CustomerStatus getStatus() {
            return status;
        }

        public void setStatus(Customer.CustomerStatus status) {
            this.status = status;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

    // 更新客户请求DTO
    public static class UpdateCustomerRequest {

        private String name;
        private String phone;
        private String email;
        private Customer.Gender gender;
        private LocalDate birthday;
        private Customer.CustomerType customerType;
        private Customer.MembershipLevel membershipLevel;
        private LocalDate membershipStartDate;
        private LocalDate membershipEndDate;
        private Integer points;
        private Customer.CustomerStatus status;
        private String notes;

        // Getters and Setters
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

        public Customer.Gender getGender() {
            return gender;
        }

        public void setGender(Customer.Gender gender) {
            this.gender = gender;
        }

        public LocalDate getBirthday() {
            return birthday;
        }

        public void setBirthday(LocalDate birthday) {
            this.birthday = birthday;
        }

        public Customer.CustomerType getCustomerType() {
            return customerType;
        }

        public void setCustomerType(Customer.CustomerType customerType) {
            this.customerType = customerType;
        }

        public Customer.MembershipLevel getMembershipLevel() {
            return membershipLevel;
        }

        public void setMembershipLevel(Customer.MembershipLevel membershipLevel) {
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

        public Customer.CustomerStatus getStatus() {
            return status;
        }

        public void setStatus(Customer.CustomerStatus status) {
            this.status = status;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

    // 客户响应DTO
    public static class CustomerResponse {

        private UUID uuid;
        private LocalDateTime createdAt;
        private Long customerNumber;
        private String name;
        private String phone;
        private String email;
        private Customer.Gender gender;
        private LocalDate birthday;
        private Customer.CustomerType customerType;
        private Customer.MembershipLevel membershipLevel;
        private LocalDate membershipStartDate;
        private LocalDate membershipEndDate;
        private Integer points;
        private Double totalSpent;
        private Integer visitCount;
        private LocalDateTime lastVisitDate;
        private Customer.CustomerStatus status;
        private String notes;

        // Constructors
        public CustomerResponse() {}

        public CustomerResponse(Customer customer) {
            this.uuid = customer.getUuid();
            this.createdAt = customer.getCreatedAt();
            this.customerNumber = customer.getCustomerNumber();
            this.name = customer.getName();
            this.phone = customer.getPhone();
            this.email = customer.getEmail();
            this.gender = customer.getGender();
            this.birthday = customer.getBirthday();
            this.customerType = customer.getCustomerType();
            this.membershipLevel = customer.getMembershipLevel();
            this.membershipStartDate = customer.getMembershipStartDate();
            this.membershipEndDate = customer.getMembershipEndDate();
            this.points = customer.getPoints();
            this.totalSpent = customer.getTotalSpent();
            this.visitCount = customer.getVisitCount();
            this.lastVisitDate = customer.getLastVisitDate();
            this.status = customer.getStatus();
            this.notes = customer.getNotes();
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

        public Customer.Gender getGender() {
            return gender;
        }

        public void setGender(Customer.Gender gender) {
            this.gender = gender;
        }

        public LocalDate getBirthday() {
            return birthday;
        }

        public void setBirthday(LocalDate birthday) {
            this.birthday = birthday;
        }

        public Customer.CustomerType getCustomerType() {
            return customerType;
        }

        public void setCustomerType(Customer.CustomerType customerType) {
            this.customerType = customerType;
        }

        public Customer.MembershipLevel getMembershipLevel() {
            return membershipLevel;
        }

        public void setMembershipLevel(Customer.MembershipLevel membershipLevel) {
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

        public Customer.CustomerStatus getStatus() {
            return status;
        }

        public void setStatus(Customer.CustomerStatus status) {
            this.status = status;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

    // 客户统计响应DTO
    public static class CustomerStatsResponse {
        private Long totalCustomers;
        private Long walkInCustomers;
        private Long memberCustomers;
        private Long activeCustomers;
        private Long inactiveCustomers;
        private Long blockedCustomers;

        // 会员等级统计
        private Long bronzeMembers;
        private Long silverMembers;
        private Long goldMembers;
        private Long platinumMembers;
        private Long diamondMembers;

        // Constructors
        public CustomerStatsResponse() {}

        // Getters and Setters
        public Long getTotalCustomers() {
            return totalCustomers;
        }

        public void setTotalCustomers(Long totalCustomers) {
            this.totalCustomers = totalCustomers;
        }

        public Long getWalkInCustomers() {
            return walkInCustomers;
        }

        public void setWalkInCustomers(Long walkInCustomers) {
            this.walkInCustomers = walkInCustomers;
        }

        public Long getMemberCustomers() {
            return memberCustomers;
        }

        public void setMemberCustomers(Long memberCustomers) {
            this.memberCustomers = memberCustomers;
        }

        public Long getActiveCustomers() {
            return activeCustomers;
        }

        public void setActiveCustomers(Long activeCustomers) {
            this.activeCustomers = activeCustomers;
        }

        public Long getInactiveCustomers() {
            return inactiveCustomers;
        }

        public void setInactiveCustomers(Long inactiveCustomers) {
            this.inactiveCustomers = inactiveCustomers;
        }

        public Long getBlockedCustomers() {
            return blockedCustomers;
        }

        public void setBlockedCustomers(Long blockedCustomers) {
            this.blockedCustomers = blockedCustomers;
        }

        public Long getBronzeMembers() {
            return bronzeMembers;
        }

        public void setBronzeMembers(Long bronzeMembers) {
            this.bronzeMembers = bronzeMembers;
        }

        public Long getSilverMembers() {
            return silverMembers;
        }

        public void setSilverMembers(Long silverMembers) {
            this.silverMembers = silverMembers;
        }

        public Long getGoldMembers() {
            return goldMembers;
        }

        public void setGoldMembers(Long goldMembers) {
            this.goldMembers = goldMembers;
        }

        public Long getPlatinumMembers() {
            return platinumMembers;
        }

        public void setPlatinumMembers(Long platinumMembers) {
            this.platinumMembers = platinumMembers;
        }

        public Long getDiamondMembers() {
            return diamondMembers;
        }

        public void setDiamondMembers(Long diamondMembers) {
            this.diamondMembers = diamondMembers;
        }
    }
}