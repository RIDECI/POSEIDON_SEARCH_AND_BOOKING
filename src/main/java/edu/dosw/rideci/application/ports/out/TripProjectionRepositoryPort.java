package edu.dosw.rideci.application.ports.out;

import edu.dosw.rideci.domain.model.Trip;
import edu.dosw.rideci.domain.model.enums.TravelType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para el repositorio de proyección de viajes
 */
public interface TripProjectionRepositoryPort {
    
    Optional<Trip> findById(String id);
    
    List<Trip> findByFilters(Optional<String> origen,
                            Optional<String> destino,
                            Optional<LocalDateTime> fechaDesde,
                            Optional<LocalDateTime> fechaHasta,
                            Optional<TravelType> travelType,
                            Optional<Integer> cuposMinimos);
    
    List<Trip> findByFiltersWithPagination(Optional<String> origen,
                                          Optional<String> destino,
                                          Optional<LocalDateTime> fechaDesde,
                                          Optional<LocalDateTime> fechaHasta,
                                          Optional<TravelType> travelType,
                                          Optional<Integer> cuposMinimos,
                                          int page,
                                          int size);
    
    List<Trip> findNearbyTrips(double latitud, double longitud, double radioKm);
    
    List<Trip> findNearbyTripsWithFilters(double latitud, double longitud, double radioKm,
                                         Integer cuposMinimos, String travelType);
    
    Trip save(Trip trip);
    
    void deleteById(String id);
    
    boolean existsById(String id);
}