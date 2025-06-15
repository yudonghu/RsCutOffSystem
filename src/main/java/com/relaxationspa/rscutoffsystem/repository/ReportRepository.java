// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/repository/ReportRepository.java
package com.relaxationspa.rscutoffsystem.repository;

import com.relaxationspa.rscutoffsystem.entity.Report;
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
public interface ReportRepository extends JpaRepository<Report, UUID> {

    // 根据报表名称查找
    List<Report> findByReportNameContainingIgnoreCase(String reportName);

    // 根据报表类型查找
    List<Report> findByReportType(Report.ReportType reportType);

    // 根据周期类型查找
    List<Report> findByPeriodType(Report.PeriodType periodType);

    // 根据报表状态查找
    List<Report> findByStatus(Report.ReportStatus status);

    // 根据生成者查找
    List<Report> findByGeneratedByUuid(UUID generatedByUuid);

    // 根据日期范围查找
    List<Report> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    List<Report> findByEndDateBetween(LocalDate startDate, LocalDate endDate);

    List<Report> findByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);

    // 查找所有待生成的报表
    @Query("SELECT r FROM Report r WHERE r.status = 'PENDING' ORDER BY r.createdAt ASC")
    List<Report> findAllPendingReports();

    // 查找所有生成中的报表
    @Query("SELECT r FROM Report r WHERE r.status = 'GENERATING'")
    List<Report> findAllGeneratingReports();

    // 查找所有已完成的报表
    @Query("SELECT r FROM Report r WHERE r.status = 'COMPLETED' ORDER BY r.completedAt DESC")
    List<Report> findAllCompletedReports();

    // 查找所有失败的报表
    @Query("SELECT r FROM Report r WHERE r.status = 'FAILED' ORDER BY r.createdAt DESC")
    List<Report> findAllFailedReports();

    // 根据报表类型和状态查找
    List<Report> findByReportTypeAndStatus(Report.ReportType reportType, Report.ReportStatus status);

    // 查找指定用户生成的报表
    @Query("SELECT r FROM Report r WHERE r.generatedByUuid = :userUuid ORDER BY r.createdAt DESC")
    List<Report> findReportsByUser(@Param("userUuid") UUID userUuid);

    // 查找指定日期范围内创建的报表
    @Query("SELECT r FROM Report r WHERE r.createdAt BETWEEN :startTime AND :endTime ORDER BY r.createdAt DESC")
    List<Report> findReportsCreatedBetween(@Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);

    // 查找指定日期范围内完成的报表
    @Query("SELECT r FROM Report r WHERE r.completedAt BETWEEN :startTime AND :endTime ORDER BY r.completedAt DESC")
    List<Report> findReportsCompletedBetween(@Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);

    // 查找长时间运行的报表（超过指定分钟数仍在生成中）
    @Query("SELECT r FROM Report r WHERE r.status = 'GENERATING' AND r.createdAt < :cutoffTime")
    List<Report> findLongRunningReports(@Param("cutoffTime") LocalDateTime cutoffTime);

    // 查找最近的报表
    @Query("SELECT r FROM Report r ORDER BY r.createdAt DESC")
    List<Report> findRecentReports();

    // 查找最近完成的报表
    @Query("SELECT r FROM Report r WHERE r.status = 'COMPLETED' ORDER BY r.completedAt DESC")
    List<Report> findRecentCompletedReports();

    // 根据报表类型和日期范围查找
    @Query("SELECT r FROM Report r WHERE r.reportType = :reportType " +
            "AND r.startDate >= :startDate AND r.endDate <= :endDate " +
            "ORDER BY r.createdAt DESC")
    List<Report> findByTypeAndDateRange(@Param("reportType") Report.ReportType reportType,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    // 查找重复的报表（相同类型、周期和日期范围）
    @Query("SELECT r FROM Report r WHERE r.reportType = :reportType " +
            "AND r.periodType = :periodType " +
            "AND r.startDate = :startDate AND r.endDate = :endDate " +
            "AND r.status IN ('PENDING', 'GENERATING', 'COMPLETED')")
    List<Report> findDuplicateReports(@Param("reportType") Report.ReportType reportType,
                                      @Param("periodType") Report.PeriodType periodType,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);

    // 统计各种状态的报表数量
    @Query("SELECT r.status, COUNT(r) FROM Report r GROUP BY r.status")
    List<Object[]> countByStatus();

    // 统计各种类型的报表数量
    @Query("SELECT r.reportType, COUNT(r) FROM Report r GROUP BY r.reportType")
    List<Object[]> countByReportType();

    // 统计各种周期的报表数量
    @Query("SELECT r.periodType, COUNT(r) FROM Report r GROUP BY r.periodType")
    List<Object[]> countByPeriodType();

    // 统计指定用户生成的报表数量
    @Query("SELECT COUNT(r) FROM Report r WHERE r.generatedByUuid = :userUuid")
    Long countByUser(@Param("userUuid") UUID userUuid);

    // 检查是否存在指定文件路径的报表
    boolean existsByFilePath(String filePath);

    // 注释掉或删除以下有问题的方法：
    /*
    // 计算平均生成时间（毫秒）
    @Query("SELECT AVG(r.completedAt - r.createdAt) FROM Report r WHERE r.status = 'COMPLETED' AND r.completedAt IS NOT NULL")
    Double calculateAverageGenerationTime();

    // 查找生成时间最长的报表
    @Query("SELECT r FROM Report r WHERE r.status = 'COMPLETED' AND r.completedAt IS NOT NULL " +
            "ORDER BY (r.completedAt - r.createdAt) DESC LIMIT 1")
    Optional<Report> findLongestGenerationTimeReport();

    // 查找生成时间最短的报表
    @Query("SELECT r FROM Report r WHERE r.status = 'COMPLETED' AND r.completedAt IS NOT NULL " +
            "ORDER BY (r.completedAt - r.createdAt) ASC LIMIT 1")
    Optional<Report> findShortestGenerationTimeReport();

    // 查找今天创建的报表
    @Query("SELECT r FROM Report r WHERE DATE(r.createdAt) = CURRENT_DATE ORDER BY r.createdAt DESC")
    List<Report> findTodayReports();

    // 查找本周创建的报表
    @Query("SELECT r FROM Report r WHERE YEAR(r.createdAt) = YEAR(CURRENT_DATE) " +
            "AND WEEK(r.createdAt) = WEEK(CURRENT_DATE) ORDER BY r.createdAt DESC")
    List<Report> findThisWeekReports();

    // 查找本月创建的报表
    @Query("SELECT r FROM Report r WHERE YEAR(r.createdAt) = YEAR(CURRENT_DATE) " +
            "AND MONTH(r.createdAt) = MONTH(CURRENT_DATE) ORDER BY r.createdAt DESC")
    List<Report> findThisMonthReports();
    */
}