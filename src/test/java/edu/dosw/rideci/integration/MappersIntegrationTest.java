package edu.dosw.rideci.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import edu.dosw.rideci.application.mappers.BookingMapperInitial;
import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.domain.model.enums.BookingStatus;
import edu.dosw.rideci.infrastructure.adapters.mapper.BookingMapper;
import edu.dosw.rideci.infrastructure.controllers.dto.Response.BookingResponse;
import edu.dosw.rideci.infrastructure.persistence.entity.BookingDocument;

@SpringBootTest
@DisplayName("Mappers Integration Tests")
class MappersIntegrationTest {

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private BookingMapperInitial bookingMapperInitial;

    @Test
    @DisplayName("BookingMapper should map Booking to BookingDocument")
    void shouldMapBookingToBookingDocument() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Booking booking = Booking.builder()
                .id("BOOKING-001")
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .notes("Test booking")
                .bookingDate(now)
                .createdAt(now)
                .build();

        // When
        BookingDocument document = bookingMapper.toDocument(booking);

        // Then
        assertNotNull(document);
        assertEquals("BOOKING-001", document.getId());
        assertEquals("TRAVEL-001", document.getTravelId());
        assertEquals(1L, document.getPassengerId());
        assertEquals("Bogotá", document.getOrigin());
        assertEquals("Medellín", document.getDestination());
        assertEquals(2, document.getReservedSeats());
        assertEquals(150000.0, document.getTotalAmount());
        assertEquals(BookingStatus.PENDING, document.getStatus());
        assertEquals("Test booking", document.getNotes());
        assertEquals(now, document.getBookingDate());
        assertEquals(now, document.getCreatedAt());
    }

    @Test
    @DisplayName("BookingMapper should map BookingDocument to Booking")
    void shouldMapBookingDocumentToBooking() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        BookingDocument document = BookingDocument.builder()
                .id("BOOKING-001")
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.CONFIRMED)
                .notes("Test document")
                .bookingDate(now)
                .confirmationDate(now)
                .createdAt(now)
                .build();

        // When
        Booking booking = bookingMapper.toDomain(document);

        // Then
        assertNotNull(booking);
        assertEquals("BOOKING-001", booking.getId());
        assertEquals("TRAVEL-001", booking.getTravelId());
        assertEquals(1L, booking.getPassengerId());
        assertEquals("Bogotá", booking.getOrigin());
        assertEquals("Medellín", booking.getDestination());
        assertEquals(2, booking.getReservedSeats());
        assertEquals(150000.0, booking.getTotalAmount());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertEquals("Test document", booking.getNotes());
        assertEquals(now, booking.getBookingDate());
        assertEquals(now, booking.getConfirmationDate());
        assertEquals(now, booking.getCreatedAt());
    }

    @Test
    @DisplayName("BookingMapperInitial should map Booking to BookingResponse")
    void shouldMapBookingToBookingResponse() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Booking booking = Booking.builder()
                .id("BOOKING-002")
                .travelId("TRAVEL-002")
                .passengerId(2L)
                .origin("Cali")
                .destination("Barranquilla")
                .reservedSeats(3)
                .totalAmount(200000.0)
                .status(BookingStatus.COMPLETED)
                .notes("Test completed booking")
                .bookingDate(now)
                .confirmationDate(now)
                .paymentId("PAY-001")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        BookingResponse response = bookingMapperInitial.toResponse(booking);

        // Then
        assertNotNull(response);
        assertEquals("BOOKING-002", response.getId());
        assertEquals("TRAVEL-002", response.getTravelId());
        assertEquals(2L, response.getPassengerId());
        assertEquals("Cali", response.getOrigin());
        assertEquals("Barranquilla", response.getDestination());
        assertEquals(3, response.getReservedSeats());
        assertEquals(200000.0, response.getTotalAmount());
        assertEquals(BookingStatus.COMPLETED, response.getStatus());
        assertEquals("Test completed booking", response.getNotes());
        assertEquals(now, response.getBookingDate());
        assertEquals(now, response.getConfirmationDate());
        assertEquals("PAY-001", response.getPaymentId());
        assertEquals(now, response.getCreatedAt());
        assertEquals(now, response.getUpdatedAt());
    }

    @Test
    @DisplayName("BookingMapper should handle null values correctly")
    void shouldHandleNullValuesInMapping() {
        // Given
        Booking booking = Booking.builder()
                .id("BOOKING-003")
                .travelId("TRAVEL-003")
                .passengerId(3L)
                .origin("Medellín")
                .destination("Cartagena")
                .reservedSeats(1)
                .totalAmount(100000.0)
                .status(BookingStatus.PENDING)
                .build();

        // When
        BookingDocument document = bookingMapper.toDocument(booking);

        // Then
        assertNotNull(document);
        assertEquals("BOOKING-003", document.getId());
        assertEquals("TRAVEL-003", document.getTravelId());
        assertEquals(BookingStatus.PENDING, document.getStatus());
    }

    @Test
    @DisplayName("BookingMapper should map all booking statuses correctly")
    void shouldMapAllBookingStatusesCorrectly() {
        // Given & When & Then - PENDING
        Booking pendingBooking = Booking.builder()
                .id("B1")
                .travelId("T1")
                .passengerId(1L)
                .status(BookingStatus.PENDING)
                .build();
        BookingDocument pendingDoc = bookingMapper.toDocument(pendingBooking);
        assertEquals(BookingStatus.PENDING, pendingDoc.getStatus());

        // CONFIRMED
        Booking confirmedBooking = Booking.builder()
                .id("B2")
                .travelId("T2")
                .passengerId(2L)
                .status(BookingStatus.CONFIRMED)
                .build();
        BookingDocument confirmedDoc = bookingMapper.toDocument(confirmedBooking);
        assertEquals(BookingStatus.CONFIRMED, confirmedDoc.getStatus());

        // CANCELLED
        Booking cancelledBooking = Booking.builder()
                .id("B3")
                .travelId("T3")
                .passengerId(3L)
                .status(BookingStatus.CANCELLED)
                .build();
        BookingDocument cancelledDoc = bookingMapper.toDocument(cancelledBooking);
        assertEquals(BookingStatus.CANCELLED, cancelledDoc.getStatus());

        // COMPLETED
        Booking completedBooking = Booking.builder()
                .id("B4")
                .travelId("T4")
                .passengerId(4L)
                .status(BookingStatus.COMPLETED)
                .build();
        BookingDocument completedDoc = bookingMapper.toDocument(completedBooking);
        assertEquals(BookingStatus.COMPLETED, completedDoc.getStatus());
    }

    @Test
    @DisplayName("BookingMapperInitial should handle cancelled bookings")
    void shouldHandleCancelledBookings() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Booking booking = Booking.builder()
                .id("BOOKING-CANCELLED")
                .travelId("TRAVEL-005")
                .passengerId(5L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.CANCELLED)
                .cancellationDate(now)
                .bookingDate(now.minusDays(1))
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .build();

        // When
        BookingResponse response = bookingMapperInitial.toResponse(booking);

        // Then
        assertNotNull(response);
        assertEquals(BookingStatus.CANCELLED, response.getStatus());
        assertEquals(now, response.getCancellationDate());
        assertNotNull(response.getUpdatedAt());
    }
}
