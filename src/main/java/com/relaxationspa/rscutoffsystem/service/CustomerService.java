// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/service/CustomerService.java
package com.relaxationspa.rscutoffsystem.service;

import com.relaxationspa.rscutoffsystem.dto.CustomerDTO;
import com.relaxationspa.rscutoffsystem.entity.Customer;
import com.relaxationspa.rscutoffsystem.exception.ResourceNotFoundException;
import com.relaxationspa.rscutoffsystem.exception.ValidationException;
import com.relaxationspa.rscutoffsystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * 创建新客户
     */
    public CustomerDTO.CustomerResponse createCustomer(CustomerDTO.CreateCustomerRequest request) {
        // 验证电话唯一性（如果提供）
        if (request.getPhone() != null && customerRepository.existsByPhone(request.getPhone())) {
            throw new ValidationException("电话号码已被使用");
        }

        // 验证邮箱唯一性（如果提供）
        if (request.getEmail() != null && customerRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("邮箱已被使用");
        }

        // 验证会员信息
        if (request.getCustomerType() == Customer.CustomerType.MEMBER) {
            validateMembershipInfo(request.getMembershipLevel(),
                    request.getMembershipStartDate(),
                    request.getMembershipEndDate());
        }

        // 创建客户实体
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setGender(request.getGender());
        customer.setBirthday(request.getBirthday());
        customer.setCustomerType(request.getCustomerType());
        customer.setStatus(request.getStatus());
        customer.setNotes(request.getNotes());

        // 设置会员信息
        if (request.getCustomerType() == Customer.CustomerType.MEMBER) {
            customer.setMembershipLevel(request.getMembershipLevel());
            customer.setMembershipStartDate(request.getMembershipStartDate());
            customer.setMembershipEndDate(request.getMembershipEndDate());
        }

        // 设置积分
        if (request.getPoints() != null) {
            customer.setPoints(request.getPoints());
        }

        // 设置客户编号
        customer.setCustomerNumber(customerRepository.getNextCustomerNumber());

        Customer savedCustomer = customerRepository.save(customer);
        return new CustomerDTO.CustomerResponse(savedCustomer);
    }

    /**
     * 根据UUID获取客户
     */
    @Transactional(readOnly = true)
    public CustomerDTO.CustomerResponse getCustomerByUuid(UUID uuid) {
        Customer customer = customerRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("客户未找到: " + uuid));
        return new CustomerDTO.CustomerResponse(customer);
    }

    /**
     * 根据客户编号获取客户
     */
    @Transactional(readOnly = true)
    public CustomerDTO.CustomerResponse getCustomerByNumber(Long customerNumber) {
        Customer customer = customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(() -> new ResourceNotFoundException("客户未找到: " + customerNumber));
        return new CustomerDTO.CustomerResponse(customer);
    }

    /**
     * 根据电话号码获取客户
     */
    @Transactional(readOnly = true)
    public Optional<CustomerDTO.CustomerResponse> getCustomerByPhone(String phone) {
        return customerRepository.findByPhone(phone)
                .map(CustomerDTO.CustomerResponse::new);
    }

    /**
     * 根据姓名搜索客户
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO.CustomerResponse> searchCustomersByName(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name).stream()
                .map(CustomerDTO.CustomerResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有客户（分页）
     */
    @Transactional(readOnly = true)
    public Page<CustomerDTO.CustomerResponse> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(CustomerDTO.CustomerResponse::new);
    }

    /**
     * 获取所有活跃客户
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO.CustomerResponse> getAllActiveCustomers() {
        return customerRepository.findAllActiveCustomers().stream()
                .map(CustomerDTO.CustomerResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有会员客户
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO.CustomerResponse> getAllMembers() {
        return customerRepository.findAllMembers().stream()
                .map(CustomerDTO.CustomerResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有散客
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO.CustomerResponse> getAllWalkInCustomers() {
        return customerRepository.findAllWalkInCustomers().stream()
                .map(CustomerDTO.CustomerResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 根据客户类型获取客户
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO.CustomerResponse> getCustomersByType(Customer.CustomerType customerType) {
        return customerRepository.findByCustomerType(customerType).stream()
                .map(CustomerDTO.CustomerResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 根据客户状态获取客户
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO.CustomerResponse> getCustomersByStatus(Customer.CustomerStatus status) {
        return customerRepository.findByStatus(status).stream()
                .map(CustomerDTO.CustomerResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 根据会员等级获取客户
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO.CustomerResponse> getCustomersByMembershipLevel(Customer.MembershipLevel level) {
        return customerRepository.findByMembershipLevel(level).stream()
                .map(CustomerDTO.CustomerResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取即将到期的会员
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO.CustomerResponse> getMembersExpiringWithin(int days) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(days);
        return customerRepository.findMembersExpiringBetween(today, endDate).stream()
                .map(CustomerDTO.CustomerResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取今天生日的客户
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO.CustomerResponse> getCustomersWithBirthdayToday() {
        return customerRepository.findCustomersWithBirthdayToday().stream()
                .map(CustomerDTO.CustomerResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取指定月份生日的客户
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO.CustomerResponse> getCustomersWithBirthdayInMonth(int month) {
        return customerRepository.findCustomersWithBirthdayInMonth(month).stream()
                .map(CustomerDTO.CustomerResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取不活跃客户（指定天数内未访问）
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO.CustomerResponse> getInactiveCustomers(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return customerRepository.findInactiveCustomersBefore(cutoffDate).stream()
                .map(CustomerDTO.CustomerResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 更新客户信息
     */
    public CustomerDTO.CustomerResponse updateCustomer(UUID uuid, CustomerDTO.UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("客户未找到: " + uuid));

        // 更新基本信息
        if (request.getName() != null) {
            customer.setName(request.getName());
        }

        // 更新电话（检查唯一性）
        if (request.getPhone() != null && !request.getPhone().equals(customer.getPhone())) {
            if (customerRepository.existsByPhone(request.getPhone())) {
                throw new ValidationException("电话号码已被使用");
            }
            customer.setPhone(request.getPhone());
        }

        // 更新邮箱（检查唯一性）
        if (request.getEmail() != null && !request.getEmail().equals(customer.getEmail())) {
            if (customerRepository.existsByEmail(request.getEmail())) {
                throw new ValidationException("邮箱已被使用");
            }
            customer.setEmail(request.getEmail());
        }

        // 更新其他字段
        if (request.getGender() != null) {
            customer.setGender(request.getGender());
        }

        if (request.getBirthday() != null) {
            customer.setBirthday(request.getBirthday());
        }

        if (request.getCustomerType() != null) {
            customer.setCustomerType(request.getCustomerType());
        }

        if (request.getStatus() != null) {
            customer.setStatus(request.getStatus());
        }

        if (request.getNotes() != null) {
            customer.setNotes(request.getNotes());
        }

        // 更新会员信息
        if (request.getMembershipLevel() != null) {
            customer.setMembershipLevel(request.getMembershipLevel());
        }

        if (request.getMembershipStartDate() != null) {
            customer.setMembershipStartDate(request.getMembershipStartDate());
        }

        if (request.getMembershipEndDate() != null) {
            customer.setMembershipEndDate(request.getMembershipEndDate());
        }

        if (request.getPoints() != null) {
            customer.setPoints(request.getPoints());
        }

        Customer updatedCustomer = customerRepository.save(customer);
        return new CustomerDTO.CustomerResponse(updatedCustomer);
    }

    /**
     * 删除客户（软删除 - 设置为不活跃状态）
     */
    public void deleteCustomer(UUID uuid) {
        Customer customer = customerRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("客户未找到: " + uuid));

        customer.setStatus(Customer.CustomerStatus.INACTIVE);
        customerRepository.save(customer);
    }

    /**
     * 彻底删除客户
     */
    public void hardDeleteCustomer(UUID uuid) {
        if (!customerRepository.existsById(uuid)) {
            throw new ResourceNotFoundException("客户未找到: " + uuid);
        }
        customerRepository.deleteById(uuid);
    }

    /**
     * 增加客户积分
     */
    public CustomerDTO.CustomerResponse addPoints(UUID uuid, int points) {
        Customer customer = customerRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("客户未找到: " + uuid));

        if (customer.getCustomerType() != Customer.CustomerType.MEMBER) {
            throw new ValidationException("只有会员客户才能累积积分");
        }

        customer.setPoints(customer.getPoints() + points);
        Customer updatedCustomer = customerRepository.save(customer);
        return new CustomerDTO.CustomerResponse(updatedCustomer);
    }

    /**
     * 扣除客户积分
     */
    public CustomerDTO.CustomerResponse deductPoints(UUID uuid, int points) {
        Customer customer = customerRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("客户未找到: " + uuid));

        if (customer.getCustomerType() != Customer.CustomerType.MEMBER) {
            throw new ValidationException("只有会员客户才能使用积分");
        }

        if (customer.getPoints() < points) {
            throw new ValidationException("积分不足");
        }

        customer.setPoints(customer.getPoints() - points);
        Customer updatedCustomer = customerRepository.save(customer);
        return new CustomerDTO.CustomerResponse(updatedCustomer);
    }

    /**
     * 更新客户访问信息
     */
    public CustomerDTO.CustomerResponse updateVisitInfo(UUID uuid, double spentAmount) {
        Customer customer = customerRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("客户未找到: " + uuid));

        // 更新访问次数
        customer.setVisitCount(customer.getVisitCount() + 1);

        // 更新最后访问时间
        customer.setLastVisitDate(LocalDateTime.now());

        // 更新消费总额
        customer.setTotalSpent(customer.getTotalSpent() + spentAmount);

        // 如果是会员，根据消费金额增加积分（1元=1积分）
        if (customer.getCustomerType() == Customer.CustomerType.MEMBER) {
            int earnedPoints = (int) spentAmount;
            customer.setPoints(customer.getPoints() + earnedPoints);
        }

        Customer updatedCustomer = customerRepository.save(customer);
        return new CustomerDTO.CustomerResponse(updatedCustomer);
    }

    /**
     * 获取客户统计信息
     */
    @Transactional(readOnly = true)
    public CustomerDTO.CustomerStatsResponse getCustomerStats() {
        CustomerDTO.CustomerStatsResponse stats = new CustomerDTO.CustomerStatsResponse();

        // 总客户数
        stats.setTotalCustomers(customerRepository.count());

        // 按类型统计
        List<Object[]> typeStats = customerRepository.countByCustomerType();
        for (Object[] stat : typeStats) {
            Customer.CustomerType type = (Customer.CustomerType) stat[0];
            Long count = (Long) stat[1];

            if (type == Customer.CustomerType.WALK_IN) {
                stats.setWalkInCustomers(count);
            } else if (type == Customer.CustomerType.MEMBER) {
                stats.setMemberCustomers(count);
            }
        }

        // 按状态统计
        List<Object[]> statusStats = customerRepository.countByStatus();
        for (Object[] stat : statusStats) {
            Customer.CustomerStatus status = (Customer.CustomerStatus) stat[0];
            Long count = (Long) stat[1];

            switch (status) {
                case ACTIVE:
                    stats.setActiveCustomers(count);
                    break;
                case INACTIVE:
                    stats.setInactiveCustomers(count);
                    break;
                case BLOCKED:
                    stats.setBlockedCustomers(count);
                    break;
            }
        }

        // 按会员等级统计
        List<Object[]> levelStats = customerRepository.countByMembershipLevel();
        for (Object[] stat : levelStats) {
            Customer.MembershipLevel level = (Customer.MembershipLevel) stat[0];
            Long count = (Long) stat[1];

            if (level != null) {
                switch (level) {
                    case BRONZE:
                        stats.setBronzeMembers(count);
                        break;
                    case SILVER:
                        stats.setSilverMembers(count);
                        break;
                    case GOLD:
                        stats.setGoldMembers(count);
                        break;
                    case PLATINUM:
                        stats.setPlatinumMembers(count);
                        break;
                    case DIAMOND:
                        stats.setDiamondMembers(count);
                        break;
                }
            }
        }

        return stats;
    }

    /**
     * 验证会员信息
     */
    private void validateMembershipInfo(Customer.MembershipLevel level, LocalDate startDate, LocalDate endDate) {
        if (level == null) {
            throw new ValidationException("会员等级不能为空");
        }

        if (startDate == null) {
            throw new ValidationException("会员开始日期不能为空");
        }

        if (endDate == null) {
            throw new ValidationException("会员结束日期不能为空");
        }

        if (endDate.isBefore(startDate)) {
            throw new ValidationException("会员结束日期不能早于开始日期");
        }
    }

    /**
     * 检查客户是否存在
     */
    @Transactional(readOnly = true)
    public boolean customerExists(UUID uuid) {
        return customerRepository.existsById(uuid);
    }
}