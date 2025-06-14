// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/repository/TransactionRepository.java
package com.relaxationspa.rscutoffsystem.repository;

import com.relaxationspa.rscutoffsystem.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    // 根据交易编号查找
    Optional<Transaction> findByTransactionNumber(String transactionNumber);

    // 根据交易类型查找
    List<Transaction> findByType(Transaction.TransactionType type);

    // 根据交易状态查找
    List<Transaction> findByStatus(Transaction.TransactionStatus status);

    // 根据支付方式查找
    List<Transaction> findByPaymentMethod(Transaction.PaymentMethod paymentMethod);

    // 根据交易日期查找
    List<Transaction> findByTransactionDate(LocalDate transactionDate);

    // 根据交易日期范围查找
    List<Transaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);

    // 根据关联客户查找
    List<Transaction> findByRelatedCustomerUuid(UUID customerUuid);

    // 根据关联员工查找
    List<Transaction> findByRelatedUserUuid(UUID userUuid);

    // 查找已确认的交易
    @Query("SELECT t FROM Transaction t WHERE t.status = 'CONFIRMED'")
    List<Transaction> findAllConfirmedTransactions();

    // 查找待确认的交易
    @Query("SELECT t FROM Transaction t WHERE t.status = 'PENDING'")
    List<Transaction> findAllPendingTransactions();

    // 查找未包含在日结中的交易
    @Query("SELECT t FROM Transaction t WHERE t.isIncludedInCutOff = false AND t.status = 'CONFIRMED'")
    List<Transaction> findUnprocessedTransactions();

    // 根据日结日期查找
    List<Transaction> findByCutOffDate(LocalDate cutOffDate);

    // 查找指定日期的所有已确认交易
    @Query("SELECT t FROM Transaction t WHERE t.transactionDate = :date AND t.status = 'CONFIRMED'")
    List<Transaction> findConfirmedTransactionsByDate(@Param("date") LocalDate date);

    // 查找指定日期范围的收入交易
    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate " +
            "AND t.type = 'INCOME' AND t.status = 'CONFIRMED'")
    List<Transaction> findIncomeTransactionsBetween(@Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);

    // 查找指定日期范围的支出交易
    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate " +
            "AND t.type = 'EXPENSE' AND t.status = 'CONFIRMED'")
    List<Transaction> findExpenseTransactionsBetween(@Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate);

    // 计算指定日期的总收入
    @Query("SELECT COALESCE(SUM(t.totalAmount), 0) FROM Transaction t " +
            "WHERE t.transactionDate = :date AND t.type = 'INCOME' AND t.status = 'CONFIRMED'")
    BigDecimal calculateDailyIncome(@Param("date") LocalDate date);

    // 计算指定日期的总支出
    @Query("SELECT COALESCE(SUM(t.totalAmount), 0) FROM Transaction t " +
            "WHERE t.transactionDate = :date AND t.type = 'EXPENSE' AND t.status = 'CONFIRMED'")
    BigDecimal calculateDailyExpense(@Param("date") LocalDate date);

    // 计算指定日期范围的总收入
    @Query("SELECT COALESCE(SUM(t.totalAmount), 0) FROM Transaction t " +
            "WHERE t.transactionDate BETWEEN :startDate AND :endDate " +
            "AND t.type = 'INCOME' AND t.status = 'CONFIRMED'")
    BigDecimal calculateIncomeForPeriod(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    // 计算指定日期范围的总支出
    @Query("SELECT COALESCE(SUM(t.totalAmount), 0) FROM Transaction t " +
            "WHERE t.transactionDate BETWEEN :startDate AND :endDate " +
            "AND t.type = 'EXPENSE' AND t.status = 'CONFIRMED'")
    BigDecimal calculateExpenseForPeriod(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    // 根据支付方式统计金额
    @Query("SELECT t.paymentMethod, COALESCE(SUM(t.totalAmount), 0) FROM Transaction t " +
            "WHERE t.transactionDate = :date AND t.status = 'CONFIRMED' " +
            "GROUP BY t.paymentMethod")
    List<Object[]> calculateDailyAmountByPaymentMethod(@Param("date") LocalDate date);

    // 查找指定客户的所有交易
    @Query("SELECT t FROM Transaction t WHERE t.relatedCustomerUuid = :customerUuid " +
            "ORDER BY t.transactionDate DESC")
    List<Transaction> findCustomerTransactionHistory(@Param("customerUuid") UUID customerUuid);

    // 查找指定员工操作的所有交易
    @Query("SELECT t FROM Transaction t WHERE t.relatedUserUuid = :userUuid " +
            "ORDER BY t.transactionDate DESC")
    List<Transaction> findUserTransactionHistory(@Param("userUuid") UUID userUuid);

    // 查找大额交易（超过指定金额）
    @Query("SELECT t FROM Transaction t WHERE t.totalAmount >= :amount AND t.status = 'CONFIRMED' " +
            "ORDER BY t.totalAmount DESC")
    List<Transaction> findLargeTransactions(@Param("amount") BigDecimal amount);

    // 检查交易编号是否存在
    boolean existsByTransactionNumber(String transactionNumber);

    // 获取下一个交易编号（基于日期）
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.transactionDate = :date")
    Long getTransactionCountForDate(@Param("date") LocalDate date);

    // 统计各种交易状态的数量
    @Query("SELECT t.status, COUNT(t) FROM Transaction t GROUP BY t.status")
    List<Object[]> countByStatus();

    // 统计各种支付方式的数量
    @Query("SELECT t.paymentMethod, COUNT(t) FROM Transaction t WHERE t.status = 'CONFIRMED' GROUP BY t.paymentMethod")
    List<Object[]> countByPaymentMethod();

    // 查找最近的交易
    @Query("SELECT t FROM Transaction t ORDER BY t.createdAt DESC")
    List<Transaction> findRecentTransactions();
}