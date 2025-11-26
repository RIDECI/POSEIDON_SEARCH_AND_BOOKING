package edu.dosw.rideci.unit.domain;

import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.domain.model.enums.BookingStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    @DisplayName("Should create booking with pending status")
    void shouldCreateBookingWithPendingStatus() {
        // Given
        String tripId = "trip-123";
        String passengerId = "passenger-456";
        int reservedSeats = 2;
        BigDecimal totalAmount = new BigDecimal("50000");
        String notes = "Test booking";

        // When
        Booking booking = new Booking(tripId, passengerId, reservedSeats, totalAmount, notes);

        // Then
        assertNotNull(booking);
        assertEquals(tripId, booking.getTripId());
        assertEquals(passengerId, booking.getPassengerId());
        assertEquals(reservedSeats, booking.getReservedSeats());
        assertEquals(totalAmount, booking.getTotalAmount());
        assertEquals(notes, booking.getNotes());
        assertEquals(BookingStatus.PENDING, booking.getStatus());
        assertNotNull(booking.getBookingDate());
        assertNotNull(booking.getCreatedAt());
        assertNotNull(booking.getUpdatedAt());
    }

    @Test
    @DisplayName("Should confirm booking successfully")
    void shouldConfirmBookingSuccessfully() {
        // Given
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.PENDING);
        String paymentId = "payment-789";

        // When
        booking.confirm(paymentId);

        // Then
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertEquals(paymentId, booking.getPaymentId());
        assertNotNull(booking.getConfirmationDate());
    }

    @Test
    @DisplayName("Should cancel booking successfully")
    void shouldCancelBookingSuccessfully() {
        // Given
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.PENDING);

        // When
        booking.cancel();

        // Then
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertNotNull(booking.getCancellationDate());
    }

    @Test
    @DisplayName("Should throw exception when confirming non-pending booking")
    void shouldThrowExceptionWhenConfirmingNonPendingBooking() {
        // Given
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.CONFIRMED);
        String paymentId = "payment-789";

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> booking.confirm(paymentId));
        assertEquals("Only pending bookings can be confirmed", exception.getMessage());
    }

    @Test
    @DisplayName("Should return true for active booking")
    void shouldReturnTrueForActiveBooking() {
        // Given
        Booking pendingBooking = new Booking();
        pendingBooking.setStatus(BookingStatus.PENDING);
        
        Booking confirmedBooking = new Booking();
        confirmedBooking.setStatus(BookingStatus.CONFIRMED);

        // When & Then
        assertTrue(pendingBooking.isActive());
        assertTrue(confirmedBooking.isActive());
    }

    @Test
    @DisplayName("Should return false for inactive booking")
    void shouldReturnFalseForInactiveBooking() {
        // Given
        Booking cancelledBooking = new Booking();
        cancelledBooking.setStatus(BookingStatus.CANCELLED);
        
        Booking completedBooking = new Booking();
        completedBooking.setStatus(BookingStatus.COMPLETED);

        // When & Then
        assertFalse(cancelledBooking.isActive());
        assertFalse(completedBooking.isActive());
    }
}