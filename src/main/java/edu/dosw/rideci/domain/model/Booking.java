package edu.dosw.rideci.domain.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import edu.dosw.rideci.domain.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    private String id;
    private String travelId;
    private Long passengerId;
    private String origin;
    private String destination;
    private int reservedSeats;
    private Double totalAmount;
    private BookingStatus status;
    private String notes;
    private LocalDateTime bookingDate;
    private LocalDateTime cancellationDate;
    private LocalDateTime confirmationDate;
    private String paymentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}