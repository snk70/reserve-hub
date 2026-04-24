package com.spring.microservice.reservehub.port.output;

import com.spring.microservice.reservehub.entity.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IBookingRepository {
    Booking save(Booking booking);
    Optional<Booking> findById(UUID id);
    List<Booking> findByUserId(UUID userId);
    List<Booking> findConflictingBookings(UUID serviceId, LocalDateTime startTime, LocalDateTime endTime);
    boolean existsConflictingBooking(UUID serviceId, LocalDateTime startTime, LocalDateTime endTime);
    void delete(Booking booking);
}