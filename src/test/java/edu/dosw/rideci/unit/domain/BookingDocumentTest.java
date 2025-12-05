package edu.dosw.rideci.unit.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.dosw.rideci.domain.model.enums.BookingStatus;
import edu.dosw.rideci.infrastructure.persistence.entity.BookingDocument;

@DisplayName("BookingDocument Entity Tests")
class BookingDocumentTest {

    @Test
    @DisplayName("Should create BookingDocument with builder")
    void shouldCreateBookingDocumentWithBuilder() {
        // Given & When
        LocalDateTime now = LocalDateTime.now();
        BookingDocument document = BookingDocument.builder()
                .id("DOC-001")
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .notes("Test document")
                .bookingDate(now)
                .createdAt(now)
                .build();

        // Then
        assertNotNull(document);
        assertEquals("DOC-001", document.getId());
        assertEquals("TRAVEL-001", document.getTravelId());
        assertEquals(1L, document.getPassengerId());
        assertEquals("Bogotá", document.getOrigin());
        assertEquals("Medellín", document.getDestination());
        assertEquals(2, document.getReservedSeats());
        assertEquals(150000.0, document.getTotalAmount());
        assertEquals(BookingStatus.PENDING, document.getStatus());
        assertEquals("Test document", document.getNotes());
        assertEquals(now, document.getBookingDate());
        assertEquals(now, document.getCreatedAt());
    }

    @Test
    @DisplayName("Should set and get all fields correctly")
    void shouldSetAndGetAllFieldsCorrectly() {
        // Given
        BookingDocument document = new BookingDocument();
        LocalDateTime now = LocalDateTime.now();

        // When
        document.setId("DOC-002");
        document.setTravelId("TRAVEL-002");
        document.setPassengerId(2L);
        document.setOrigin("Cali");
        document.setDestination("Barranquilla");
        document.setReservedSeats(3);
        document.setTotalAmount(200000.0);
        document.setStatus(BookingStatus.CONFIRMED);
        document.setNotes("Updated document");
        document.setBookingDate(now);
        document.setConfirmationDate(now);
        document.setPaymentId("PAY-001");
        document.setCreatedAt(now);
        document.setUpdatedAt(now);

        // Then
        assertEquals("DOC-002", document.getId());
        assertEquals("TRAVEL-002", document.getTravelId());
        assertEquals(2L, document.getPassengerId());
        assertEquals("Cali", document.getOrigin());
        assertEquals("Barranquilla", document.getDestination());
        assertEquals(3, document.getReservedSeats());
        assertEquals(200000.0, document.getTotalAmount());
        assertEquals(BookingStatus.CONFIRMED, document.getStatus());
        assertEquals("Updated document", document.getNotes());
        assertEquals(now, document.getBookingDate());
        assertEquals(now, document.getConfirmationDate());
        assertEquals("PAY-001", document.getPaymentId());
        assertEquals(now, document.getCreatedAt());
        assertEquals(now, document.getUpdatedAt());
    }

    @Test
    @DisplayName("Should verify equals and hashCode")
    void shouldVerifyEqualsAndHashCode() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        BookingDocument document1 = BookingDocument.builder()
                .id("DOC-001")
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .bookingDate(now)
                .build();

        BookingDocument document2 = BookingDocument.builder()
                .id("DOC-001")
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .bookingDate(now)
                .build();

        BookingDocument document3 = BookingDocument.builder()
                .id("DOC-002")
                .travelId("TRAVEL-002")
                .passengerId(2L)
                .origin("Cali")
                .destination("Barranquilla")
                .reservedSeats(3)
                .totalAmount(200000.0)
                .status(BookingStatus.CONFIRMED)
                .bookingDate(now)
                .build();

        // Then
        assertEquals(document1, document2);
        assertEquals(document1.hashCode(), document2.hashCode());
        assertNotEquals(document1, document3);
        assertNotEquals(document1.hashCode(), document3.hashCode());
    }

    @Test
    @DisplayName("Should handle cancellation date")
    void shouldHandleCancellationDate() {
        // Given
        BookingDocument document = BookingDocument.builder()
                .id("DOC-001")
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .status(BookingStatus.PENDING)
                .build();

        // When
        LocalDateTime cancellationDate = LocalDateTime.now();
        document.setCancellationDate(cancellationDate);
        document.setStatus(BookingStatus.CANCELLED);

        // Then
        assertEquals(cancellationDate, document.getCancellationDate());
        assertEquals(BookingStatus.CANCELLED, document.getStatus());
    }

    @Test
    @DisplayName("Should create empty BookingDocument")
    void shouldCreateEmptyBookingDocument() {
        // When
        BookingDocument document = new BookingDocument();

        // Then
        assertNotNull(document);
    }

    @Test
    @DisplayName("Should handle all booking statuses")
    void shouldHandleAllBookingStatuses() {
        // Given
        BookingDocument document = BookingDocument.builder()
                .id("DOC-001")
                .build();

        // When & Then
        document.setStatus(BookingStatus.PENDING);
        assertEquals(BookingStatus.PENDING, document.getStatus());

        document.setStatus(BookingStatus.CONFIRMED);
        assertEquals(BookingStatus.CONFIRMED, document.getStatus());

        document.setStatus(BookingStatus.CANCELLED);
        assertEquals(BookingStatus.CANCELLED, document.getStatus());

        document.setStatus(BookingStatus.COMPLETED);
        assertEquals(BookingStatus.COMPLETED, document.getStatus());
    }
}
