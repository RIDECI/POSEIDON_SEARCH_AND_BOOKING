package edu.dosw.rideci.unit.usecase;

import edu.dosw.rideci.application.usecase.CreateBookingUseCase;
import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.domain.model.Trip;
import edu.dosw.rideci.domain.model.enums.BookingStatus;
import edu.dosw.rideci.domain.model.enums.TravelType;
import edu.dosw.rideci.application.ports.out.BookingRepositoryPort;
import edu.dosw.rideci.application.ports.out.TripProjectionRepositoryPort;
import edu.dosw.rideci.application.ports.out.EventPublisherPort;
import edu.dosw.rideci.exceptions.TripNotFoundException;
import edu.dosw.rideci.exceptions.InsufficientSeatsException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CreateBookingUseCaseTest {

    @Mock
    private BookingRepositoryPort bookingRepository;

    @Mock
    private TripProjectionRepositoryPort tripRepository;

    @Mock
    private EventPublisherPort eventPublisher;

    private CreateBookingUseCase createBookingUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createBookingUseCase = new CreateBookingUseCase(bookingRepository, tripRepository, eventPublisher);
    }

    @Test
    @DisplayName("Should create booking successfully")
    void shouldCreateBookingSuccessfully() {
        // Given
        String tripId = "trip-123";
        String passengerId = "passenger-456";
        int reservedSeats = 2;
        String notes = "Test booking";

        Trip trip = createTestTrip();
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Booking result = createBookingUseCase.createBooking(tripId, passengerId, reservedSeats, notes);

        // Then
        assertNotNull(result);
        assertEquals(tripId, result.getTripId());
        assertEquals(passengerId, result.getPassengerId());
        assertEquals(reservedSeats, result.getReservedSeats());
        assertEquals(BookingStatus.PENDING, result.getStatus());
        assertEquals(notes, result.getNotes());

        verify(tripRepository).findById(tripId);
        verify(bookingRepository).save(any(Booking.class));
        verify(tripRepository).save(any(Trip.class));
        verify(eventPublisher).publishBookingCreated(anyString(), eq(tripId), eq(passengerId), eq(reservedSeats), anyString());
        verify(eventPublisher).publishTripSeatsUpdate(eq(tripId), anyInt());
    }

    @Test
    @DisplayName("Should throw exception when trip not found")
    void shouldThrowExceptionWhenTripNotFound() {
        // Given
        String tripId = "non-existent-trip";
        String passengerId = "passenger-456";
        int reservedSeats = 2;
        String notes = "Test booking";

        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TripNotFoundException.class, 
            () -> createBookingUseCase.createBooking(tripId, passengerId, reservedSeats, notes));

        verify(tripRepository).findById(tripId);
        verifyNoInteractions(bookingRepository, eventPublisher);
    }

    @Test
    @DisplayName("Should throw exception when insufficient seats")
    void shouldThrowExceptionWhenInsufficientSeats() {
        // Given
        String tripId = "trip-123";
        String passengerId = "passenger-456";
        int reservedSeats = 5; // More than available
        String notes = "Test booking";

        Trip trip = createTestTrip();
        trip.setAvailableSeats(2); // Only 2 seats available
        
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));

        // When & Then
        assertThrows(InsufficientSeatsException.class, 
            () -> createBookingUseCase.createBooking(tripId, passengerId, reservedSeats, notes));

        verify(tripRepository).findById(tripId);
        verifyNoInteractions(bookingRepository, eventPublisher);
    }

    private Trip createTestTrip() {
        Trip trip = new Trip();
        trip.setId("trip-123");
        trip.setDriverId("driver-456");
        trip.setOrigin("Bogotá");
        trip.setDestination("Medellín");
        trip.setDepartureDateTime(LocalDateTime.now().plusDays(1));
        trip.setArrivalDateTime(LocalDateTime.now().plusDays(1).plusHours(8));
        trip.setTravelType(TravelType.CAR);
        trip.setAvailableSeats(4);
        trip.setTotalSeats(4);
        trip.setPrice(new BigDecimal("80000"));
        trip.setDescription("Test trip");
        trip.setActive(true);
        return trip;
    }
}