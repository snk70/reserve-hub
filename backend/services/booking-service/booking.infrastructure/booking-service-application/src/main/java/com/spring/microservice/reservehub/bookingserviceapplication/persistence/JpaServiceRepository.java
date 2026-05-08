package com.spring.microservice.reservehub.bookingserviceapplication.persistence;

import com.spring.microservice.reservehub.entity.BusinessService;
import com.spring.microservice.reservehub.port.output.IServiceRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaServiceRepository implements IServiceRepository {
    private final SpringDataServiceRepository repository;

    @Override
    public BusinessService save(BusinessService service) {
        return repository.save(service);
    }

    @Override
    public Optional<BusinessService> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<BusinessService> findByBusinessOwnerId(UUID businessOwnerId) {
        return repository.findByBusinessOwnerId(businessOwnerId);
    }

    @Override
    public List<BusinessService> findAllActive() {
        return repository.findAll();
    }
}

interface SpringDataServiceRepository extends JpaRepository<BusinessService, UUID> {


    //    List<Service> findByBusinessOwnerId(UUID businessOwnerId);
    //    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT s FROM BusinessService s WHERE s.businessOwnerId = :businessOwnerId " +
            "AND s.isActive=true ")
    List<BusinessService> findByBusinessOwnerId(
            @Param("businessOwnerId") UUID businessOwnerId);

    @NotNull
    @Query()
    Optional<BusinessService> findById(UUID id);

    @Query("SELECT s FROM BusinessService s WHERE s.isActive=true")
    List<BusinessService> findAllActive();

//    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.service.id = :serviceId " +
//            "AND b.timeSlot.startTime < :endTime " +
//            "AND b.timeSlot.endTime > :startTime " +
//            "AND b.status != 'CANCELLED'")
//    boolean existsConflictingBooking(
//            @Param("serviceId") UUID serviceId,
//            @Param("startTime") LocalDateTime startTime,
//            @Param("endTime") LocalDateTime endTime);
}