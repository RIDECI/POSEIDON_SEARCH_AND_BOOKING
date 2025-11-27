package edu.dosw.rideci.application.usecase;

import edu.dosw.rideci.domain.model.Trip;
import edu.dosw.rideci.application.ports.out.TripProjectionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProcesarEventosDeTripUseCase {
    
    private final TripProjectionRepositoryPort tripRepository;
    
    public ProcesarEventosDeTripUseCase(TripProjectionRepositoryPort tripRepository) {
        this.tripRepository = tripRepository;
    }
    
    /**
     * Procesa evento de viaje creado
     */
    public void procesarTripCreado(Trip trip) {
        tripRepository.save(trip);
    }
    
    /**
     * Procesa evento de viaje actualizado
     */
    public void procesarTripActualizado(Trip trip) {
        Trip existingTrip = tripRepository.findById(trip.getId())
                .orElseThrow(() -> new IllegalArgumentException("Viaje no encontrado: " + trip.getId()));
        
        // Actualizar campos necesarios manteniendo el estado local de cupos
        existingTrip.setOrigin(trip.getOrigin());
        existingTrip.setDestination(trip.getDestination());
        existingTrip.setDepartureDateTime(trip.getDepartureDateTime());
        existingTrip.setArrivalDateTime(trip.getArrivalDateTime());
        existingTrip.setTravelType(trip.getTravelType());
        existingTrip.setPrice(trip.getPrice());
        existingTrip.setDescription(trip.getDescription());
        existingTrip.setOriginLatitude(trip.getOriginLatitude());
        existingTrip.setOriginLongitude(trip.getOriginLongitude());
        existingTrip.setDestinationLatitude(trip.getDestinationLatitude());
        existingTrip.setDestinationLongitude(trip.getDestinationLongitude());
        existingTrip.setActive(trip.isActive());
        
        // Solo actualizar cupos totales si es diferente
        if (!existingTrip.getTotalSeats().equals(trip.getTotalSeats())) {
            int diferenciaCupos = trip.getTotalSeats() - existingTrip.getTotalSeats();
            existingTrip.setTotalSeats(trip.getTotalSeats());
            existingTrip.setAvailableSeats(existingTrip.getAvailableSeats() + diferenciaCupos);
        }
        
        tripRepository.save(existingTrip);
    }
    
    /**
     * Procesa evento de viaje cancelado
     */
    public void procesarTripCancelado(String tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Viaje no encontrado: " + tripId));
        
        trip.setActive(false);
        tripRepository.save(trip);
    }
}