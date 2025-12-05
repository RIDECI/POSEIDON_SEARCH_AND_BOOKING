package edu.dosw.rideci.domain.model;

import java.time.LocalDateTime;

import edu.dosw.rideci.domain.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Clase base que contiene los campos comunes para todas las representaciones de Booking.
 * Reduce la duplicación de código entre las diferentes capas de la arquitectura.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BookingBase {

    protected String travelId;
    protected Long passengerId;
    protected String origin;
    protected String destination;
    protected int reservedSeats;
    protected Double totalAmount;
    protected BookingStatus status;
    protected String notes;
    protected LocalDateTime bookingDate;
    protected LocalDateTime cancellationDate;
    protected LocalDateTime confirmationDate;
    protected String paymentId;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

}
