package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.enums.TravelType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * Trip - Read Model Entity
 * This entity represents a trip that is synchronized from another microservice
 * through events. It is used for queries and search filters.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    
    private String id;
    private String driverId;
    private String origin;
    private String destination;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private TravelType travelType;
    private Integer availableSeats;
    private Integer totalSeats;
    private BigDecimal price;
    private String description;
    private Double originLatitude;
    private Double originLongitude;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Métodos de negocio
    
    /**
     * Validates if there are available seats for a reservation
     */
    public boolean hasAvailableSeats(int requestedSeats) {
        return this.availableSeats >= requestedSeats;
    }
    
    /**
     * Reserves seats in the trip
     */
    public void reserveSeats(int seats) {
        if (!hasAvailableSeats(seats)) {
            throw new IllegalStateException("Not enough available seats");
        }
        this.availableSeats -= seats;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Releases seats from the trip (due to cancellation)
     */
    public void releaseSeats(int seats) {
        if (this.availableSeats + seats > this.totalSeats) {
            throw new IllegalStateException("Cannot release more seats than total");
        }
        this.availableSeats += seats;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Updates the trip seats
     */
    public void updateSeats(Integer newAvailableSeats) {
        if (newAvailableSeats < 0 || newAvailableSeats > this.totalSeats) {
            throw new IllegalArgumentException("Available seats must be between 0 and " + this.totalSeats);
        }
        this.availableSeats = newAvailableSeats;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Constructor for creating a new trip from event data
     */
    public Trip(String id, String driverId, String origin, String destination, 
                LocalDateTime departureDateTime, LocalDateTime arrivalDateTime,
                TravelType travelType, Integer availableSeats, Integer totalSeats,
                BigDecimal price, String description, Double originLatitude,
                Double originLongitude, Double destinationLatitude, Double destinationLongitude,
                boolean active) {
        this.id = id;
        this.driverId = driverId;
        this.origin = origin;
        this.destination = destination;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
        this.travelType = travelType;
        this.availableSeats = availableSeats;
        this.totalSeats = totalSeats;
        this.price = price;
        this.description = description;
        this.originLatitude = originLatitude;
        this.originLongitude = originLongitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
        this.active = active;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}