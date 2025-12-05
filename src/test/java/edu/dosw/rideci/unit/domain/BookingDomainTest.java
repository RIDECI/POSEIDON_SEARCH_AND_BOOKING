package edu.dosw.rideci.unit.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.domain.model.enums.BookingStatus;

@DisplayName("Booking Domain Model Tests")
class BookingDomainTest {

    @Test
    @DisplayName("Should create booking with builder")
    void shouldCreateBookingWithBuilder() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        Booking booking = Booking.builder()
                .id("BOOKING-001")
                .travelId("TRAVEL-001")
                .passengerId(12345L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .notes("Test booking")
                .bookingDate(now)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertNotNull(booking);
        assertEquals("BOOKING-001", booking.getId());
        assertEquals("TRAVEL-001", booking.getTravelId());
        assertEquals(12345L, booking.getPassengerId());
        assertEquals("Bogotá", booking.getOrigin());
        assertEquals("Medellín", booking.getDestination());
        assertEquals(2, booking.getReservedSeats());
        assertEquals(150000.0, booking.getTotalAmount());
        assertEquals(BookingStatus.PENDING, booking.getStatus());
        assertEquals("Test booking", booking.getNotes());
        assertEquals(now, booking.getBookingDate());
        assertEquals(now, booking.getCreatedAt());
        assertEquals(now, booking.getUpdatedAt());
    }

    @Test
    @DisplayName("Should update booking properties")
    void shouldUpdateBookingProperties() {
        // Given
        Booking booking = Booking.builder()
                .id("BOOKING-001")
                .reservedSeats(2)
                .status(BookingStatus.PENDING)
                .build();

        // When
        booking.setReservedSeats(3);
        booking.setStatus(BookingStatus.CONFIRMED);

        // Then
        assertEquals(3, booking.getReservedSeats());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }

    @Test
    @DisplayName("Should handle all booking statuses")
    void shouldHandleAllBookingStatuses() {
        // Given & When & Then
        Booking pendingBooking = Booking.builder().status(BookingStatus.PENDING).build();
        assertEquals(BookingStatus.PENDING, pendingBooking.getStatus());

        Booking confirmedBooking = Booking.builder().status(BookingStatus.CONFIRMED).build();
        assertEquals(BookingStatus.CONFIRMED, confirmedBooking.getStatus());

        Booking cancelledBooking = Booking.builder().status(BookingStatus.CANCELLED).build();
        assertEquals(BookingStatus.CANCELLED, cancelledBooking.getStatus());

        Booking completedBooking = Booking.builder().status(BookingStatus.COMPLETED).build();
        assertEquals(BookingStatus.COMPLETED, completedBooking.getStatus());
    }

    @Test
    @DisplayName("Should create booking with all optional fields null")
    void shouldCreateBookingWithNullOptionalFields() {
        // Given & When
        Booking booking = Booking.builder()
                .id("BOOKING-001")
                .travelId("TRAVEL-001")
                .passengerId(12345L)
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .build();

        // Then
        assertNotNull(booking);
        assertNull(booking.getNotes());
        assertNull(booking.getCancellationDate());
        assertNull(booking.getConfirmationDate());
        assertNull(booking.getPaymentId());
    }

    @Test
    @DisplayName("Should set cancellation and confirmation dates")
    void shouldSetCancellationAndConfirmationDates() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Booking booking = Booking.builder()
                .id("BOOKING-001")
                .status(BookingStatus.PENDING)
                .build();

        // When
        booking.setConfirmationDate(now);
        booking.setStatus(BookingStatus.CONFIRMED);

        // Then
        assertEquals(now, booking.getConfirmationDate());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());

        // When - Cancel
        LocalDateTime cancelTime = LocalDateTime.now().plusHours(1);
        booking.setCancellationDate(cancelTime);
        booking.setStatus(BookingStatus.CANCELLED);

        // Then
        assertEquals(cancelTime, booking.getCancellationDate());
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void shouldTestEqualsAndHashCode() {
        // Given
        Booking booking1 = Booking.builder()
                .id("BOOKING-001")
                .travelId("TRAVEL-001")
                .passengerId(12345L)
                .build();

        Booking booking2 = Booking.builder()
                .id("BOOKING-001")
                .travelId("TRAVEL-001")
                .passengerId(12345L)
                .build();

        Booking booking3 = Booking.builder()
                .id("BOOKING-002")
                .travelId("TRAVEL-001")
                .passengerId(12345L)
                .build();

        // Then
        assertEquals(booking1, booking2);
        assertNotEquals(booking1, booking3);
        assertEquals(booking1.hashCode(), booking2.hashCode());
    }
}
