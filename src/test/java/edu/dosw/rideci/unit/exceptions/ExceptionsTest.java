package edu.dosw.rideci.unit.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.dosw.rideci.exceptions.BookingNotFoundException;
import edu.dosw.rideci.exceptions.InsufficientSeatsException;

@DisplayName("Custom Exceptions Tests")
class ExceptionsTest {

    @Test
    @DisplayName("Should create BookingNotFoundException with message")
    void shouldCreateBookingNotFoundException() {
        // Given
        String message = "Booking with id BOOKING-001 not found";

        // When
        BookingNotFoundException exception = new BookingNotFoundException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Should create InsufficientSeatsException with message")
    void shouldCreateInsufficientSeatsException() {
        // Given
        String message = "Not enough seats available";

        // When
        InsufficientSeatsException exception = new InsufficientSeatsException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Should throw and catch BookingNotFoundException")
    void shouldThrowAndCatchBookingNotFoundException() {
        // Given
        String bookingId = "BOOKING-999";

        // When & Then
        Exception exception = assertThrows(BookingNotFoundException.class, () -> {
            throw new BookingNotFoundException("Booking with id " + bookingId + " not found");
        });

        assertTrue(exception.getMessage().contains(bookingId));
    }

    @Test
    @DisplayName("Should throw and catch InsufficientSeatsException")
    void shouldThrowAndCatchInsufficientSeatsException() {
        // When & Then
        Exception exception = assertThrows(InsufficientSeatsException.class, () -> {
            throw new InsufficientSeatsException("Number of seats must be greater than 0");
        });

        assertTrue(exception.getMessage().contains("seats"));
    }
}
