package com.spring.microservice.reservehub.port.output;


import com.spring.microservice.reservehub.entity.Booking;

public interface IEventPublisher {
    void publishBookingCreated(Booking booking);
    void publishBookingCancelled(Booking booking);
    void publishBookingConfirmed(Booking booking);
}