package com.spring.microservice.reservehub.bookingserviceapplication.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingEvent {
    private UUID eventId;
    private UUID bookingId;
    private UUID userId;
    private UUID serviceId;
    private String serviceName;
    private String eventType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private LocalDateTime timestamp;
}