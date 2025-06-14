// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/controller/CustomerController.java
package com.relaxationspa.rscutoffsystem.controller;

import com.relaxationspa.rscutoffsystem.dto.CustomerDTO;
import com.relaxationspa.rscutoffsystem.entity.Customer;
import com.relaxationspa.rscutoffsystem.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 创建新客户
     */
    @PostMapping
    public ResponseEntity<CustomerDTO.CustomerResponse> createCustomer(@Valid @RequestBody CustomerDTO.CreateCustomerRequest request) {
        CustomerDTO.CustomerResponse response = customerService.createCustomer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 获取所有客户（分页）
     */
    @GetMapping
    public ResponseEntity<Page<CustomerDTO.CustomerResponse>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CustomerDTO.CustomerResponse> customers = customerService.getAllCustomers(pageable);

        return ResponseEntity.ok(customers);
    }

    /**
     * 根据UUID获取客户
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<CustomerDTO.CustomerResponse> getCustomerByUuid(@PathVariable UUID uuid) {
        CustomerDTO.CustomerResponse response = customerService.getCustomerByUuid(uuid);
        return ResponseEntity.ok(response);
    }

    /**
     * 根据客户编号获取客户
     */
    @GetMapping("/number/{customerNumber}")
    public ResponseEntity<CustomerDTO.CustomerResponse> getCustomerByNumber(@PathVariable Long customerNumber) {
        CustomerDTO.CustomerResponse response = customerService.getCustomerByNumber(customerNumber);
        return ResponseEntity.ok(response);
    }

    /**
     * 根据电话号码获取客户
     */
    @GetMapping("/phone/{phone}")
    public ResponseEntity<CustomerDTO.CustomerResponse> getCustomerByPhone(@PathVariable String phone) {
        return customerService.getCustomerByPhone(phone)
                .map(customer -> ResponseEntity.ok(customer))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据姓名搜索客户
     */
    @GetMapping("/search")
    public ResponseEntity<List<CustomerDTO.CustomerResponse>> searchCustomersByName(@RequestParam String name) {
        List<CustomerDTO.CustomerResponse> customers = customerService.searchCustomersByName(name);
        return ResponseEntity.ok(customers);
    }

    /**
     * 获取所有活跃客户
     */
    @GetMapping("/active")
    public ResponseEntity<List<CustomerDTO.CustomerResponse>> getAllActiveCustomers() {
        List<CustomerDTO.CustomerResponse> customers = customerService.getAllActiveCustomers();
        return ResponseEntity.ok(customers);
    }

    /**
     * 获取所有会员客户
     */
    @GetMapping("/members")
    public ResponseEntity<List<CustomerDTO.CustomerResponse>> getAllMembers() {
        List<CustomerDTO.CustomerResponse> customers = customerService.getAllMembers();
        return ResponseEntity.ok(customers);
    }

    /**
     * 获取所有散客
     */
    @GetMapping("/walk-in")
    public ResponseEntity<List<CustomerDTO.CustomerResponse>> getAllWalkInCustomers() {
        List<CustomerDTO.CustomerResponse> customers = customerService.getAllWalkInCustomers();
        return ResponseEntity.ok(customers);
    }

    /**
     * 根据客户类型获取客户
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<CustomerDTO.CustomerResponse>> getCustomersByType(@PathVariable Customer.CustomerType type) {
        List<CustomerDTO.CustomerResponse> customers = customerService.getCustomersByType(type);
        return ResponseEntity.ok(customers);
    }

    /**
     * 根据客户状态获取客户
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<CustomerDTO.CustomerResponse>> getCustomersByStatus(@PathVariable Customer.CustomerStatus status) {
        List<CustomerDTO.CustomerResponse> customers = customerService.getCustomersByStatus(status);
        return ResponseEntity.ok(customers);
    }

    /**
     * 根据会员等级获取客户
     */
    @GetMapping("/membership/{level}")
    public ResponseEntity<List<CustomerDTO.CustomerResponse>> getCustomersByMembershipLevel(@PathVariable Customer.MembershipLevel level) {
        List<CustomerDTO.CustomerResponse> customers = customerService.getCustomersByMembershipLevel(level);
        return ResponseEntity.ok(customers);
    }

    /**
     * 获取即将到期的会员
     */
    @GetMapping("/members/expiring")
    public ResponseEntity<List<CustomerDTO.CustomerResponse>> getMembersExpiringWithin(
            @RequestParam(defaultValue = "30") int days) {
        List<CustomerDTO.CustomerResponse> customers = customerService.getMembersExpiringWithin(days);
        return ResponseEntity.ok(customers);
    }

    /**
     * 获取今天生日的客户
     */
    @GetMapping("/birthday/today")
    public ResponseEntity<List<CustomerDTO.CustomerResponse>> getCustomersWithBirthdayToday() {
        List<CustomerDTO.CustomerResponse> customers = customerService.getCustomersWithBirthdayToday();
        return ResponseEntity.ok(customers);
    }

    /**
     * 获取指定月份生日的客户
     */
    @GetMapping("/birthday/month/{month}")
    public ResponseEntity<List<CustomerDTO.CustomerResponse>> getCustomersWithBirthdayInMonth(@PathVariable int month) {
        List<CustomerDTO.CustomerResponse> customers = customerService.getCustomersWithBirthdayInMonth(month);
        return ResponseEntity.ok(customers);
    }

    /**
     * 获取不活跃客户
     */
    @GetMapping("/inactive")
    public ResponseEntity<List<CustomerDTO.CustomerResponse>> getInactiveCustomers(
            @RequestParam(defaultValue = "90") int days) {
        List<CustomerDTO.CustomerResponse> customers = customerService.getInactiveCustomers(days);
        return ResponseEntity.ok(customers);
    }

    /**
     * 更新客户信息
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<CustomerDTO.CustomerResponse> updateCustomer(
            @PathVariable UUID uuid,
            @Valid @RequestBody CustomerDTO.UpdateCustomerRequest request) {
        CustomerDTO.CustomerResponse response = customerService.updateCustomer(uuid, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 软删除客户（设置为不活跃状态）
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID uuid) {
        customerService.deleteCustomer(uuid);
        return ResponseEntity.noContent().build();
    }

    /**
     * 彻底删除客户
     */
    @DeleteMapping("/{uuid}/hard")
    public ResponseEntity<Void> hardDeleteCustomer(@PathVariable UUID uuid) {
        customerService.hardDeleteCustomer(uuid);
        return ResponseEntity.noContent().build();
    }

    /**
     * 增加客户积分
     */
    @PostMapping("/{uuid}/points/add")
    public ResponseEntity<CustomerDTO.CustomerResponse> addPoints(
            @PathVariable UUID uuid,
            @RequestParam int points) {
        CustomerDTO.CustomerResponse response = customerService.addPoints(uuid, points);
        return ResponseEntity.ok(response);
    }

    /**
     * 扣除客户积分
     */
    @PostMapping("/{uuid}/points/deduct")
    public ResponseEntity<CustomerDTO.CustomerResponse> deductPoints(
            @PathVariable UUID uuid,
            @RequestParam int points) {
        CustomerDTO.CustomerResponse response = customerService.deductPoints(uuid, points);
        return ResponseEntity.ok(response);
    }

    /**
     * 更新客户访问信息
     */
    @PostMapping("/{uuid}/visit")
    public ResponseEntity<CustomerDTO.CustomerResponse> updateVisitInfo(
            @PathVariable UUID uuid,
            @RequestParam double spentAmount) {
        CustomerDTO.CustomerResponse response = customerService.updateVisitInfo(uuid, spentAmount);
        return ResponseEntity.ok(response);
    }

    /**
     * 获取客户统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<CustomerDTO.CustomerStatsResponse> getCustomerStats() {
        CustomerDTO.CustomerStatsResponse stats = customerService.getCustomerStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * 检查客户是否存在
     */
    @GetMapping("/{uuid}/exists")
    public ResponseEntity<Boolean> customerExists(@PathVariable UUID uuid) {
        boolean exists = customerService.customerExists(uuid);
        return ResponseEntity.ok(exists);
    }
}