package edu.dosw.rideci.infrastructure.controllers.dto.Request;

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
public class BookingRequest {

    private String id;
    private String travelId;
    private Long passengerId;
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
