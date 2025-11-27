package edu.dosw.rideci.infrastructure.persistence.entity;

import edu.dosw.rideci.domain.model.enums.TravelType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "travels")
public class TravelDocument {

    @Id
    private String id;  // Changed to String for MongoDB
    
    @Field("ride_id")
    private String rideId;  // Added for compatibility with repository
    
    @Field("driver_id")
    private Long driverId;
    
    @Field("available_slots")
    private int availableSlots;
    
    @Field("available_seats")  // Added alias for query compatibility
    private int availableSeats;
    
    private String status;
    
    @Field("travel_type")
    private TravelType travelType;
    
    @Field("estimated_cost")
    private double estimatedCost;
    
    @Field("departure_date_time")
    private LocalDateTime departureDateAndTime;
    
    @Field("departure_time")  // Added alias for query compatibility
    private LocalDateTime departureTime;
    
    @Field("passengers_id")
    private List<Long> passengersId;
    
    private String conditions;
    
    // Location fields for queries
    @Field("origin_lat")
    private Double originLat;
    
    @Field("origin_lng")
    private Double originLng;
    
    @Field("destiny_lat")
    private Double destinyLat;
    
    @Field("destiny_lng")
    private Double destinyLng;
    
    // Constructors
    public TravelDocument() {}
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getRideId() { return rideId; }
    public void setRideId(String rideId) { this.rideId = rideId; }
    
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    
    public int getAvailableSlots() { return availableSlots; }
    public void setAvailableSlots(int availableSlots) { 
        this.availableSlots = availableSlots;
        this.availableSeats = availableSlots; // Keep in sync
    }
    
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { 
        this.availableSeats = availableSeats;
        this.availableSlots = availableSeats; // Keep in sync
    }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public TravelType getTravelType() { return travelType; }
    public void setTravelType(TravelType travelType) { this.travelType = travelType; }
    
    public double getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(double estimatedCost) { this.estimatedCost = estimatedCost; }
    
    public LocalDateTime getDepartureDateAndTime() { return departureDateAndTime; }
    public void setDepartureDateAndTime(LocalDateTime departureDateAndTime) { 
        this.departureDateAndTime = departureDateAndTime;
        this.departureTime = departureDateAndTime; // Keep in sync
    }
    
    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { 
        this.departureTime = departureTime;
        this.departureDateAndTime = departureTime; // Keep in sync
    }
    
    public List<Long> getPassengersId() { return passengersId; }
    public void setPassengersId(List<Long> passengersId) { this.passengersId = passengersId; }
    
    public String getConditions() { return conditions; }
    public void setConditions(String conditions) { this.conditions = conditions; }
    
    public Double getOriginLat() { return originLat; }
    public void setOriginLat(Double originLat) { this.originLat = originLat; }
    
    public Double getOriginLng() { return originLng; }
    public void setOriginLng(Double originLng) { this.originLng = originLng; }
    
    public Double getDestinyLat() { return destinyLat; }
    public void setDestinyLat(Double destinyLat) { this.destinyLat = destinyLat; }
    
    public Double getDestinyLng() { return destinyLng; }
    public void setDestinyLng(Double destinyLng) { this.destinyLng = destinyLng; }
}
