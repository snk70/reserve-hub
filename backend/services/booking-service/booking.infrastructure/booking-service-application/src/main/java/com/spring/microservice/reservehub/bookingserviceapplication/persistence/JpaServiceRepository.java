package com.spring.microservice.reservehub.bookingserviceapplication.persistence;

import com.spring.microservice.reservehub.entity.Service;
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
    public Service save(Service service) {
        return repository.save(service);
    }

    @Override
    public Optional<Service> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<Service> findByBusinessOwnerId(UUID businessOwnerId) {
        return repository.findByBusinessOwnerId(businessOwnerId);
    }

    @Override
    public List<Service> findAllActive() {
        return repository.findAll();
    }
}

interface SpringDataServiceRepository extends JpaRepository<Service, UUID> {


    //    List<Service> findByBusinessOwnerId(UUID businessOwnerId);
    //    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT s FROM Service s WHERE s.businessOwnerId = :businessOwnerId " +
            "AND s.isActive=true ")
    List<Service> findByBusinessOwnerId(
            @Param("businessOwnerId") UUID businessOwnerId);

    @NotNull
    @Query()
    Optional<Service> findById(UUID id);

    @Query("SELECT s FROM Service s WHERE s.isActive=true")
    List<Service> findAllActive();

//    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.service.id = :serviceId " +
//            "AND b.timeSlot.startTime < :endTime " +
//            "AND b.timeSlot.endTime > :startTime " +
//            "AND b.status != 'CANCELLED'")
//    boolean existsConflictingBooking(
//            @Param("serviceId") UUID serviceId,
//            @Param("startTime") LocalDateTime startTime,
//            @Param("endTime") LocalDateTime endTime);
}