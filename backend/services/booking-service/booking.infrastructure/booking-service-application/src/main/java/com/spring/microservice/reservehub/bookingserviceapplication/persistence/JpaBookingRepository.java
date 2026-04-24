package com.spring.microservice.reservehub.bookingserviceapplication.persistence;

import com.spring.microservice.reservehub.entity.Booking;
import com.spring.microservice.reservehub.port.output.IBookingRepository;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaBookingRepository implements IBookingRepository {

    private final SpringDataBookingRepository repository;

    @Override
    public Booking save(Booking booking) {
        return repository.save(booking);
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<Booking> findByUserId(UUID userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<Booking> findConflictingBookings(UUID serviceId, LocalDateTime startTime, LocalDateTime endTime) {
        return repository.findConflictingBookings(serviceId, startTime, endTime);
    }

    @Override
    public boolean existsConflictingBooking(UUID serviceId, LocalDateTime startTime, LocalDateTime endTime) {
        return repository.existsConflictingBooking(serviceId, startTime, endTime);
    }

    @Override
    public void delete(Booking booking) {
        repository.delete(booking);
    }
}

interface SpringDataBookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByUserId(UUID userId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT b FROM Booking b WHERE b.service.id = :serviceId " +
            "AND b.timeSlot.startTime < :endTime " +
            "AND b.timeSlot.endTime > :startTime " +
            "AND b.status != 'CANCELLED'")
    List<Booking> findConflictingBookings(
            @Param("serviceId") UUID serviceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.service.id = :serviceId " +
            "AND b.timeSlot.startTime < :endTime " +
            "AND b.timeSlot.endTime > :startTime " +
            "AND b.status != 'CANCELLED'")
    boolean existsConflictingBooking(
            @Param("serviceId") UUID serviceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}