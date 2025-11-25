package edu.dosw.rideci.domain.ports.in;

import edu.dosw.rideci.domain.model.Trip;
import edu.dosw.rideci.domain.model.enums.TravelType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Input port for searching trips
 */
public interface SearchTripsPort {
    
    /**
     * Search trips by filters
     */
    List<Trip> searchTrips(Optional<String> origin,
                          Optional<String> destination,
                          Optional<LocalDateTime> fromDate,
                          Optional<LocalDateTime> toDate,
                          Optional<TravelType> travelType,
                          Optional<Integer> minimumSeats);
    
    /**
     * Search trips with pagination
     */
    List<Trip> searchTripsWithPagination(Optional<String> origin,
                                         Optional<String> destination,
                                         Optional<LocalDateTime> fromDate,
                                         Optional<LocalDateTime> toDate,
                                         Optional<TravelType> travelType,
                                         Optional<Integer> minimumSeats,
                                         int page,
                                         int size);
}