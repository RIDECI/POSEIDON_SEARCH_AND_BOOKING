package edu.dosw.rideci.application.dto;

import java.time.LocalDateTime;

import edu.dosw.rideci.domain.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingCommand {

    private String travelId;
    private Long passengerId;
    private String origin;
    private String destination;
    private int reservedSeats;
    private Double totalAmount;
    private BookingStatus status;
    private String notes;
    private LocalDateTime bookingDate;

}
