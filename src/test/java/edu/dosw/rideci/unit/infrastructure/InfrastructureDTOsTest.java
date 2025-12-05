package edu.dosw.rideci.unit.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.dosw.rideci.domain.model.enums.BookingStatus;
import edu.dosw.rideci.infrastructure.controllers.dto.Request.BookingRequest;
import edu.dosw.rideci.infrastructure.controllers.dto.Request.UpdateSeatsRequest;
import edu.dosw.rideci.infrastructure.controllers.dto.Response.BookingResponse;

@DisplayName("DTOs Infrastructure Tests")
class InfrastructureDTOsTest {

    @Test
    @DisplayName("Should create BookingRequest with builder")
    void shouldCreateBookingRequestWithBuilder() {
        // Given & When
        LocalDateTime now = LocalDateTime.now();
        BookingRequest request = BookingRequest.builder()
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .notes("Test request")
                .bookingDate(now)
                .build();

        // Then
        assertNotNull(request);
        assertEquals("TRAVEL-001", request.getTravelId());
        assertEquals(1L, request.getPassengerId());
        assertEquals("Bogotá", request.getOrigin());
        assertEquals("Medellín", request.getDestination());
        assertEquals(2, request.getReservedSeats());
        assertEquals(150000.0, request.getTotalAmount());
        assertEquals(BookingStatus.PENDING, request.getStatus());
        assertEquals("Test request", request.getNotes());
        assertEquals(now, request.getBookingDate());
    }

    @Test
    @DisplayName("Should set and get BookingRequest fields")
    void shouldSetAndGetBookingRequestFields() {
        // Given
        BookingRequest request = new BookingRequest();
        LocalDateTime now = LocalDateTime.now();

        // When
        request.setTravelId("TRAVEL-002");
        request.setPassengerId(2L);
        request.setOrigin("Cali");
        request.setDestination("Barranquilla");
        request.setReservedSeats(3);
        request.setTotalAmount(200000.0);
        request.setStatus(BookingStatus.CONFIRMED);
        request.setNotes("Updated request");
        request.setBookingDate(now);

        // Then
        assertEquals("TRAVEL-002", request.getTravelId());
        assertEquals(2L, request.getPassengerId());
        assertEquals("Cali", request.getOrigin());
        assertEquals("Barranquilla", request.getDestination());
        assertEquals(3, request.getReservedSeats());
        assertEquals(200000.0, request.getTotalAmount());
        assertEquals(BookingStatus.CONFIRMED, request.getStatus());
        assertEquals("Updated request", request.getNotes());
        assertEquals(now, request.getBookingDate());
    }

    @Test
    @DisplayName("Should verify BookingRequest equals and hashCode")
    void shouldVerifyBookingRequestEqualsAndHashCode() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        BookingRequest request1 = BookingRequest.builder()
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .bookingDate(now)
                .build();

        BookingRequest request2 = BookingRequest.builder()
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .bookingDate(now)
                .build();

        BookingRequest request3 = BookingRequest.builder()
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
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
    }

    @Test
    @DisplayName("Should create BookingResponse with builder")
    void shouldCreateBookingResponseWithBuilder() {
        // Given & When
        LocalDateTime now = LocalDateTime.now();
        BookingResponse response = BookingResponse.builder()
                .id("BOOKING-001")
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .notes("Test response")
                .bookingDate(now)
                .createdAt(now)
                .build();

        // Then
        assertNotNull(response);
        assertEquals("BOOKING-001", response.getId());
        assertEquals("TRAVEL-001", response.getTravelId());
        assertEquals(1L, response.getPassengerId());
        assertEquals("Bogotá", response.getOrigin());
        assertEquals("Medellín", response.getDestination());
        assertEquals(2, response.getReservedSeats());
        assertEquals(150000.0, response.getTotalAmount());
        assertEquals(BookingStatus.PENDING, response.getStatus());
        assertEquals("Test response", response.getNotes());
        assertEquals(now, response.getBookingDate());
        assertEquals(now, response.getCreatedAt());
    }

    @Test
    @DisplayName("Should set and get BookingResponse fields")
    void shouldSetAndGetBookingResponseFields() {
        // Given
        BookingResponse response = new BookingResponse();
        LocalDateTime now = LocalDateTime.now();

        // When
        response.setId("BOOKING-002");
        response.setTravelId("TRAVEL-002");
        response.setPassengerId(2L);
        response.setOrigin("Cali");
        response.setDestination("Barranquilla");
        response.setReservedSeats(3);
        response.setTotalAmount(200000.0);
        response.setStatus(BookingStatus.CONFIRMED);
        response.setNotes("Updated response");
        response.setBookingDate(now);
        response.setConfirmationDate(now);
        response.setPaymentId("PAY-001");
        response.setCreatedAt(now);
        response.setUpdatedAt(now);

        // Then
        assertEquals("BOOKING-002", response.getId());
        assertEquals("TRAVEL-002", response.getTravelId());
        assertEquals(2L, response.getPassengerId());
        assertEquals("Cali", response.getOrigin());
        assertEquals("Barranquilla", response.getDestination());
        assertEquals(3, response.getReservedSeats());
        assertEquals(200000.0, response.getTotalAmount());
        assertEquals(BookingStatus.CONFIRMED, response.getStatus());
        assertEquals("Updated response", response.getNotes());
        assertEquals(now, response.getBookingDate());
        assertEquals(now, response.getConfirmationDate());
        assertEquals("PAY-001", response.getPaymentId());
        assertEquals(now, response.getCreatedAt());
        assertEquals(now, response.getUpdatedAt());
    }

    @Test
    @DisplayName("Should verify BookingResponse equals and hashCode")
    void shouldVerifyBookingResponseEqualsAndHashCode() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        BookingResponse response1 = BookingResponse.builder()
                .id("BOOKING-001")
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .bookingDate(now)
                .build();

        BookingResponse response2 = BookingResponse.builder()
                .id("BOOKING-001")
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .bookingDate(now)
                .build();

        BookingResponse response3 = BookingResponse.builder()
                .id("BOOKING-002")
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
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1, response3);
    }

    @Test
    @DisplayName("Should create UpdateSeatsRequest")
    void shouldCreateUpdateSeatsRequest() {
        // Given & When
        UpdateSeatsRequest request = new UpdateSeatsRequest();
        request.setReservedSeats(5);

        // Then
        assertNotNull(request);
        assertEquals(5, request.getReservedSeats());
    }

    @Test
    @DisplayName("Should handle cancellation in BookingResponse")
    void shouldHandleCancellationInBookingResponse() {
        // Given
        BookingResponse response = BookingResponse.builder()
                .id("BOOKING-001")
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .status(BookingStatus.PENDING)
                .build();

        // When
        LocalDateTime cancellationDate = LocalDateTime.now();
        response.setCancellationDate(cancellationDate);
        response.setStatus(BookingStatus.CANCELLED);

        // Then
        assertEquals(cancellationDate, response.getCancellationDate());
        assertEquals(BookingStatus.CANCELLED, response.getStatus());
    }
}
