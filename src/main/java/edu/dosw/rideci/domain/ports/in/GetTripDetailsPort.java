package edu.dosw.rideci.domain.ports.in;

import edu.dosw.rideci.domain.model.Trip;
import java.util.Optional;

/**
 * Input port for getting trip details
 */
public interface GetTripDetailsPort {
    
    /**
     * Gets trip details by ID
     */
    Optional<Trip> getTripDetails(String tripId);
}