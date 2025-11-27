package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.event.TripCreatedEvent;
import edu.dosw.rideci.application.event.TripUpdateEvent;
import edu.dosw.rideci.application.event.TripCancelledEvent;
import edu.dosw.rideci.infrastructure.persistence.entity.TravelDocument;
import edu.dosw.rideci.infrastructure.persistence.Repository.TravelRepository;
import edu.dosw.rideci.domain.model.enums.TravelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class TravelService {

    private static final Logger logger = LoggerFactory.getLogger(TravelService.class);
    
    private final TravelRepository travelRepository;
    
    public TravelService(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }
    
    /**
     * Escucha eventos de creación de viajes desde RabbitMQ
     */
    @RabbitListener(queues = "#{tripCreatedQueue.name}")
    public void handleTripCreated(TripCreatedEvent event) {
        try {
            logger.info("Recibido evento de viaje creado: {}", event.getTripId());
            
            TravelDocument travelDocument = new TravelDocument();
            travelDocument.setRideId(event.getTripId());
            travelDocument.setDriverId(Long.parseLong(event.getDriverId()));
            travelDocument.setAvailableSlots(event.getTotalSeats());
            travelDocument.setStatus("ACTIVE");
            travelDocument.setTravelType(TravelType.valueOf(event.getTravelType()));
            travelDocument.setEstimatedCost(event.getPrice().doubleValue());
            travelDocument.setDepartureDateAndTime(event.getDepartureDateTime());
            travelDocument.setConditions(event.getDescription());
            travelDocument.setOriginLat(event.getOriginLatitude());
            travelDocument.setOriginLng(event.getOriginLongitude());
            travelDocument.setDestinyLat(event.getDestinationLatitude());
            travelDocument.setDestinyLng(event.getDestinationLongitude());
            travelDocument.setPassengersId(new ArrayList<>());
            
            travelRepository.save(travelDocument);
            logger.info("Viaje guardado exitosamente: {}", event.getTripId());
            
        } catch (Exception e) {
            logger.error("Error procesando evento de viaje creado: {}", e.getMessage(), e);
            throw e; // Re-lanzar para manejo de dead letter queue
        }
    }
    
    /**
     * Escucha eventos de actualización de viajes desde RabbitMQ
     */
    @RabbitListener(queues = "#{tripUpdatedQueue.name}")
    public void handleTripUpdated(TripUpdateEvent event) {
        try {
            logger.info("Recibido evento de actualización de viaje: {}", event.getTripId());
            
            Optional<TravelDocument> optionalTravel = travelRepository.findByRideId(event.getTripId());
            
            if (optionalTravel.isPresent()) {
                TravelDocument travel = optionalTravel.get();
                
                // Actualizar campos si están presentes en el evento
                if (event.getTotalSeats() != null) {
                    travel.setAvailableSlots(event.getTotalSeats());
                }
                
                // Actualizar el estado basado en el campo active
                travel.setStatus(event.isActive() ? "ACTIVE" : "INACTIVE");
                
                if (event.getPrice() != null) {
                    travel.setEstimatedCost(event.getPrice().doubleValue());
                }
                if (event.getDepartureDateTime() != null) {
                    travel.setDepartureDateAndTime(event.getDepartureDateTime());
                }
                if (event.getDescription() != null) {
                    travel.setConditions(event.getDescription());
                }
                if (event.getOriginLatitude() != null) {
                    travel.setOriginLat(event.getOriginLatitude());
                }
                if (event.getOriginLongitude() != null) {
                    travel.setOriginLng(event.getOriginLongitude());
                }
                if (event.getDestinationLatitude() != null) {
                    travel.setDestinyLat(event.getDestinationLatitude());
                }
                if (event.getDestinationLongitude() != null) {
                    travel.setDestinyLng(event.getDestinationLongitude());
                }
                
                travelRepository.save(travel);
                logger.info("Viaje actualizado exitosamente: {}", event.getTripId());
                
            } else {
                logger.warn("No se encontró el viaje para actualizar: {}", event.getTripId());
            }
            
        } catch (Exception e) {
            logger.error("Error procesando evento de actualización de viaje: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Escucha eventos de cancelación de viajes desde RabbitMQ
     */
    @RabbitListener(queues = "#{tripCancelledQueue.name}")
    public void handleTripCancelled(TripCancelledEvent event) {
        try {
            logger.info("Recibido evento de cancelación de viaje: {}", event.getTripId());
            
            Optional<TravelDocument> optionalTravel = travelRepository.findByRideId(event.getTripId());
            
            if (optionalTravel.isPresent()) {
                TravelDocument travel = optionalTravel.get();
                travel.setStatus("CANCELLED");
                
                travelRepository.save(travel);
                logger.info("Viaje cancelado exitosamente: {}", event.getTripId());
                
            } else {
                logger.warn("No se encontró el viaje para cancelar: {}", event.getTripId());
            }
            
        } catch (Exception e) {
            logger.error("Error procesando evento de cancelación de viaje: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Actualizar asientos disponibles cuando se hace una reserva
     */
    @Transactional
    public boolean updateAvailableSeats(String tripId, int seatsToReserve) {
        try {
            Optional<TravelDocument> optionalTravel = travelRepository.findByRideId(tripId);
            
            if (optionalTravel.isPresent()) {
                TravelDocument travel = optionalTravel.get();
                
                if (travel.getAvailableSlots() >= seatsToReserve) {
                    travel.setAvailableSlots(travel.getAvailableSlots() - seatsToReserve);
                    travelRepository.save(travel);
                    
                    logger.info("Asientos actualizados para viaje {}: {} asientos reservados", 
                              tripId, seatsToReserve);
                    return true;
                } else {
                    logger.warn("No hay suficientes asientos disponibles en viaje {}: disponibles={}, solicitados={}", 
                              tripId, travel.getAvailableSlots(), seatsToReserve);
                    return false;
                }
            } else {
                logger.warn("No se encontró el viaje: {}", tripId);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error actualizando asientos del viaje {}: {}", tripId, e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Obtener información de un viaje
     */
    public Optional<TravelDocument> getTripInfo(String tripId) {
        return travelRepository.findByRideId(tripId);
    }
}