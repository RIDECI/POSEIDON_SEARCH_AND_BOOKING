package edu.dosw.rideci.infrastructure.adapters.rabbitmq;

import edu.dosw.rideci.application.usecase.ProcesarEventosDeTripUseCase;
import edu.dosw.rideci.domain.model.Trip;
import edu.dosw.rideci.domain.model.enums.TravelType;
import edu.dosw.rideci.infrastructure.dto.event.TripCreatedEvent;
import edu.dosw.rideci.infrastructure.dto.event.TripUpdateEvent;
import edu.dosw.rideci.infrastructure.dto.event.TripCancelledEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class TripEventsListenerAdapter {
    
    private final ProcesarEventosDeTripUseCase procesarEventosDeTripUseCase;
    private final ObjectMapper objectMapper;
    
    public TripEventsListenerAdapter(ProcesarEventosDeTripUseCase procesarEventosDeTripUseCase,
                                    ObjectMapper objectMapper) {
        this.procesarEventosDeTripUseCase = procesarEventosDeTripUseCase;
        this.objectMapper = objectMapper;
    }
    
    @RabbitListener(queues = "${rabbitmq.queues.trip.created}")
    public void handleTripCreated(String message) {
        try {
            TripCreatedEvent event = objectMapper.readValue(message, TripCreatedEvent.class);
            
            // Mapear evento a entidad Trip
            Trip trip = new Trip(
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
            
            procesarEventosDeTripUseCase.procesarTripCreado(trip);
            
        } catch (Exception e) {
            // Log del error
            System.err.println("Error procesando evento TripCreated: " + e.getMessage());
        }
    }
    
    @RabbitListener(queues = "${rabbitmq.queues.trip.updated}")
    public void handleTripUpdated(String message) {
        try {
            TripUpdateEvent event = objectMapper.readValue(message, TripUpdateEvent.class);
            
            // Mapear evento a entidad Trip
            Trip trip = new Trip(
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
            
            procesarEventosDeTripUseCase.procesarTripActualizado(trip);
            
        } catch (Exception e) {
            // Log del error
            System.err.println("Error procesando evento TripUpdated: " + e.getMessage());
        }
    }
    
    @RabbitListener(queues = "${rabbitmq.queues.trip.cancelled}")
    public void handleTripCancelled(String message) {
        try {
            TripCancelledEvent event = objectMapper.readValue(message, TripCancelledEvent.class);
            
            procesarEventosDeTripUseCase.procesarTripCancelado(event.getTripId());
            
        } catch (Exception e) {
            // Log del error
            System.err.println("Error procesando evento TripCancelled: " + e.getMessage());
        }
    }
}