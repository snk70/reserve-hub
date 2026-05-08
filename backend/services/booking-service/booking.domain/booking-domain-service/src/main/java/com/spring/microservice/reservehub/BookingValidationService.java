package com.spring.microservice.reservehub;


import com.spring.microservice.reservehub.entity.Booking;
import com.spring.microservice.reservehub.entity.BusinessService;
import com.spring.microservice.reservehub.valobj.BookingStatus;
import com.spring.microservice.reservehub.valobj.TimeSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingValidationService {

//    private final IBookingRepository bookingRepository;

    public void validateBooking(BusinessService service, TimeSlot timeSlot, UUID userId) {
        // Validate time slot
        if (!timeSlot.isValid()) {
            throw new IllegalArgumentException("Invalid time slot: start time must be before end time");
        }

        // Check business hours
        if (!timeSlot.isWithinBusinessHours()) {
            throw new IllegalArgumentException("Bookings only allowed between 8 AM and 8 PM");
        }

        // Check if service is active
        if (!service.isAvailable()) {
            throw new IllegalStateException("Service is not available for booking");
        }

        // Check for conflicting bookings
//        boolean hasConflict = bookingRepository.existsConflictingBooking(
//                service.getId(),
//                timeSlot.getStartTime(),
//                timeSlot.getEndTime()
//        );

//        if (hasConflict) {
//            throw new IllegalStateException("Time slot is already booked");
//        }

        // Check future booking only
        if (timeSlot.getStartTime().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot book past time slots");
        }
    }

    public void validateCancellation(Booking booking, UUID userId) {
        if (!booking.belongsToUser(userId)) {
            throw new SecurityException("Cannot cancel another user's booking");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed booking");
        }

        // Check if cancellation is within allowed time (e.g., 24 hours before)
        if (booking.getTimeSlot().getStartTime()
                .isBefore(java.time.LocalDateTime.now().plusHours(24))) {
            throw new IllegalStateException("Cancellation must be at least 24 hours before start time");
        }
    }
}