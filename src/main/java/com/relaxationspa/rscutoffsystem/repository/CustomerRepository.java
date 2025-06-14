// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/repository/CustomerRepository.java
package com.relaxationspa.rscutoffsystem.repository;

import com.relaxationspa.rscutoffsystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    // 根据客户编号查找
    Optional<Customer> findByCustomerNumber(Long customerNumber);

    // 根据姓名查找
    List<Customer> findByName(String name);

    // 根据姓名模糊查找
    List<Customer> findByNameContainingIgnoreCase(String name);

    // 根据电话号码查找
    Optional<Customer> findByPhone(String phone);

    // 根据邮箱查找
    Optional<Customer> findByEmail(String email);

    // 根据客户类型查找
    List<Customer> findByCustomerType(Customer.CustomerType customerType);

    // 根据客户状态查找
    List<Customer> findByStatus(Customer.CustomerStatus status);

    // 根据会员等级查找
    List<Customer> findByMembershipLevel(Customer.MembershipLevel membershipLevel);

    // 查找所有活跃客户
    @Query("SELECT c FROM Customer c WHERE c.status = 'ACTIVE'")
    List<Customer> findAllActiveCustomers();

    // 查找所有会员客户
    @Query("SELECT c FROM Customer c WHERE c.customerType = 'MEMBER'")
    List<Customer> findAllMembers();

    // 查找所有散客
    @Query("SELECT c FROM Customer c WHERE c.customerType = 'WALK_IN'")
    List<Customer> findAllWalkInCustomers();

    // 查找会员即将到期的客户（30天内）
    @Query("SELECT c FROM Customer c WHERE c.customerType = 'MEMBER' " +
            "AND c.membershipEndDate BETWEEN :startDate AND :endDate")
    List<Customer> findMembersExpiringBetween(@Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);

    // 查找积分大于指定值的客户
    @Query("SELECT c FROM Customer c WHERE c.points >= :minPoints ORDER BY c.points DESC")
    List<Customer> findByPointsGreaterThanEqual(@Param("minPoints") Integer minPoints);

    // 查找消费总额大于指定值的客户
    @Query("SELECT c FROM Customer c WHERE c.totalSpent >= :minSpent ORDER BY c.totalSpent DESC")
    List<Customer> findByTotalSpentGreaterThanEqual(@Param("minSpent") Double minSpent);

    // 查找访问次数大于指定值的客户
    @Query("SELECT c FROM Customer c WHERE c.visitCount >= :minVisits ORDER BY c.visitCount DESC")
    List<Customer> findByVisitCountGreaterThanEqual(@Param("minVisits") Integer minVisits);

    // 查找最后访问时间在指定日期之前的客户（不活跃客户）
    @Query("SELECT c FROM Customer c WHERE c.lastVisitDate < :beforeDate")
    List<Customer> findInactiveCustomersBefore(@Param("beforeDate") LocalDateTime beforeDate);

    // 查找生日在指定月份的客户
    @Query("SELECT c FROM Customer c WHERE MONTH(c.birthday) = :month")
    List<Customer> findCustomersWithBirthdayInMonth(@Param("month") int month);

    // 查找今天生日的客户
    @Query("SELECT c FROM Customer c WHERE MONTH(c.birthday) = MONTH(CURRENT_DATE) " +
            "AND DAY(c.birthday) = DAY(CURRENT_DATE)")
    List<Customer> findCustomersWithBirthdayToday();

    // 根据会员等级和状态查找
    List<Customer> findByMembershipLevelAndStatus(Customer.MembershipLevel membershipLevel,
                                                  Customer.CustomerStatus status);

    // 检查电话号码是否存在
    boolean existsByPhone(String phone);

    // 检查邮箱是否存在
    boolean existsByEmail(String email);

    // 检查客户编号是否存在
    boolean existsByCustomerNumber(Long customerNumber);

    // 获取下一个客户编号
    @Query("SELECT COALESCE(MAX(c.customerNumber), 0) + 1 FROM Customer c")
    Long getNextCustomerNumber();

    // 统计各类型客户数量
    @Query("SELECT c.customerType, COUNT(c) FROM Customer c GROUP BY c.customerType")
    List<Object[]> countByCustomerType();

    // 统计各状态客户数量
    @Query("SELECT c.status, COUNT(c) FROM Customer c GROUP BY c.status")
    List<Object[]> countByStatus();

    // 统计各会员等级数量
    @Query("SELECT c.membershipLevel, COUNT(c) FROM Customer c WHERE c.customerType = 'MEMBER' GROUP BY c.membershipLevel")
    List<Object[]> countByMembershipLevel();
}