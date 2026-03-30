package com.spring.microservice.reservehub.entity;

import com.spring.microservice.reservehub.valobj.Money;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "business_owner_id", nullable = false)
    private UUID businessOwnerId;

    @Embedded
    private Money price;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "max_concurrent_bookings")
    private Integer maxConcurrentBookings = 1;

    private Boolean isActive = true;

    // Domain behaviors
    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void updatePrice(Money newPrice) {
        if (!newPrice.isPositive()) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        this.price = newPrice;
    }

    public boolean isAvailable() {
        return isActive;
    }
}