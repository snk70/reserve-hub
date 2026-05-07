package com.spring.microservice.reservehub.bookingserviceapplication.web;

import com.spring.microservice.reservehub.dto.BookingResponse;
import com.spring.microservice.reservehub.dto.CreateBookingRequest;
import com.spring.microservice.reservehub.port.input.IBookingUseCasePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final IBookingUseCasePort bookingUseCase;  // ← وابستگی به Interface

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody CreateBookingRequest request,
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingUseCase.createBooking(request, userId));
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponse>> getMyBookings(
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(bookingUseCase.getUserBookings(userId));
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> cancelBooking(
            @PathVariable UUID bookingId,
            @RequestHeader("X-User-Id") UUID userId) {
        bookingUseCase.cancelBooking(bookingId, userId);
        return ResponseEntity.noContent().build();
    }
}