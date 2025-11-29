package edu.dosw.rideci.application.usecase;

import edu.dosw.rideci.domain.model.Trip;
import edu.dosw.rideci.application.ports.in.SearchNearbyTripsPort;
import edu.dosw.rideci.application.ports.out.TripProjectionRepositoryPort;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SearchNearbyTripsUseCase implements SearchNearbyTripsPort {
    
    private final TripProjectionRepositoryPort tripRepository;
    
    public SearchNearbyTripsUseCase(TripProjectionRepositoryPort tripRepository) {
        this.tripRepository = tripRepository;
    }
    
    @Override
    public List<Trip> searchNearbyTrips(double latitude, double longitude, double radiusKm) {
        return tripRepository.findNearbyTrips(latitude, longitude, radiusKm);
    }
    
    @Override
    public List<Trip> searchNearbyTripsWithFilters(double latitude, double longitude, 
                                                  double radiusKm, Integer minimumSeats, 
                                                  String travelType) {
        return tripRepository.findNearbyTripsWithFilters(latitude, longitude, radiusKm, 
                                                        minimumSeats, travelType);
    }
}