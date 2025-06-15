// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/service/ServiceService.java
package com.relaxationspa.rscutoffsystem.service;

import com.relaxationspa.rscutoffsystem.dto.ServiceDTO;
import com.relaxationspa.rscutoffsystem.entity.Service;
import com.relaxationspa.rscutoffsystem.exception.ResourceNotFoundException;
import com.relaxationspa.rscutoffsystem.exception.ValidationException;
import com.relaxationspa.rscutoffsystem.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Transactional
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    /**
     * 创建新服务
     */
    public ServiceDTO.ServiceResponse createService(ServiceDTO.CreateServiceRequest request) {
        // 验证服务代码唯一性
        if (serviceRepository.existsByServiceCode(request.getServiceCode())) {
            throw new ValidationException("服务代码已存在");
        }

        // 验证服务名称唯一性
        if (serviceRepository.existsByServiceName(request.getServiceName())) {
            throw new ValidationException("服务名称已存在");
        }

        // 创建服务实体
        Service service = new Service();
        service.setServiceCode(request.getServiceCode());
        service.setServiceName(request.getServiceName());
        service.setServiceType(request.getServiceType());
        service.setDurationMinutes(request.getDurationMinutes());
        service.setPrice(request.getPrice());
        service.setDescription(request.getDescription());
        service.setStatus(request.getStatus());
        service.setIsCombination(request.getIsCombination());
        service.setCombinationDescription(request.getCombinationDescription());
        service.setNotes(request.getNotes());

        // 设置显示顺序
        if (request.getDisplayOrder() != null) {
            service.setDisplayOrder(request.getDisplayOrder());
        } else {
            service.setDisplayOrder(serviceRepository.getNextDisplayOrder());
        }

        Service savedService = serviceRepository.save(service);
        return new ServiceDTO.ServiceResponse(savedService);
    }

    /**
     * 根据UUID获取服务
     */
    @Transactional(readOnly = true)
    public ServiceDTO.ServiceResponse getServiceByUuid(UUID uuid) {
        Service service = serviceRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("服务未找到: " + uuid));
        return new ServiceDTO.ServiceResponse(service);
    }

    /**
     * 根据服务代码获取服务
     */
    @Transactional(readOnly = true)
    public ServiceDTO.ServiceResponse getServiceByCode(String serviceCode) {
        Service service = serviceRepository.findByServiceCode(serviceCode)
                .orElseThrow(() -> new ResourceNotFoundException("服务未找到: " + serviceCode));
        return new ServiceDTO.ServiceResponse(service);
    }

    /**
     * 获取所有服务（分页）
     */
    @Transactional(readOnly = true)
    public Page<ServiceDTO.ServiceResponse> getAllServices(Pageable pageable) {
        return serviceRepository.findAll(pageable)
                .map(ServiceDTO.ServiceResponse::new);
    }

    /**
     * 获取所有可用服务
     */
    @Transactional(readOnly = true)
    public List<ServiceDTO.ServiceResponse> getAllActiveServices() {
        return serviceRepository.findAllActiveServices().stream()
                .map(ServiceDTO.ServiceResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 根据服务类型获取服务
     */
    @Transactional(readOnly = true)
    public List<ServiceDTO.ServiceResponse> getServicesByType(Service.ServiceType serviceType) {
        return serviceRepository.findActiveServicesByType(serviceType).stream()
                .map(ServiceDTO.ServiceResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取椅背按摩服务
     */
    @Transactional(readOnly = true)
    public List<ServiceDTO.ServiceResponse> getChairMassageServices() {
        return serviceRepository.findChairMassageServices().stream()
                .map(ServiceDTO.ServiceResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取足疗服务
     */
    @Transactional(readOnly = true)
    public List<ServiceDTO.ServiceResponse> getFootReflexologyServices() {
        return serviceRepository.findFootReflexologyServices().stream()
                .map(ServiceDTO.ServiceResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取身体按摩服务
     */
    @Transactional(readOnly = true)
    public List<ServiceDTO.ServiceResponse> getBodyMassageServices() {
        return serviceRepository.findBodyMassageServices().stream()
                .map(ServiceDTO.ServiceResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取组合按摩服务
     */
    @Transactional(readOnly = true)
    public List<ServiceDTO.ServiceResponse> getCombinationMassageServices() {
        return serviceRepository.findCombinationMassageServices().stream()
                .map(ServiceDTO.ServiceResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 根据价格范围获取服务
     */
    @Transactional(readOnly = true)
    public List<ServiceDTO.ServiceResponse> getServicesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return serviceRepository.findByPriceBetween(minPrice, maxPrice).stream()
                .map(ServiceDTO.ServiceResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 根据时长范围获取服务
     */
    @Transactional(readOnly = true)
    public List<ServiceDTO.ServiceResponse> getServicesByDurationRange(Integer minDuration, Integer maxDuration) {
        return serviceRepository.findServicesByDurationRange(minDuration, maxDuration).stream()
                .map(ServiceDTO.ServiceResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 搜索服务
     */
    @Transactional(readOnly = true)
    public List<ServiceDTO.ServiceResponse> searchServices(String keyword) {
        return serviceRepository.findByServiceNameContainingIgnoreCase(keyword).stream()
                .map(ServiceDTO.ServiceResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取快速服务（30分钟以下）
     */
    @Transactional(readOnly = true)
    public List<ServiceDTO.ServiceResponse> getQuickServices() {
        return serviceRepository.findQuickServices().stream()
                .map(ServiceDTO.ServiceResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取长时间服务（60分钟以上）
     */
    @Transactional(readOnly = true)
    public List<ServiceDTO.ServiceResponse> getLongServices() {
        return serviceRepository.findLongServices().stream()
                .map(ServiceDTO.ServiceResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取服务菜单（按类型分组）
     */
    @Transactional(readOnly = true)
    public List<ServiceDTO.ServiceMenuResponse> getServiceMenu() {
        List<ServiceDTO.ServiceMenuResponse> menu = new ArrayList<>();

        // 按服务类型分组
        for (Service.ServiceType serviceType : Service.ServiceType.values()) {
            List<ServiceDTO.ServiceResponse> services = getServicesByType(serviceType);
            if (!services.isEmpty()) {
                ServiceDTO.ServiceMenuResponse menuItem = new ServiceDTO.ServiceMenuResponse();
                menuItem.setServiceType(serviceType);
                menuItem.setServiceTypeDisplayName(getServiceTypeDisplayName(serviceType));
                menuItem.setServices(services);
                menu.add(menuItem);
            }
        }

        return menu;
    }

    /**
     * 更新服务
     */
    public ServiceDTO.ServiceResponse updateService(UUID uuid, ServiceDTO.UpdateServiceRequest request) {
        Service service = serviceRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("服务未找到: " + uuid));

        // 更新服务名称（检查唯一性）
        if (request.getServiceName() != null && !request.getServiceName().equals(service.getServiceName())) {
            if (serviceRepository.existsByServiceName(request.getServiceName())) {
                throw new ValidationException("服务名称已存在");
            }
            service.setServiceName(request.getServiceName());
        }

        // 更新其他字段
        if (request.getServiceType() != null) {
            service.setServiceType(request.getServiceType());
        }

        if (request.getDurationMinutes() != null) {
            service.setDurationMinutes(request.getDurationMinutes());
        }

        if (request.getPrice() != null) {
            service.setPrice(request.getPrice());
        }

        if (request.getDescription() != null) {
            service.setDescription(request.getDescription());
        }

        if (request.getStatus() != null) {
            service.setStatus(request.getStatus());
        }

        if (request.getIsCombination() != null) {
            service.setIsCombination(request.getIsCombination());
        }

        if (request.getCombinationDescription() != null) {
            service.setCombinationDescription(request.getCombinationDescription());
        }

        if (request.getDisplayOrder() != null) {
            service.setDisplayOrder(request.getDisplayOrder());
        }

        if (request.getNotes() != null) {
            service.setNotes(request.getNotes());
        }

        Service updatedService = serviceRepository.save(service);
        return new ServiceDTO.ServiceResponse(updatedService);
    }

    /**
     * 删除服务（软删除 - 设置为不可用状态）
     */
    public void deleteService(UUID uuid) {
        Service service = serviceRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("服务未找到: " + uuid));

        service.setStatus(Service.ServiceStatus.INACTIVE);
        serviceRepository.save(service);
    }

    /**
     * 彻底删除服务
     */
    public void hardDeleteService(UUID uuid) {
        if (!serviceRepository.existsById(uuid)) {
            throw new ResourceNotFoundException("服务未找到: " + uuid);
        }
        serviceRepository.deleteById(uuid);
    }

    /**
     * 激活服务
     */
    public ServiceDTO.ServiceResponse activateService(UUID uuid) {
        Service service = serviceRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("服务未找到: " + uuid));

        service.setStatus(Service.ServiceStatus.ACTIVE);
        Service updatedService = serviceRepository.save(service);
        return new ServiceDTO.ServiceResponse(updatedService);
    }

    /**
     * 停用服务
     */
    public ServiceDTO.ServiceResponse deactivateService(UUID uuid) {
        Service service = serviceRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("服务未找到: " + uuid));

        service.setStatus(Service.ServiceStatus.INACTIVE);
        Service updatedService = serviceRepository.save(service);
        return new ServiceDTO.ServiceResponse(updatedService);
    }

    /**
     * 批量更新价格
     */
    public List<ServiceDTO.ServiceResponse> batchUpdatePrices(Map<UUID, BigDecimal> priceUpdates) {
        List<ServiceDTO.ServiceResponse> updatedServices = new ArrayList<>();

        for (Map.Entry<UUID, BigDecimal> entry : priceUpdates.entrySet()) {
            Service service = serviceRepository.findById(entry.getKey())
                    .orElseThrow(() -> new ResourceNotFoundException("服务未找到: " + entry.getKey()));

            service.setPrice(entry.getValue());
            Service updatedService = serviceRepository.save(service);
            updatedServices.add(new ServiceDTO.ServiceResponse(updatedService));
        }

        return updatedServices;
    }

    /**
     * 获取服务统计
     */
    @Transactional(readOnly = true)
    public ServiceDTO.ServiceStatsResponse getServiceStats() {
        ServiceDTO.ServiceStatsResponse stats = new ServiceDTO.ServiceStatsResponse();

        // 总服务数
        stats.setTotalServices(serviceRepository.count());

        // 按状态统计
        List<Object[]> statusStats = serviceRepository.countServicesByStatus();
        for (Object[] stat : statusStats) {
            Service.ServiceStatus status = (Service.ServiceStatus) stat[0];
            Long count = (Long) stat[1];

            if (status == Service.ServiceStatus.ACTIVE) {
                stats.setActiveServices(count);
            } else if (status == Service.ServiceStatus.INACTIVE) {
                stats.setInactiveServices(count);
            }
        }

        // 按类型统计
        List<Object[]> typeStats = serviceRepository.countActiveServicesByType();
        for (Object[] stat : typeStats) {
            Service.ServiceType type = (Service.ServiceType) stat[0];
            Long count = (Long) stat[1];

            switch (type) {
                case CHAIR_MASSAGE:
                    stats.setChairMassageServices(count);
                    break;
                case FOOT_REFLEXOLOGY:
                    stats.setFootReflexologyServices(count);
                    break;
                case BODY_MASSAGE:
                    stats.setBodyMassageServices(count);
                    break;
                case COMBINATION_MASSAGE:
                    stats.setCombinationServices(count);
                    break;
            }
        }

        // 价格统计
        stats.setAveragePrice(serviceRepository.calculateAveragePrice());

        serviceRepository.findMostExpensiveService()
                .ifPresent(service -> stats.setHighestPrice(service.getPrice()));

        serviceRepository.findLeastExpensiveService()
                .ifPresent(service -> stats.setLowestPrice(service.getPrice()));

        return stats;
    }

    /**
     * 初始化RelaxationSpa默认服务
     */
    public void initializeDefaultServices() {
        if (serviceRepository.count() == 0) {
            createDefaultServices();
        }
    }

    /**
     * 创建默认服务数据
     */
    private void createDefaultServices() {
        List<Service> defaultServices = Arrays.asList(
                // 椅背按摩
                createDefaultService("CM012", "椅背按摩 12分钟", Service.ServiceType.CHAIR_MASSAGE, 12, new BigDecimal("12.00"), 1),
                createDefaultService("CM017", "椅背按摩 17分钟", Service.ServiceType.CHAIR_MASSAGE, 17, new BigDecimal("17.00"), 2),
                createDefaultService("CM020", "椅背按摩 20分钟", Service.ServiceType.CHAIR_MASSAGE, 20, new BigDecimal("20.00"), 3),
                createDefaultService("CM030", "椅背按摩 30分钟", Service.ServiceType.CHAIR_MASSAGE, 30, new BigDecimal("30.00"), 4),

                // 足疗
                createDefaultService("FR030", "足疗 30分钟", Service.ServiceType.FOOT_REFLEXOLOGY, 30, new BigDecimal("30.00"), 5),
                createDefaultService("FR040", "足疗 40分钟", Service.ServiceType.FOOT_REFLEXOLOGY, 40, new BigDecimal("40.00"), 6),
                createDefaultService("FR050", "足疗 50分钟", Service.ServiceType.FOOT_REFLEXOLOGY, 50, new BigDecimal("48.00"), 7),

                // 身体按摩
                createDefaultService("BM030", "身体按摩 30分钟", Service.ServiceType.BODY_MASSAGE, 30, new BigDecimal("30.00"), 8),
                createDefaultService("BM040", "身体按摩 40分钟", Service.ServiceType.BODY_MASSAGE, 40, new BigDecimal("40.00"), 9),
                createDefaultService("BM060", "身体按摩 60分钟", Service.ServiceType.BODY_MASSAGE, 60, new BigDecimal("60.00"), 10),

                // 组合按摩
                createDefaultService("COMB030", "组合按摩 30分钟", Service.ServiceType.COMBINATION_MASSAGE, 30, new BigDecimal("30.00"), 11),
                createDefaultService("COMB040", "组合按摩 40分钟", Service.ServiceType.COMBINATION_MASSAGE, 40, new BigDecimal("40.00"), 12),
                createDefaultService("COMB060", "组合按摩 60分钟", Service.ServiceType.COMBINATION_MASSAGE, 60, new BigDecimal("60.00"), 13),
                createDefaultService("COMB070", "组合按摩 70分钟", Service.ServiceType.COMBINATION_MASSAGE, 70, new BigDecimal("70.00"), 14)
        );

        serviceRepository.saveAll(defaultServices);
    }

    /**
     * 创建默认服务对象
     */
    private Service createDefaultService(String code, String name, Service.ServiceType type,
                                         Integer duration, BigDecimal price, Integer displayOrder) {
        Service service = new Service();
        service.setServiceCode(code);
        service.setServiceName(name);
        service.setServiceType(type);
        service.setDurationMinutes(duration);
        service.setPrice(price);
        service.setStatus(Service.ServiceStatus.ACTIVE);
        service.setIsCombination(type == Service.ServiceType.COMBINATION_MASSAGE);
        service.setDisplayOrder(displayOrder);

        if (type == Service.ServiceType.COMBINATION_MASSAGE) {
            service.setCombinationDescription("Choose from Chair or Body Massage + Foot Massage");
        }

        return service;
    }

    /**
     * 获取服务类型显示名称
     */
    private String getServiceTypeDisplayName(Service.ServiceType serviceType) {
        return switch (serviceType) {
            case CHAIR_MASSAGE -> "椅背按摩";
            case FOOT_REFLEXOLOGY -> "足疗";
            case BODY_MASSAGE -> "身体按摩";
            case COMBINATION_MASSAGE -> "组合按摩";
        };
    }

    /**
     * 检查服务是否存在
     */
    @Transactional(readOnly = true)
    public boolean serviceExists(UUID uuid) {
        return serviceRepository.existsById(uuid);
    }
}