package com.spring.microservice.reservehub.imodels;

import com.spring.microservice.reservehub.entity.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IServiceRepository {
    Service save(Service service);
    Optional<Service> findById(UUID id);
    List<Service> findByBusinessOwnerId(UUID businessOwnerId);
    List<Service> findAllActive();
    boolean existsById(UUID id);
}