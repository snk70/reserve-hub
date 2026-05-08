package com.spring.microservice.reservehub.entity;

import com.spring.microservice.reservehub.valobj.BookingStatus;
import com.spring.microservice.reservehub.valobj.TimeSlot;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private BusinessService service;

    @Embedded
    private TimeSlot timeSlot;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    @Version
    private Long version;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Domain behaviors (Business logic)
    public void confirm() {
        if (this.status != BookingStatus.PENDING) {
            throw new IllegalStateException("Only pending bookings can be confirmed");
        }
        this.status = BookingStatus.CONFIRMED;
    }

    public void cancel() {
        if (this.status == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Completed bookings cannot be cancelled");
        }
        this.status = BookingStatus.CANCELLED;
    }

    public void complete() {
        if (this.status != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed bookings can be completed");
        }
        this.status = BookingStatus.COMPLETED;
    }

    public boolean isOverlapping(Booking other) {
        if (!this.service.getId().equals(other.getService().getId())) {
            return false;
        }
        return this.timeSlot.isOverlap(other.getTimeSlot());
    }

    public boolean belongsToUser(UUID userId) {
        return this.userId.equals(userId);
    }
}