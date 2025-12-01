package edu.dosw.rideci.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import edu.dosw.rideci.domain.model.enums.BookingStatus;
import lombok.Data;

@Data
@Document(collection = "bookings")
public class BookingDocument {

    @Id
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
