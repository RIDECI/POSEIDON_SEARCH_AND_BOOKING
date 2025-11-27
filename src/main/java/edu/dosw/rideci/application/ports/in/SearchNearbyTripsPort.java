package edu.dosw.rideci.application.ports.in;

import edu.dosw.rideci.domain.model.Trip;
import java.util.List;

/**
 * Input port for searching nearby trips by location
 */
public interface SearchNearbyTripsPort {
    
    /**
     * Search trips near a specific location
     */
    List<Trip> searchNearbyTrips(double latitude, 
                                double longitude, 
                                double radiusKm);
    
    /**
     * Search nearby trips with additional filters
     */
    List<Trip> searchNearbyTripsWithFilters(double latitude,
                                           double longitude,
                                           double radiusKm,
                                           Integer minimumSeats,
                                           String travelType);
}