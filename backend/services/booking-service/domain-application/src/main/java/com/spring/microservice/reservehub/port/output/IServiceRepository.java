package com.spring.microservice.reservehub.port.output;

import com.spring.microservice.reservehub.entity.BusinessService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IServiceRepository {
    BusinessService save(BusinessService service);
    Optional<BusinessService> findById(UUID id);
    List<BusinessService> findByBusinessOwnerId(UUID businessOwnerId);
    List<BusinessService> findAllActive();
}