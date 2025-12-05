package edu.dosw.rideci.unit.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.dosw.rideci.application.dto.CreateBookingCommand;
import edu.dosw.rideci.application.event.BookingCreatedEvent;
import edu.dosw.rideci.domain.model.enums.BookingStatus;

@DisplayName("DTOs and Events Tests")
class DTOsTest {

    @Test
    @DisplayName("Should create CreateBookingCommand with builder")
    void shouldCreateCommandWithBuilder() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        CreateBookingCommand command = CreateBookingCommand.builder()
                .travelId("TRAVEL-001")
                .passengerId(12345L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .notes("Test")
                .bookingDate(now)
                .build();

        // Then
        assertNotNull(command);
        assertEquals("TRAVEL-001", command.getTravelId());
        assertEquals(12345L, command.getPassengerId());
        assertEquals("Bogotá", command.getOrigin());
        assertEquals("Medellín", command.getDestination());
        assertEquals(2, command.getReservedSeats());
        assertEquals(150000.0, command.getTotalAmount());
        assertEquals(BookingStatus.PENDING, command.getStatus());
    }

    @Test
    @DisplayName("Should create BookingCreatedEvent with builder")
    void shouldCreateEventWithBuilder() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        BookingCreatedEvent event = BookingCreatedEvent.builder()
                .bookingId("BOOKING-001")
                .travelId("TRAVEL-001")
                .origin("Bogotá")
                .destination("Medellín")
                .passengerId(12345L)
                .reservedSeats(2)
                .timestamp(now)
                .build();

        // Then
        assertNotNull(event);
        assertEquals("BOOKING-001", event.getBookingId());
        assertEquals("TRAVEL-001", event.getTravelId());
        assertEquals("Bogotá", event.getOrigin());
        assertEquals("Medellín", event.getDestination());
        assertEquals(12345L, event.getPassengerId());
        assertEquals(2, event.getReservedSeats());
        assertEquals(now, event.getTimestamp());
    }

    @Test
    @DisplayName("Should update command properties")
    void shouldUpdateCommandProperties() {
        // Given
        CreateBookingCommand command = CreateBookingCommand.builder()
                .reservedSeats(2)
                .totalAmount(100000.0)
                .build();

        // When
        command.setReservedSeats(3);
        command.setTotalAmount(150000.0);

        // Then
        assertEquals(3, command.getReservedSeats());
        assertEquals(150000.0, command.getTotalAmount());
    }

    @Test
    @DisplayName("Should test command equals and hashCode")
    void shouldTestCommandEqualsAndHashCode() {
        // Given
        CreateBookingCommand command1 = CreateBookingCommand.builder()
                .travelId("TRAVEL-001")
                .passengerId(12345L)
                .reservedSeats(2)
                .build();

        CreateBookingCommand command2 = CreateBookingCommand.builder()
                .travelId("TRAVEL-001")
                .passengerId(12345L)
                .reservedSeats(2)
                .build();

        // Then
        assertEquals(command1, command2);
        assertEquals(command1.hashCode(), command2.hashCode());
    }

    @Test
    @DisplayName("Should test event equals and hashCode")
    void shouldTestEventEqualsAndHashCode() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        
        BookingCreatedEvent event1 = BookingCreatedEvent.builder()
                .bookingId("BOOKING-001")
                .travelId("TRAVEL-001")
                .timestamp(now)
                .build();

        BookingCreatedEvent event2 = BookingCreatedEvent.builder()
                .bookingId("BOOKING-001")
                .travelId("TRAVEL-001")
                .timestamp(now)
                .build();

        // Then
        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }
}
