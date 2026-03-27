package com.spring.microservice.reservehub.valobj;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public boolean isOverlap(TimeSlot other) {
        return this.startTime.isBefore(other.endTime) &&
                other.startTime.isBefore(this.endTime);
    }

    public boolean isValid() {
        return startTime != null &&
                endTime != null &&
                startTime.isBefore(endTime);
    }

    public boolean isWithinBusinessHours() {
        int startHour = startTime.getHour();
        int endHour = endTime.getHour();
        return startHour >= 8 && endHour <= 20;
    }
}