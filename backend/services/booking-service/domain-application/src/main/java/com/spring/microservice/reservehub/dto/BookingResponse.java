package com.spring.microservice.reservehub.dto;


import com.spring.microservice.reservehub.valobj.BookingStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BookingResponse {
    private UUID id;
    private UUID userId;
    private UUID serviceId;
    private String serviceName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}