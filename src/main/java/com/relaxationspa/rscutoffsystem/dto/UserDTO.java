// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/dto/UserDTO.java
package com.relaxationspa.rscutoffsystem.dto;

import com.relaxationspa.rscutoffsystem.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class UserDTO {

    // 创建用户请求DTO
    public static class CreateUserRequest {

        @NotBlank(message = "昵称不能为空")
        private String nickname;

        @NotNull(message = "性别不能为空")
        private User.Gender gender;

        private LocalDate birthday;

        @Pattern(regexp = "^\\d{10}$", message = "请输入有效的手机号码")
        private String phone;

        @Email(message = "请输入有效的邮箱地址")
        private String email;

        private LocalDate hireDate;

        private User.EmploymentStatus employmentStatus = User.EmploymentStatus.ACTIVE;

        private Set<User.Role> roles;

        // Constructors
        public CreateUserRequest() {}

        // Getters and Setters
        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public User.Gender getGender() {
            return gender;
        }

        public void setGender(User.Gender gender) {
            this.gender = gender;
        }

        public LocalDate getBirthday() {
            return birthday;
        }

        public void setBirthday(LocalDate birthday) {
            this.birthday = birthday;
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

        public LocalDate getHireDate() {
            return hireDate;
        }

        public void setHireDate(LocalDate hireDate) {
            this.hireDate = hireDate;
        }

        public User.EmploymentStatus getEmploymentStatus() {
            return employmentStatus;
        }

        public void setEmploymentStatus(User.EmploymentStatus employmentStatus) {
            this.employmentStatus = employmentStatus;
        }

        public Set<User.Role> getRoles() {
            return roles;
        }

        public void setRoles(Set<User.Role> roles) {
            this.roles = roles;
        }
    }

    // 更新用户请求DTO
    public static class UpdateUserRequest {

        private String nickname;
        private LocalDate birthday;
        private String phone;
        private String email;
        private User.Gender gender;
        private LocalDate hireDate;
        private User.EmploymentStatus employmentStatus;
        private Set<User.Role> roles;

        // Getters and Setters
        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public LocalDate getBirthday() {
            return birthday;
        }

        public void setBirthday(LocalDate birthday) {
            this.birthday = birthday;
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

        public User.Gender getGender() {
            return gender;
        }

        public void setGender(User.Gender gender) {
            this.gender = gender;
        }

        public LocalDate getHireDate() {
            return hireDate;
        }

        public void setHireDate(LocalDate hireDate) {
            this.hireDate = hireDate;
        }

        public User.EmploymentStatus getEmploymentStatus() {
            return employmentStatus;
        }

        public void setEmploymentStatus(User.EmploymentStatus employmentStatus) {
            this.employmentStatus = employmentStatus;
        }

        public Set<User.Role> getRoles() {
            return roles;
        }

        public void setRoles(Set<User.Role> roles) {
            this.roles = roles;
        }
    }

    // 用户响应DTO
    public static class UserResponse {

        private UUID uuid;
        private LocalDateTime createdAt;
        private Long employeeNumber;
        private String nickname;
        private User.Gender gender;
        private LocalDate birthday;
        private String phone;
        private String email;
        private LocalDate hireDate;
        private User.EmploymentStatus employmentStatus;
        private Set<User.Role> roles;

        // Constructors
        public UserResponse() {}

        public UserResponse(User user) {
            this.uuid = user.getUuid();
            this.createdAt = user.getCreatedAt();
            this.employeeNumber = user.getEmployeeNumber();
            this.nickname = user.getNickname();
            this.gender = user.getGender();
            this.birthday = user.getBirthday();
            this.phone = user.getPhone();
            this.email = user.getEmail();
            this.hireDate = user.getHireDate();
            this.employmentStatus = user.getEmploymentStatus();
            this.roles = user.getRoles();
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

        public Long getEmployeeNumber() {
            return employeeNumber;
        }

        public void setEmployeeNumber(Long employeeNumber) {
            this.employeeNumber = employeeNumber;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public User.Gender getGender() {
            return gender;
        }

        public void setGender(User.Gender gender) {
            this.gender = gender;
        }

        public LocalDate getBirthday() {
            return birthday;
        }

        public void setBirthday(LocalDate birthday) {
            this.birthday = birthday;
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

        public LocalDate getHireDate() {
            return hireDate;
        }

        public void setHireDate(LocalDate hireDate) {
            this.hireDate = hireDate;
        }

        public User.EmploymentStatus getEmploymentStatus() {
            return employmentStatus;
        }

        public void setEmploymentStatus(User.EmploymentStatus employmentStatus) {
            this.employmentStatus = employmentStatus;
        }

        public Set<User.Role> getRoles() {
            return roles;
        }

        public void setRoles(Set<User.Role> roles) {
            this.roles = roles;
        }
    }
}