package edu.dosw.rideci.application.service;

import edu.dosw.rideci.infrastructure.persistence.entity.BookingDocument;
import edu.dosw.rideci.infrastructure.persistence.Repository.BookingRepository;
import edu.dosw.rideci.exceptions.InsufficientSeatsException;
import edu.dosw.rideci.exceptions.TripNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    
    private final BookingRepository bookingRepository;
    private final TravelService travelService;
    
    public BookingService(BookingRepository bookingRepository, TravelService travelService) {
        this.bookingRepository = bookingRepository;
        this.travelService = travelService;
    }
    
    /**
     * Crear una nueva reserva simple
     */
    @Transactional
    public BookingDocument createSimpleBooking(String tripId, String passengerId, int seatNumber, double totalPrice) {
        try {
            logger.info("Creando reserva simple - Viaje: {}, Pasajero: {}, Asiento: {}", 
                       tripId, passengerId, seatNumber);
            
            // Verificar que el viaje existe
            var tripInfo = travelService.getTripInfo(tripId);
            if (tripInfo.isEmpty()) {
                throw new TripNotFoundException("Viaje no encontrado: " + tripId);
            }
            
            // Verificar asientos disponibles
            if (tripInfo.get().getAvailableSlots() <= 0) {
                throw new InsufficientSeatsException("No hay asientos disponibles");
            }
            
            // Actualizar asientos disponibles
            boolean updated = travelService.updateAvailableSeats(tripId, 1);
            if (!updated) {
                throw new InsufficientSeatsException("No se pudieron reservar los asientos");
            }
            
            // Crear la reserva usando la entidad existente
            BookingDocument bookingDocument = new BookingDocument();
            bookingDocument.setId(UUID.randomUUID().toString());
            bookingDocument.setRideId(tripId);
            bookingDocument.setPassengerId(passengerId);
            bookingDocument.setSeatNumber(seatNumber);
            bookingDocument.setTotalPrice(totalPrice);
            bookingDocument.setStatus("PENDING");
            bookingDocument.setCreatedAt(LocalDateTime.now());
            
            BookingDocument savedBooking = bookingRepository.save(bookingDocument);
            
            logger.info("Reserva creada exitosamente: {}", savedBooking.getId());
            
            return savedBooking;
            
        } catch (TripNotFoundException | InsufficientSeatsException e) {
            logger.warn("Error creando reserva: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error inesperado creando reserva: {}", e.getMessage(), e);
            throw new RuntimeException("Error interno creando la reserva", e);
        }
    }
    
    /**
     * Obtener una reserva por ID
     */
    public Optional<BookingDocument> getBookingById(String bookingId) {
        return bookingRepository.findById(bookingId);
    }
    
    /**
     * Actualizar estado de una reserva
     */
    @Transactional
    public void updateBookingStatus(String bookingId, String newStatus) {
        Optional<BookingDocument> optionalBooking = bookingRepository.findById(bookingId);
        
        if (optionalBooking.isPresent()) {
            BookingDocument booking = optionalBooking.get();
            booking.setStatus(newStatus);
            bookingRepository.save(booking);
            
            logger.info("Estado de reserva {} actualizado a: {}", bookingId, newStatus);
        } else {
            logger.warn("No se encontró la reserva: {}", bookingId);
        }
    }
}