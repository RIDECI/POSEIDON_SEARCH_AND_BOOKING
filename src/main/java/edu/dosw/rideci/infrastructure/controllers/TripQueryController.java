package edu.dosw.rideci.infrastructure.controllers;

import edu.dosw.rideci.domain.model.Trip;
import edu.dosw.rideci.domain.model.enums.TravelType;
import edu.dosw.rideci.application.ports.in.SearchTripsPort;
import edu.dosw.rideci.application.ports.in.SearchNearbyTripsPort;
import edu.dosw.rideci.application.ports.in.GetTripDetailsPort;
import edu.dosw.rideci.infrastructure.dto.Response.TripResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "*")
public class TripQueryController {
    
    private final SearchTripsPort searchTripsPort;
    private final SearchNearbyTripsPort searchNearbyTripsPort;
    private final GetTripDetailsPort getTripDetailsPort;
    
    public TripQueryController(SearchTripsPort searchTripsPort,
                              SearchNearbyTripsPort searchNearbyTripsPort,
                              GetTripDetailsPort getTripDetailsPort) {
        this.searchTripsPort = searchTripsPort;
        this.searchNearbyTripsPort = searchNearbyTripsPort;
        this.getTripDetailsPort = getTripDetailsPort;
    }
    
    /**
     * Buscar viajes con filtros
     */
    @GetMapping("/search")
    public ResponseEntity<List<TripResponse>> buscarViajes(
            @RequestParam(required = false) String origen,
            @RequestParam(required = false) String destino,
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta,
            @RequestParam(required = false) String travelType,
            @RequestParam(required = false) Integer cuposMinimos,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Optional<LocalDateTime> fechaDesdeOpt = Optional.ofNullable(fechaDesde)
                .map(LocalDateTime::parse);
        Optional<LocalDateTime> fechaHastaOpt = Optional.ofNullable(fechaHasta)
                .map(LocalDateTime::parse);
        Optional<TravelType> travelTypeOpt = Optional.ofNullable(travelType)
                .map(TravelType::valueOf);
        
        List<Trip> trips;
        
        if (page > 0 || size != 10) {
            trips = searchTripsPort.searchTripsWithPagination(
                    Optional.ofNullable(origen),
                    Optional.ofNullable(destino),
                    fechaDesdeOpt,
                    fechaHastaOpt,
                    travelTypeOpt,
                    Optional.ofNullable(cuposMinimos),
                    page, size
            );
        } else {
            trips = searchTripsPort.searchTrips(
                    Optional.ofNullable(origen),
                    Optional.ofNullable(destino),
                    fechaDesdeOpt,
                    fechaHastaOpt,
                    travelTypeOpt,
                    Optional.ofNullable(cuposMinimos)
            );
        }
        
        List<TripResponse> response = trips.stream()
                .map(this::mapToTripResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Buscar viajes cercanos por ubicación
     */
    @GetMapping("/nearby")
    public ResponseEntity<List<TripResponse>> buscarViajesCercanos(
            @RequestParam double latitud,
            @RequestParam double longitud,
            @RequestParam(defaultValue = "10.0") double radioKm,
            @RequestParam(required = false) Integer cuposMinimos,
            @RequestParam(required = false) String travelType) {
        
        List<Trip> trips;
        
        if (cuposMinimos != null || travelType != null) {
            trips = searchNearbyTripsPort.searchNearbyTripsWithFilters(
                    latitud, longitud, radioKm, cuposMinimos, travelType
            );
        } else {
            trips = searchNearbyTripsPort.searchNearbyTrips(
                    latitud, longitud, radioKm
            );
        }
        
        List<TripResponse> response = trips.stream()
                .map(this::mapToTripResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener detalles de un viaje por ID
     */
    @GetMapping("/{tripId}")
    public ResponseEntity<TripResponse> obtenerDetallesViaje(@PathVariable String tripId) {
        Optional<Trip> tripOpt = getTripDetailsPort.getTripDetails(tripId);
        
        if (tripOpt.isPresent()) {
            TripResponse response = mapToTripResponse(tripOpt.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Mapea Trip a TripResponse
     */
    private TripResponse mapToTripResponse(Trip trip) {
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
}