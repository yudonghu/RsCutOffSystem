// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/dto/ServiceDTO.java
package com.relaxationspa.rscutoffsystem.dto;

import com.relaxationspa.rscutoffsystem.entity.Service;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ServiceDTO {

    // 创建服务请求DTO
    public static class CreateServiceRequest {

        @NotBlank(message = "服务代码不能为空")
        private String serviceCode;

        @NotBlank(message = "服务名称不能为空")
        private String serviceName;

        @NotNull(message = "服务类型不能为空")
        private Service.ServiceType serviceType;

        @NotNull(message = "服务时长不能为空")
        @Min(value = 1, message = "服务时长必须大于0分钟")
        private Integer durationMinutes;

        @NotNull(message = "服务价格不能为空")
        @DecimalMin(value = "0.01", message = "服务价格必须大于0")
        private BigDecimal price;

        private String description;

        private Service.ServiceStatus status = Service.ServiceStatus.ACTIVE;

        private Boolean isCombination = false;

        private String combinationDescription;

        private Integer displayOrder;

        private String notes;

        // Constructors
        public CreateServiceRequest() {}

        // Getters and Setters
        public String getServiceCode() {
            return serviceCode;
        }

        public void setServiceCode(String serviceCode) {
            this.serviceCode = serviceCode;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public Service.ServiceType getServiceType() {
            return serviceType;
        }

        public void setServiceType(Service.ServiceType serviceType) {
            this.serviceType = serviceType;
        }

        public Integer getDurationMinutes() {
            return durationMinutes;
        }

        public void setDurationMinutes(Integer durationMinutes) {
            this.durationMinutes = durationMinutes;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Service.ServiceStatus getStatus() {
            return status;
        }

        public void setStatus(Service.ServiceStatus status) {
            this.status = status;
        }

        public Boolean getIsCombination() {
            return isCombination;
        }

        public void setIsCombination(Boolean isCombination) {
            this.isCombination = isCombination;
        }

        public String getCombinationDescription() {
            return combinationDescription;
        }

        public void setCombinationDescription(String combinationDescription) {
            this.combinationDescription = combinationDescription;
        }

        public Integer getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

    // 更新服务请求DTO
    public static class UpdateServiceRequest {

        private String serviceName;
        private Service.ServiceType serviceType;
        private Integer durationMinutes;
        private BigDecimal price;
        private String description;
        private Service.ServiceStatus status;
        private Boolean isCombination;
        private String combinationDescription;
        private Integer displayOrder;
        private String notes;

        // Getters and Setters
        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public Service.ServiceType getServiceType() {
            return serviceType;
        }

        public void setServiceType(Service.ServiceType serviceType) {
            this.serviceType = serviceType;
        }

        public Integer getDurationMinutes() {
            return durationMinutes;
        }

        public void setDurationMinutes(Integer durationMinutes) {
            this.durationMinutes = durationMinutes;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Service.ServiceStatus getStatus() {
            return status;
        }

        public void setStatus(Service.ServiceStatus status) {
            this.status = status;
        }

        public Boolean getIsCombination() {
            return isCombination;
        }

        public void setIsCombination(Boolean isCombination) {
            this.isCombination = isCombination;
        }

        public String getCombinationDescription() {
            return combinationDescription;
        }

        public void setCombinationDescription(String combinationDescription) {
            this.combinationDescription = combinationDescription;
        }

        public Integer getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

    // 服务响应DTO
    public static class ServiceResponse {

        private UUID uuid;
        private LocalDateTime createdAt;
        private String serviceCode;
        private String serviceName;
        private Service.ServiceType serviceType;
        private String serviceTypeDisplayName;
        private Integer durationMinutes;
        private BigDecimal price;
        private String formattedPrice;
        private String description;
        private Service.ServiceStatus status;
        private Boolean isCombination;
        private String combinationDescription;
        private Integer displayOrder;
        private String notes;
        private String displayName;

        // Constructors
        public ServiceResponse() {}

        public ServiceResponse(Service service) {
            this.uuid = service.getUuid();
            this.createdAt = service.getCreatedAt();
            this.serviceCode = service.getServiceCode();
            this.serviceName = service.getServiceName();
            this.serviceType = service.getServiceType();
            this.serviceTypeDisplayName = service.getServiceTypeDisplayName();
            this.durationMinutes = service.getDurationMinutes();
            this.price = service.getPrice();
            this.formattedPrice = service.getFormattedPrice();
            this.description = service.getDescription();
            this.status = service.getStatus();
            this.isCombination = service.getIsCombination();
            this.combinationDescription = service.getCombinationDescription();
            this.displayOrder = service.getDisplayOrder();
            this.notes = service.getNotes();
            this.displayName = service.getDisplayName();
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

        public String getServiceCode() {
            return serviceCode;
        }

        public void setServiceCode(String serviceCode) {
            this.serviceCode = serviceCode;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public Service.ServiceType getServiceType() {
            return serviceType;
        }

        public void setServiceType(Service.ServiceType serviceType) {
            this.serviceType = serviceType;
        }

        public String getServiceTypeDisplayName() {
            return serviceTypeDisplayName;
        }

        public void setServiceTypeDisplayName(String serviceTypeDisplayName) {
            this.serviceTypeDisplayName = serviceTypeDisplayName;
        }

        public Integer getDurationMinutes() {
            return durationMinutes;
        }

        public void setDurationMinutes(Integer durationMinutes) {
            this.durationMinutes = durationMinutes;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getFormattedPrice() {
            return formattedPrice;
        }

        public void setFormattedPrice(String formattedPrice) {
            this.formattedPrice = formattedPrice;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Service.ServiceStatus getStatus() {
            return status;
        }

        public void setStatus(Service.ServiceStatus status) {
            this.status = status;
        }

        public Boolean getIsCombination() {
            return isCombination;
        }

        public void setIsCombination(Boolean isCombination) {
            this.isCombination = isCombination;
        }

        public String getCombinationDescription() {
            return combinationDescription;
        }

        public void setCombinationDescription(String combinationDescription) {
            this.combinationDescription = combinationDescription;
        }

        public Integer getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
    }

    // 服务统计响应DTO
    public static class ServiceStatsResponse {
        private Long totalServices;
        private Long activeServices;
        private Long inactiveServices;
        private Long chairMassageServices;
        private Long footReflexologyServices;
        private Long bodyMassageServices;
        private Long combinationServices;
        private BigDecimal averagePrice;
        private BigDecimal highestPrice;
        private BigDecimal lowestPrice;

        // Constructors
        public ServiceStatsResponse() {}

        // Getters and Setters
        public Long getTotalServices() {
            return totalServices;
        }

        public void setTotalServices(Long totalServices) {
            this.totalServices = totalServices;
        }

        public Long getActiveServices() {
            return activeServices;
        }

        public void setActiveServices(Long activeServices) {
            this.activeServices = activeServices;
        }

        public Long getInactiveServices() {
            return inactiveServices;
        }

        public void setInactiveServices(Long inactiveServices) {
            this.inactiveServices = inactiveServices;
        }

        public Long getChairMassageServices() {
            return chairMassageServices;
        }

        public void setChairMassageServices(Long chairMassageServices) {
            this.chairMassageServices = chairMassageServices;
        }

        public Long getFootReflexologyServices() {
            return footReflexologyServices;
        }

        public void setFootReflexologyServices(Long footReflexologyServices) {
            this.footReflexologyServices = footReflexologyServices;
        }

        public Long getBodyMassageServices() {
            return bodyMassageServices;
        }

        public void setBodyMassageServices(Long bodyMassageServices) {
            this.bodyMassageServices = bodyMassageServices;
        }

        public Long getCombinationServices() {
            return combinationServices;
        }

        public void setCombinationServices(Long combinationServices) {
            this.combinationServices = combinationServices;
        }

        public BigDecimal getAveragePrice() {
            return averagePrice;
        }

        public void setAveragePrice(BigDecimal averagePrice) {
            this.averagePrice = averagePrice;
        }

        public BigDecimal getHighestPrice() {
            return highestPrice;
        }

        public void setHighestPrice(BigDecimal highestPrice) {
            this.highestPrice = highestPrice;
        }

        public BigDecimal getLowestPrice() {
            return lowestPrice;
        }

        public void setLowestPrice(BigDecimal lowestPrice) {
            this.lowestPrice = lowestPrice;
        }
    }

    // 服务菜单响应DTO（用于客户端显示菜单）
    public static class ServiceMenuResponse {
        private Service.ServiceType serviceType;
        private String serviceTypeDisplayName;
        private java.util.List<ServiceResponse> services;

        // Constructors
        public ServiceMenuResponse() {}

        public ServiceMenuResponse(Service.ServiceType serviceType, String serviceTypeDisplayName) {
            this.serviceType = serviceType;
            this.serviceTypeDisplayName = serviceTypeDisplayName;
        }

        // Getters and Setters
        public Service.ServiceType getServiceType() {
            return serviceType;
        }

        public void setServiceType(Service.ServiceType serviceType) {
            this.serviceType = serviceType;
        }

        public String getServiceTypeDisplayName() {
            return serviceTypeDisplayName;
        }

        public void setServiceTypeDisplayName(String serviceTypeDisplayName) {
            this.serviceTypeDisplayName = serviceTypeDisplayName;
        }

        public java.util.List<ServiceResponse> getServices() {
            return services;
        }

        public void setServices(java.util.List<ServiceResponse> services) {
            this.services = services;
        }
    }
}