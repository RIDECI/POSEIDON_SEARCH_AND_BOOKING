package edu.dosw.rideci.unit.domain;

import edu.dosw.rideci.domain.model.Trip;
import edu.dosw.rideci.domain.model.enums.TravelType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TripTest {

    @Test
    @DisplayName("Should create trip successfully")
    void shouldCreateTripSuccessfully() {
        // Given
        String id = "trip-123";
        String driverId = "driver-456";
        String origin = "Bogotá";
        String destination = "Medellín";
        LocalDateTime departureDateTime = LocalDateTime.now().plusDays(1);
        LocalDateTime arrivalDateTime = departureDateTime.plusHours(8);
        TravelType travelType = TravelType.CAR;
        Integer availableSeats = 3;
        Integer totalSeats = 4;
        BigDecimal price = new BigDecimal("80000");
        String description = "Viaje cómodo y seguro";
        boolean active = true;

        // When
        Trip trip = new Trip(id, driverId, origin, destination, departureDateTime, 
                           arrivalDateTime, travelType, availableSeats, totalSeats,
                           price, description, 4.6097, -74.0817, 6.2442, -75.5812, active);

        // Then
        assertNotNull(trip);
        assertEquals(id, trip.getId());
        assertEquals(driverId, trip.getDriverId());
        assertEquals(origin, trip.getOrigin());
        assertEquals(destination, trip.getDestination());
        assertEquals(departureDateTime, trip.getDepartureDateTime());
        assertEquals(arrivalDateTime, trip.getArrivalDateTime());
        assertEquals(travelType, trip.getTravelType());
        assertEquals(availableSeats, trip.getAvailableSeats());
        assertEquals(totalSeats, trip.getTotalSeats());
        assertEquals(price, trip.getPrice());
        assertEquals(description, trip.getDescription());
        assertTrue(trip.isActive());
    }

    @Test
    @DisplayName("Should return true when has available seats")
    void shouldReturnTrueWhenHasAvailableSeats() {
        // Given
        Trip trip = new Trip();
        trip.setAvailableSeats(4);
        int requestedSeats = 2;

        // When
        boolean hasSeats = trip.hasAvailableSeats(requestedSeats);

        // Then
        assertTrue(hasSeats);
    }

    @Test
    @DisplayName("Should return false when not enough available seats")
    void shouldReturnFalseWhenNotEnoughAvailableSeats() {
        // Given
        Trip trip = new Trip();
        trip.setAvailableSeats(1);
        int requestedSeats = 3;

        // When
        boolean hasSeats = trip.hasAvailableSeats(requestedSeats);

        // Then
        assertFalse(hasSeats);
    }

    @Test
    @DisplayName("Should reserve seats successfully")
    void shouldReserveSeatsSuccessfully() {
        // Given
        Trip trip = new Trip();
        trip.setAvailableSeats(4);
        int seatsToReserve = 2;

        // When
        trip.reserveSeats(seatsToReserve);

        // Then
        assertEquals(2, trip.getAvailableSeats());
    }

    @Test
    @DisplayName("Should throw exception when trying to reserve more seats than available")
    void shouldThrowExceptionWhenTryingToReserveMoreSeatsThanAvailable() {
        // Given
        Trip trip = new Trip();
        trip.setAvailableSeats(1);
        int seatsToReserve = 3;

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> trip.reserveSeats(seatsToReserve));
        assertEquals("Not enough available seats", exception.getMessage());
    }

    @Test
    @DisplayName("Should release seats successfully")
    void shouldReleaseSeatsSuccessfully() {
        // Given
        Trip trip = new Trip();
        trip.setAvailableSeats(2);
        trip.setTotalSeats(4);
        int seatsToRelease = 1;

        // When
        trip.releaseSeats(seatsToRelease);

        // Then
        assertEquals(3, trip.getAvailableSeats());
    }

    @Test
    @DisplayName("Should throw exception when releasing more seats than total")
    void shouldThrowExceptionWhenReleasingMoreSeatsThanTotal() {
        // Given
        Trip trip = new Trip();
        trip.setAvailableSeats(3);
        trip.setTotalSeats(4);
        int seatsToRelease = 2;

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> trip.releaseSeats(seatsToRelease));
        assertEquals("Cannot release more seats than total", exception.getMessage());
    }
}