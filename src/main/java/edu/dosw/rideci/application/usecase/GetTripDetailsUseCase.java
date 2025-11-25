package edu.dosw.rideci.application.usecase;

import edu.dosw.rideci.domain.model.Trip;
import edu.dosw.rideci.domain.ports.in.GetTripDetailsPort;
import edu.dosw.rideci.domain.ports.out.TripProjectionRepositoryPort;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class GetTripDetailsUseCase implements GetTripDetailsPort {
    
    private final TripProjectionRepositoryPort tripRepository;
    
    public GetTripDetailsUseCase(TripProjectionRepositoryPort tripRepository) {
        this.tripRepository = tripRepository;
    }
    
    @Override
    public Optional<Trip> getTripDetails(String tripId) {
        return tripRepository.findById(tripId);
    }
}