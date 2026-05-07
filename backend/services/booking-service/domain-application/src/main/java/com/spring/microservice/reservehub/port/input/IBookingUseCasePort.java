package com.spring.microservice.reservehub.port.input;


import com.spring.microservice.reservehub.dto.BookingResponse;
import com.spring.microservice.reservehub.dto.CreateBookingRequest;

import java.util.List;
import java.util.UUID;

public interface IBookingUseCasePort {
    BookingResponse createBooking(CreateBookingRequest request, UUID userId);
    void cancelBooking(UUID bookingId, UUID userId);
    List<BookingResponse> getUserBookings(UUID userId);
    BookingResponse getBooking(UUID bookingId, UUID userId);
}