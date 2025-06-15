// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/controller/ServiceController.java
package com.relaxationspa.rscutoffsystem.controller;

import com.relaxationspa.rscutoffsystem.dto.ServiceDTO;
import com.relaxationspa.rscutoffsystem.entity.Service;
import com.relaxationspa.rscutoffsystem.service.ServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    /**
     * 创建新服务
     */
    @PostMapping
    public ResponseEntity<ServiceDTO.ServiceResponse> createService(@Valid @RequestBody ServiceDTO.CreateServiceRequest request) {
        ServiceDTO.ServiceResponse response = serviceService.createService(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 获取所有服务（分页）
     */
    @GetMapping
    public ResponseEntity<Page<ServiceDTO.ServiceResponse>> getAllServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "displayOrder") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ServiceDTO.ServiceResponse> services = serviceService.getAllServices(pageable);

        return ResponseEntity.ok(services);
    }

    /**
     * 根据UUID获取服务
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<ServiceDTO.ServiceResponse> getServiceByUuid(@PathVariable UUID uuid) {
        ServiceDTO.ServiceResponse response = serviceService.getServiceByUuid(uuid);
        return ResponseEntity.ok(response);
    }

    /**
     * 根据服务代码获取服务
     */
    @GetMapping("/code/{serviceCode}")
    public ResponseEntity<ServiceDTO.ServiceResponse> getServiceByCode(@PathVariable String serviceCode) {
        ServiceDTO.ServiceResponse response = serviceService.getServiceByCode(serviceCode);
        return ResponseEntity.ok(response);
    }

    /**
     * 获取所有可用服务
     */
    @GetMapping("/active")
    public ResponseEntity<List<ServiceDTO.ServiceResponse>> getAllActiveServices() {
        List<ServiceDTO.ServiceResponse> services = serviceService.getAllActiveServices();
        return ResponseEntity.ok(services);
    }

    /**
     * 根据服务类型获取服务
     */
    @GetMapping("/type/{serviceType}")
    public ResponseEntity<List<ServiceDTO.ServiceResponse>> getServicesByType(@PathVariable Service.ServiceType serviceType) {
        List<ServiceDTO.ServiceResponse> services = serviceService.getServicesByType(serviceType);
        return ResponseEntity.ok(services);
    }

    /**
     * 获取椅背按摩服务
     */
    @GetMapping("/chair-massage")
    public ResponseEntity<List<ServiceDTO.ServiceResponse>> getChairMassageServices() {
        List<ServiceDTO.ServiceResponse> services = serviceService.getChairMassageServices();
        return ResponseEntity.ok(services);
    }

    /**
     * 获取足疗服务
     */
    @GetMapping("/foot-reflexology")
    public ResponseEntity<List<ServiceDTO.ServiceResponse>> getFootReflexologyServices() {
        List<ServiceDTO.ServiceResponse> services = serviceService.getFootReflexologyServices();
        return ResponseEntity.ok(services);
    }

    /**
     * 获取身体按摩服务
     */
    @GetMapping("/body-massage")
    public ResponseEntity<List<ServiceDTO.ServiceResponse>> getBodyMassageServices() {
        List<ServiceDTO.ServiceResponse> services = serviceService.getBodyMassageServices();
        return ResponseEntity.ok(services);
    }

    /**
     * 获取组合按摩服务
     */
    @GetMapping("/combination-massage")
    public ResponseEntity<List<ServiceDTO.ServiceResponse>> getCombinationMassageServices() {
        List<ServiceDTO.ServiceResponse> services = serviceService.getCombinationMassageServices();
        return ResponseEntity.ok(services);
    }

    /**
     * 根据价格范围获取服务
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<ServiceDTO.ServiceResponse>> getServicesByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<ServiceDTO.ServiceResponse> services = serviceService.getServicesByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(services);
    }

    /**
     * 根据时长范围获取服务
     */
    @GetMapping("/duration-range")
    public ResponseEntity<List<ServiceDTO.ServiceResponse>> getServicesByDurationRange(
            @RequestParam Integer minDuration,
            @RequestParam Integer maxDuration) {
        List<ServiceDTO.ServiceResponse> services = serviceService.getServicesByDurationRange(minDuration, maxDuration);
        return ResponseEntity.ok(services);
    }

    /**
     * 搜索服务
     */
    @GetMapping("/search")
    public ResponseEntity<List<ServiceDTO.ServiceResponse>> searchServices(@RequestParam String keyword) {
        List<ServiceDTO.ServiceResponse> services = serviceService.searchServices(keyword);
        return ResponseEntity.ok(services);
    }

    /**
     * 获取快速服务（30分钟以下）
     */
    @GetMapping("/quick")
    public ResponseEntity<List<ServiceDTO.ServiceResponse>> getQuickServices() {
        List<ServiceDTO.ServiceResponse> services = serviceService.getQuickServices();
        return ResponseEntity.ok(services);
    }

    /**
     * 获取长时间服务（60分钟以上）
     */
    @GetMapping("/long")
    public ResponseEntity<List<ServiceDTO.ServiceResponse>> getLongServices() {
        List<ServiceDTO.ServiceResponse> services = serviceService.getLongServices();
        return ResponseEntity.ok(services);
    }

    /**
     * 获取服务菜单（按类型分组）
     */
    @GetMapping("/menu")
    public ResponseEntity<List<ServiceDTO.ServiceMenuResponse>> getServiceMenu() {
        List<ServiceDTO.ServiceMenuResponse> menu = serviceService.getServiceMenu();
        return ResponseEntity.ok(menu);
    }

    /**
     * 更新服务
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<ServiceDTO.ServiceResponse> updateService(
            @PathVariable UUID uuid,
            @Valid @RequestBody ServiceDTO.UpdateServiceRequest request) {
        ServiceDTO.ServiceResponse response = serviceService.updateService(uuid, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 删除服务（软删除）
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteService(@PathVariable UUID uuid) {
        serviceService.deleteService(uuid);
        return ResponseEntity.noContent().build();
    }

    /**
     * 彻底删除服务
     */
    @DeleteMapping("/{uuid}/hard")
    public ResponseEntity<Void> hardDeleteService(@PathVariable UUID uuid) {
        serviceService.hardDeleteService(uuid);
        return ResponseEntity.noContent().build();
    }

    /**
     * 激活服务
     */
    @PostMapping("/{uuid}/activate")
    public ResponseEntity<ServiceDTO.ServiceResponse> activateService(@PathVariable UUID uuid) {
        ServiceDTO.ServiceResponse response = serviceService.activateService(uuid);
        return ResponseEntity.ok(response);
    }

    /**
     * 停用服务
     */
    @PostMapping("/{uuid}/deactivate")
    public ResponseEntity<ServiceDTO.ServiceResponse> deactivateService(@PathVariable UUID uuid) {
        ServiceDTO.ServiceResponse response = serviceService.deactivateService(uuid);
        return ResponseEntity.ok(response);
    }

    /**
     * 批量更新价格
     */
    @PutMapping("/batch-update-prices")
    public ResponseEntity<List<ServiceDTO.ServiceResponse>> batchUpdatePrices(@RequestBody Map<UUID, BigDecimal> priceUpdates) {
        List<ServiceDTO.ServiceResponse> updatedServices = serviceService.batchUpdatePrices(priceUpdates);
        return ResponseEntity.ok(updatedServices);
    }

    /**
     * 获取服务统计
     */
    @GetMapping("/stats")
    public ResponseEntity<ServiceDTO.ServiceStatsResponse> getServiceStats() {
        ServiceDTO.ServiceStatsResponse stats = serviceService.getServiceStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * 初始化默认服务
     */
    @PostMapping("/initialize")
    public ResponseEntity<String> initializeDefaultServices() {
        serviceService.initializeDefaultServices();
        return ResponseEntity.ok("默认服务初始化完成");
    }

    /**
     * 检查服务是否存在
     */
    @GetMapping("/{uuid}/exists")
    public ResponseEntity<Boolean> serviceExists(@PathVariable UUID uuid) {
        boolean exists = serviceService.serviceExists(uuid);
        return ResponseEntity.ok(exists);
    }
}