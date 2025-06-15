// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/repository/ServiceRepository.java
package com.relaxationspa.rscutoffsystem.repository;

import com.relaxationspa.rscutoffsystem.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {

    // 根据服务代码查找
    Optional<Service> findByServiceCode(String serviceCode);

    // 根据服务名称查找
    List<Service> findByServiceNameContainingIgnoreCase(String serviceName);

    // 根据服务类型查找
    List<Service> findByServiceType(Service.ServiceType serviceType);

    // 根据服务状态查找
    List<Service> findByStatus(Service.ServiceStatus status);

    // 根据时长查找
    List<Service> findByDurationMinutes(Integer durationMinutes);

    // 根据价格范围查找
    List<Service> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // 查找所有可用服务
    @Query("SELECT s FROM Service s WHERE s.status = 'ACTIVE' ORDER BY s.displayOrder ASC, s.serviceType ASC, s.durationMinutes ASC")
    List<Service> findAllActiveServices();

    // 根据服务类型查找可用服务
    @Query("SELECT s FROM Service s WHERE s.serviceType = :serviceType AND s.status = 'ACTIVE' ORDER BY s.durationMinutes ASC")
    List<Service> findActiveServicesByType(@Param("serviceType") Service.ServiceType serviceType);

    // 查找椅背按摩服务
    @Query("SELECT s FROM Service s WHERE s.serviceType = 'CHAIR_MASSAGE' AND s.status = 'ACTIVE' ORDER BY s.durationMinutes ASC")
    List<Service> findChairMassageServices();

    // 查找足疗服务
    @Query("SELECT s FROM Service s WHERE s.serviceType = 'FOOT_REFLEXOLOGY' AND s.status = 'ACTIVE' ORDER BY s.durationMinutes ASC")
    List<Service> findFootReflexologyServices();

    // 查找身体按摩服务
    @Query("SELECT s FROM Service s WHERE s.serviceType = 'BODY_MASSAGE' AND s.status = 'ACTIVE' ORDER BY s.durationMinutes ASC")
    List<Service> findBodyMassageServices();

    // 查找组合按摩服务
    @Query("SELECT s FROM Service s WHERE s.serviceType = 'COMBINATION_MASSAGE' AND s.status = 'ACTIVE' ORDER BY s.durationMinutes ASC")
    List<Service> findCombinationMassageServices();

    // 查找组合服务
    @Query("SELECT s FROM Service s WHERE s.isCombination = true AND s.status = 'ACTIVE'")
    List<Service> findCombinationServices();

    // 根据时长范围查找服务
    @Query("SELECT s FROM Service s WHERE s.durationMinutes BETWEEN :minDuration AND :maxDuration AND s.status = 'ACTIVE' ORDER BY s.durationMinutes ASC")
    List<Service> findServicesByDurationRange(@Param("minDuration") Integer minDuration, @Param("maxDuration") Integer maxDuration);

    // 查找指定价格以下的服务
    @Query("SELECT s FROM Service s WHERE s.price <= :maxPrice AND s.status = 'ACTIVE' ORDER BY s.price ASC")
    List<Service> findServicesByMaxPrice(@Param("maxPrice") BigDecimal maxPrice);

    // 查找指定价格以上的服务
    @Query("SELECT s FROM Service s WHERE s.price >= :minPrice AND s.status = 'ACTIVE' ORDER BY s.price ASC")
    List<Service> findServicesByMinPrice(@Param("minPrice") BigDecimal minPrice);

    // 查找最受欢迎的服务（根据某种排序逻辑，这里按displayOrder）
    @Query("SELECT s FROM Service s WHERE s.status = 'ACTIVE' ORDER BY s.displayOrder ASC LIMIT :limit")
    List<Service> findPopularServices(@Param("limit") Integer limit);

    // 查找快速服务（30分钟以下）
    @Query("SELECT s FROM Service s WHERE s.durationMinutes <= 30 AND s.status = 'ACTIVE' ORDER BY s.durationMinutes ASC")
    List<Service> findQuickServices();

    // 查找长时间服务（60分钟以上）
    @Query("SELECT s FROM Service s WHERE s.durationMinutes >= 60 AND s.status = 'ACTIVE' ORDER BY s.durationMinutes ASC")
    List<Service> findLongServices();

    // 检查服务代码是否存在
    boolean existsByServiceCode(String serviceCode);

    // 检查服务名称是否存在
    boolean existsByServiceName(String serviceName);

    // 统计各类型服务数量
    @Query("SELECT s.serviceType, COUNT(s) FROM Service s WHERE s.status = 'ACTIVE' GROUP BY s.serviceType")
    List<Object[]> countActiveServicesByType();

    // 统计各状态服务数量
    @Query("SELECT s.status, COUNT(s) FROM Service s GROUP BY s.status")
    List<Object[]> countServicesByStatus();

    // 计算平均价格
    @Query("SELECT AVG(s.price) FROM Service s WHERE s.status = 'ACTIVE'")
    BigDecimal calculateAveragePrice();

    // 计算各类型平均价格
    @Query("SELECT s.serviceType, AVG(s.price) FROM Service s WHERE s.status = 'ACTIVE' GROUP BY s.serviceType")
    List<Object[]> calculateAveragePriceByType();

    // 查找价格最高的服务
    @Query("SELECT s FROM Service s WHERE s.status = 'ACTIVE' ORDER BY s.price DESC LIMIT 1")
    Optional<Service> findMostExpensiveService();

    // 查找价格最低的服务
    @Query("SELECT s FROM Service s WHERE s.status = 'ACTIVE' ORDER BY s.price ASC LIMIT 1")
    Optional<Service> findLeastExpensiveService();

    // 根据显示顺序查找
    List<Service> findByDisplayOrderNotNullOrderByDisplayOrderAsc();

    // 获取下一个显示顺序
    @Query("SELECT COALESCE(MAX(s.displayOrder), 0) + 1 FROM Service s")
    Integer getNextDisplayOrder();
}