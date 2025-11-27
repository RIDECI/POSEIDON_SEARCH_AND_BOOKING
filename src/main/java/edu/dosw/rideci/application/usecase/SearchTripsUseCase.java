package edu.dosw.rideci.application.usecase;

import edu.dosw.rideci.domain.model.Trip;
import edu.dosw.rideci.domain.model.enums.TravelType;
import edu.dosw.rideci.application.ports.in.SearchTripsPort;
import edu.dosw.rideci.application.ports.out.TripProjectionRepositoryPort;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class SearchTripsUseCase implements SearchTripsPort {
    
    private final TripProjectionRepositoryPort tripRepository;
    
    public SearchTripsUseCase(TripProjectionRepositoryPort tripRepository) {
        this.tripRepository = tripRepository;
    }
    
    @Override
    public List<Trip> searchTrips(Optional<String> origin,
                                  Optional<String> destination,
                                  Optional<LocalDateTime> departureFrom,
                                  Optional<LocalDateTime> departureTo,
                                  Optional<TravelType> travelType,
                                  Optional<Integer> minimumSeats) {
        return tripRepository.findByFilters(origin, destination, departureFrom, 
                                           departureTo, travelType, minimumSeats);
    }
    
    @Override
    public List<Trip> searchTripsWithPagination(Optional<String> origin,
                                               Optional<String> destination,
                                               Optional<LocalDateTime> departureFrom,
                                               Optional<LocalDateTime> departureTo,
                                               Optional<TravelType> travelType,
                                               Optional<Integer> minimumSeats,
                                               int page, int size) {
        return tripRepository.findByFiltersWithPagination(origin, destination, departureFrom,
                                                         departureTo, travelType, minimumSeats,
                                                         page, size);
    }
}