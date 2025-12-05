package edu.dosw.rideci.application.event;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreatedEvent {

    private String bookingId;
    private String travelId;
    private String origin;
    private String destination;
    private Long passengerId;
    private int reservedSeats;
    private LocalDateTime timestamp;

}
