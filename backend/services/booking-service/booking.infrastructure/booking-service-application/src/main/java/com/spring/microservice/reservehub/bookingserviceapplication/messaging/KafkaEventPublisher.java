package com.spring.microservice.reservehub.bookingserviceapplication.messaging;

import com.spring.microservice.reservehub.entity.Booking;
import com.spring.microservice.reservehub.port.output.IEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements IEventPublisher {

    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;
    private static final String TOPIC = "booking-events";

    @Override
    public void publishBookingCreated(Booking booking) {
        publish(createEvent(booking, "CREATED"));
    }

    @Override
    public void publishBookingCancelled(Booking booking) {
        publish(createEvent(booking, "CANCELLED"));
    }

    @Override
    public void publishBookingConfirmed(Booking booking) {
        publish(createEvent(booking, "CONFIRMED"));
    }

    private BookingEvent createEvent(Booking booking, String eventType) {
        return BookingEvent.builder()
                .eventId(UUID.randomUUID())
                .bookingId(booking.getId())
                .userId(booking.getUserId())
                .serviceId(booking.getService().getId())
                .serviceName(booking.getService().getName())
                .eventType(eventType)
                .startTime(booking.getTimeSlot().getStartTime())
                .endTime(booking.getTimeSlot().getEndTime())
                .status(booking.getStatus().name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private void publish(BookingEvent event) {
        kafkaTemplate.send(TOPIC, event.getBookingId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Event published: {}", event.getEventType());
                    } else {
                        log.error("Failed to publish event: {}", ex.getMessage());
                    }
                });
    }
}