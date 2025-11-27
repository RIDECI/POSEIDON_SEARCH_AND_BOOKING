package edu.dosw.rideci.application.mappers;

import edu.dosw.rideci.domain.model.Trip;
import edu.dosw.rideci.domain.model.enums.TravelType;
import edu.dosw.rideci.infrastructure.dto.Response.TripResponse;
import edu.dosw.rideci.application.event.TripCreatedEvent;
import edu.dosw.rideci.application.event.TripUpdateEvent;
import org.springframework.stereotype.Component;

@Component
public class TripMapper {
    
    /**
     * Convierte Trip a TripResponse
     */
    public TripResponse toTripResponse(Trip trip) {
        TripResponse response = new TripResponse();
        response.setId(trip.getId());
        response.setDriverId(trip.getDriverId());
        response.setOrigin(trip.getOrigin());
        response.setDestination(trip.getDestination());
        response.setDepartureDateTime(trip.getDepartureDateTime());
        response.setArrivalDateTime(trip.getArrivalDateTime());
        response.setTravelType(trip.getTravelType().toString());
        response.setAvailableSeats(trip.getAvailableSeats());
        response.setTotalSeats(trip.getTotalSeats());
        response.setPrice(trip.getPrice());
        response.setDescription(trip.getDescription());
        response.setOriginLatitude(trip.getOriginLatitude());
        response.setOriginLongitude(trip.getOriginLongitude());
        response.setDestinationLatitude(trip.getDestinationLatitude());
        response.setDestinationLongitude(trip.getDestinationLongitude());
        response.setActive(trip.isActive());
        response.setCreatedAt(trip.getCreatedAt());
        response.setUpdatedAt(trip.getUpdatedAt());
        return response;
    }
    
    /**
     * Convierte TripCreatedEvent a Trip
     */
    public Trip fromTripCreatedEvent(TripCreatedEvent event) {
        return new Trip(
                event.getTripId(),
                event.getDriverId(),
                event.getOrigin(),
                event.getDestination(),
                event.getDepartureDateTime(),
                event.getArrivalDateTime(),
                TravelType.valueOf(event.getTravelType()),
                event.getTotalSeats(), // cupos disponibles = cupos totales inicialmente
                event.getTotalSeats(),
                event.getPrice(),
                event.getDescription(),
                event.getOriginLatitude(),
                event.getOriginLongitude(),
                event.getDestinationLatitude(),
                event.getDestinationLongitude(),
                true // activo por defecto
        );
    }
    
    /**
     * Convierte TripUpdateEvent a Trip
     */
    public Trip fromTripUpdateEvent(TripUpdateEvent event) {
        return new Trip(
                event.getTripId(),
                event.getDriverId(),
                event.getOrigin(),
                event.getDestination(),
                event.getDepartureDateTime(),
                event.getArrivalDateTime(),
                TravelType.valueOf(event.getTravelType()),
                event.getTotalSeats(), // se ajustará en el use case
                event.getTotalSeats(),
                event.getPrice(),
                event.getDescription(),
                event.getOriginLatitude(),
                event.getOriginLongitude(),
                event.getDestinationLatitude(),
                event.getDestinationLongitude(),
                event.isActive()
        );
    }
}