package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * Booking - Domain entity for reservations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    
    private String id;
    private String tripId;
    private String passengerId;
    private int reservedSeats;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private String notes;
    private LocalDateTime bookingDate;
    private LocalDateTime cancellationDate;
    private LocalDateTime confirmationDate;
    private String paymentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    
    /**
     * Constructor for new booking
     */
    public Booking(String tripId, String passengerId, int reservedSeats, 
                   BigDecimal totalAmount, String notes) {
        this.tripId = tripId;
        this.passengerId = passengerId;
        this.reservedSeats = reservedSeats;
        this.totalAmount = totalAmount;
        this.notes = notes;
        this.status = BookingStatus.PENDING;
        this.bookingDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Complete constructor for booking
     */
    public Booking(String id, String tripId, String passengerId, int reservedSeats,
                   BigDecimal totalAmount, BookingStatus status, String notes,
                   LocalDateTime bookingDate, LocalDateTime cancellationDate,
                   LocalDateTime confirmationDate, String paymentId) {
        this.id = id;
        this.tripId = tripId;
        this.passengerId = passengerId;
        this.reservedSeats = reservedSeats;
        this.totalAmount = totalAmount;
        this.status = status;
        this.notes = notes;
        this.bookingDate = bookingDate;
        this.cancellationDate = cancellationDate;
        this.confirmationDate = confirmationDate;
        this.paymentId = paymentId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    
    // Business methods
    
    /**
     * Confirms the booking
     */
    public void confirm(String paymentId) {
        if (this.status != BookingStatus.PENDING) {
            throw new IllegalStateException("Only pending bookings can be confirmed");
        }
        this.status = BookingStatus.CONFIRMED;
        this.paymentId = paymentId;
        this.confirmationDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Cancels the booking
     */
    public void cancel() {
        if (this.status == BookingStatus.CANCELLED || this.status == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a booking that is already cancelled or completed");
        }
        this.status = BookingStatus.CANCELLED;
        this.cancellationDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Marks the booking as completed
     */
    public void complete() {
        if (this.status != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed bookings can be completed");
        }
        this.status = BookingStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Checks if the booking can be cancelled
     */
    public boolean canBeCancelled() {
        return this.status == BookingStatus.PENDING || this.status == BookingStatus.CONFIRMED;
    }
    
    /**
     * Checks if the booking is active (confirmed or pending)
     */
    public boolean isActive() {
        return this.status == BookingStatus.CONFIRMED || this.status == BookingStatus.PENDING;
    }
}