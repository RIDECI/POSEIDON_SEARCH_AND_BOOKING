package edu.dosw.rideci.unit.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.dosw.rideci.application.dto.CreateBookingCommand;
import edu.dosw.rideci.application.event.BookingCreatedEvent;
import edu.dosw.rideci.application.ports.out.BookingRepositoryPort;
import edu.dosw.rideci.application.ports.out.EventPublisherPort;
import edu.dosw.rideci.application.service.BookingService;
import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.domain.model.enums.BookingStatus;
import edu.dosw.rideci.exceptions.BookingNotFoundException;
import edu.dosw.rideci.exceptions.InsufficientSeatsException;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookingService Tests")
class BookingServiceTest {

    @Mock
    private BookingRepositoryPort bookingRepositoryPort;

    @Mock
    private EventPublisherPort eventPublisherPort;

    @InjectMocks
    private BookingService bookingService;

    private CreateBookingCommand createBookingCommand;
    private Booking booking;

    @BeforeEach
    void setUp() {
        createBookingCommand = CreateBookingCommand.builder()
                .travelId("TRAVEL-001")
                .passengerId(12345L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .notes("Test booking")
                .bookingDate(LocalDateTime.now())
                .build();

        booking = Booking.builder()
                .id("BOOKING-001")
                .travelId("TRAVEL-001")
                .passengerId(12345L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .notes("Test booking")
                .bookingDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should create booking successfully and publish event")
    void shouldCreateBookingSuccessfully() {
        // Given
        when(bookingRepositoryPort.createBooking(any(Booking.class))).thenReturn(booking);
        doNothing().when(eventPublisherPort).publishBookingCreatedEvent(any(BookingCreatedEvent.class));

        // When
        Booking result = bookingService.createBooking(createBookingCommand);

        // Then
        assertNotNull(result);
        assertEquals("BOOKING-001", result.getId());
        assertEquals("TRAVEL-001", result.getTravelId());
        assertEquals(12345L, result.getPassengerId());
        assertEquals("Bogotá", result.getOrigin());
        assertEquals("Medellín", result.getDestination());
        assertEquals(2, result.getReservedSeats());
        assertEquals(150000.0, result.getTotalAmount());
        assertEquals(BookingStatus.PENDING, result.getStatus());

        verify(bookingRepositoryPort, times(1)).createBooking(any(Booking.class));
        verify(eventPublisherPort, times(1)).publishBookingCreatedEvent(any(BookingCreatedEvent.class));
    }

    @Test
    @DisplayName("Should create booking with default PENDING status when status is null")
    void shouldCreateBookingWithDefaultStatus() {
        // Given
        CreateBookingCommand commandWithoutStatus = CreateBookingCommand.builder()
                .travelId("TRAVEL-001")
                .passengerId(12345L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(null)
                .bookingDate(LocalDateTime.now())
                .build();

        when(bookingRepositoryPort.createBooking(any(Booking.class))).thenReturn(booking);
        doNothing().when(eventPublisherPort).publishBookingCreatedEvent(any(BookingCreatedEvent.class));

        // When
        Booking result = bookingService.createBooking(commandWithoutStatus);

        // Then
        assertNotNull(result);
        verify(bookingRepositoryPort, times(1)).createBooking(argThat(b -> b.getStatus() == BookingStatus.PENDING));
    }

    @Test
    @DisplayName("Should confirm booking successfully")
    void shouldConfirmBookingSuccessfully() {
        // Given
        String bookingId = "BOOKING-001";
        doNothing().when(bookingRepositoryPort).confirmBooking(bookingId);

        // When & Then
        assertDoesNotThrow(() -> bookingService.confirmBooking(bookingId));
        verify(bookingRepositoryPort, times(1)).confirmBooking(bookingId);
    }

    @Test
    @DisplayName("Should cancel booking successfully")
    void shouldCancelBookingSuccessfully() {
        // Given
        String bookingId = "BOOKING-001";
        doNothing().when(bookingRepositoryPort).cancelBooking(bookingId);

        // When & Then
        assertDoesNotThrow(() -> bookingService.cancelBooking(bookingId));
        verify(bookingRepositoryPort, times(1)).cancelBooking(bookingId);
    }

    @Test
    @DisplayName("Should get booking by id successfully")
    void shouldGetBookingByIdSuccessfully() {
        // Given
        String bookingId = "BOOKING-001";
        when(bookingRepositoryPort.findBookingById(bookingId)).thenReturn(Optional.of(booking));

        // When
        Booking result = bookingService.getBookingById(bookingId);

        // Then
        assertNotNull(result);
        assertEquals(bookingId, result.getId());
        verify(bookingRepositoryPort, times(1)).findBookingById(bookingId);
    }

    @Test
    @DisplayName("Should throw BookingNotFoundException when booking not found by id")
    void shouldThrowExceptionWhenBookingNotFoundById() {
        // Given
        String bookingId = "NON-EXISTENT";
        when(bookingRepositoryPort.findBookingById(bookingId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(bookingId));
        verify(bookingRepositoryPort, times(1)).findBookingById(bookingId);
    }

    @Test
    @DisplayName("Should get bookings by passenger id successfully")
    void shouldGetBookingsByPassengerIdSuccessfully() {
        // Given
        Long passengerId = 12345L;
        List<Booking> bookings = Arrays.asList(booking, booking);
        when(bookingRepositoryPort.findBookingsByPassengerId(passengerId)).thenReturn(bookings);

        // When
        List<Booking> result = bookingService.getBookingsByPassengerId(passengerId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookingRepositoryPort, times(1)).findBookingsByPassengerId(passengerId);
    }

    @Test
    @DisplayName("Should get bookings by travel id successfully")
    void shouldGetBookingsByTravelIdSuccessfully() {
        // Given
        String travelId = "TRAVEL-001";
        List<Booking> bookings = Arrays.asList(booking, booking);
        when(bookingRepositoryPort.findBookingsByTravelId(travelId)).thenReturn(bookings);

        // When
        List<Booking> result = bookingService.getBookingsByTravelId(travelId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookingRepositoryPort, times(1)).findBookingsByTravelId(travelId);
    }

    @Test
    @DisplayName("Should update seats successfully")
    void shouldUpdateSeatsSuccessfully() {
        // Given
        String bookingId = "BOOKING-001";
        int newSeats = 3;
        Booking updatedBooking = Booking.builder()
                .id(bookingId)
                .reservedSeats(newSeats)
                .build();

        when(bookingRepositoryPort.findBookingById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepositoryPort.updateBooking(any(Booking.class))).thenReturn(updatedBooking);

        // When
        Booking result = bookingService.updateSeats(bookingId, newSeats);

        // Then
        assertNotNull(result);
        assertEquals(newSeats, result.getReservedSeats());
        verify(bookingRepositoryPort, times(1)).findBookingById(bookingId);
        verify(bookingRepositoryPort, times(1)).updateBooking(any(Booking.class));
    }

    @Test
    @DisplayName("Should throw InsufficientSeatsException when updating with invalid seats")
    void shouldThrowExceptionWhenUpdatingWithInvalidSeats() {
        // Given
        String bookingId = "BOOKING-001";
        int invalidSeats = 0;
        when(bookingRepositoryPort.findBookingById(bookingId)).thenReturn(Optional.of(booking));

        // When & Then
        assertThrows(InsufficientSeatsException.class, () -> bookingService.updateSeats(bookingId, invalidSeats));
        verify(bookingRepositoryPort, times(1)).findBookingById(bookingId);
        verify(bookingRepositoryPort, never()).updateBooking(any(Booking.class));
    }

    @Test
    @DisplayName("Should throw BookingNotFoundException when updating non-existent booking")
    void shouldThrowExceptionWhenUpdatingNonExistentBooking() {
        // Given
        String bookingId = "NON-EXISTENT";
        int newSeats = 3;
        when(bookingRepositoryPort.findBookingById(bookingId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookingNotFoundException.class, () -> bookingService.updateSeats(bookingId, newSeats));
        verify(bookingRepositoryPort, times(1)).findBookingById(bookingId);
        verify(bookingRepositoryPort, never()).updateBooking(any(Booking.class));
    }

    @Test
    @DisplayName("Should complete booking successfully")
    void shouldCompleteBookingSuccessfully() {
        // Given
        String bookingId = "BOOKING-001";
        doNothing().when(bookingRepositoryPort).completeBooking(bookingId);

        // When & Then
        assertDoesNotThrow(() -> bookingService.completeBooking(bookingId));
        verify(bookingRepositoryPort, times(1)).completeBooking(bookingId);
    }

    @Test
    @DisplayName("Should validate availability successfully when seats available")
    void shouldValidateAvailabilitySuccessfully() {
        // Given
        String travelId = "TRAVEL-001";
        int requestedSeats = 10;
        int reservedSeats = 30;
        when(bookingRepositoryPort.countReservedSeatsByTravelId(travelId)).thenReturn(reservedSeats);

        // When
        boolean result = bookingService.validateAvailability(travelId, requestedSeats);

        // Then
        assertTrue(result);
        verify(bookingRepositoryPort, times(1)).countReservedSeatsByTravelId(travelId);
    }

    @Test
    @DisplayName("Should return false when no seats available")
    void shouldReturnFalseWhenNoSeatsAvailable() {
        // Given
        String travelId = "TRAVEL-001";
        int requestedSeats = 15;
        int reservedSeats = 45; // 45 + 15 = 60 > 50
        when(bookingRepositoryPort.countReservedSeatsByTravelId(travelId)).thenReturn(reservedSeats);

        // When
        boolean result = bookingService.validateAvailability(travelId, requestedSeats);

        // Then
        assertFalse(result);
        verify(bookingRepositoryPort, times(1)).countReservedSeatsByTravelId(travelId);
    }
}
