package com.spring.microservice.reservehub.service;


import com.spring.microservice.reservehub.BookingValidationService;
import com.spring.microservice.reservehub.dto.BookingResponse;
import com.spring.microservice.reservehub.dto.CreateBookingRequest;
import com.spring.microservice.reservehub.entity.Booking;

import com.spring.microservice.reservehub.entity.BusinessService;
import com.spring.microservice.reservehub.port.output.IBookingRepository;
import com.spring.microservice.reservehub.port.output.IEventPublisher;
import com.spring.microservice.reservehub.port.output.IServiceRepository;
import com.spring.microservice.reservehub.valobj.TimeSlot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingApplicationService {

    private final IBookingRepository bookingRepository;
    private final IServiceRepository serviceRepository;
    private final BookingValidationService validationService;
    private final IEventPublisher eventPublisher;

    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request, UUID userId) {
        log.info("Creating booking for user: {}, service: {}", userId, request.getServiceId());

        // 1. Get service from domain repository
        Service service = (Service) serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        // 2. Create time slot value object
        TimeSlot timeSlot = new TimeSlot(request.getStartTime(), request.getEndTime());

        // 3. Validate using domain service
        validationService.validateBooking((BusinessService) service, timeSlot, userId);

        // 4. Create booking entity
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setService((BusinessService) service);
        booking.setTimeSlot(timeSlot);

        // 5. Persist
        booking = bookingRepository.save(booking);

        // 6. Publish domain event
        eventPublisher.publishBookingCreated(booking);

        log.info("Booking created successfully: {}", booking.getId());

        return mapToResponse(booking);
    }

    @Transactional
    @CacheEvict(value = "userBookings", key = "#userId")
    public void cancelBooking(UUID bookingId, UUID userId) {
        log.info("Cancelling booking: {} for user: {}", bookingId, userId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // Use domain service for validation
        validationService.validateCancellation(booking, userId);

        // Domain behavior
        booking.cancel();

        bookingRepository.save(booking);
        eventPublisher.publishBookingCancelled(booking);

        log.info("Booking cancelled: {}", bookingId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "userBookings", key = "#userId")
    public List<BookingResponse> getUserBookings(UUID userId) {
        log.info("Fetching bookings for user: {}", userId);

        return bookingRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookingResponse getBooking(UUID bookingId, UUID userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.belongsToUser(userId)) {
            throw new SecurityException("Access denied");
        }

        return mapToResponse(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUserId())
                .serviceId(booking.getService().getId())
                .serviceName(booking.getService().getName())
                .startTime(booking.getTimeSlot().getStartTime())
                .endTime(booking.getTimeSlot().getEndTime())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}