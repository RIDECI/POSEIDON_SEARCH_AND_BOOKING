package edu.dosw.rideci.application.usecase;

import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.domain.model.Trip;
import edu.dosw.rideci.domain.model.enums.BookingStatus;
import edu.dosw.rideci.domain.ports.in.CreateBookingPort;
import edu.dosw.rideci.domain.ports.out.BookingRepositoryPort;
import edu.dosw.rideci.domain.ports.out.TripProjectionRepositoryPort;
import edu.dosw.rideci.domain.ports.out.EventPublisherPort;
import edu.dosw.rideci.exceptions.TripNotFoundException;
import edu.dosw.rideci.exceptions.InsufficientSeatsException;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CreateBookingUseCase implements CreateBookingPort {
    
    private final BookingRepositoryPort bookingRepository;
    private final TripProjectionRepositoryPort tripRepository;
    private final EventPublisherPort eventPublisher;
    
    public CreateBookingUseCase(BookingRepositoryPort bookingRepository,
                               TripProjectionRepositoryPort tripRepository,
                               EventPublisherPort eventPublisher) {
        this.bookingRepository = bookingRepository;
        this.tripRepository = tripRepository;
        this.eventPublisher = eventPublisher;
    }
    
    @Override
    public Booking createBooking(String tripId, String passengerId, 
                               int reservedSeats, String notes) {
        // Buscar trip
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripNotFoundException(tripId));
        
        // Validar cupos disponibles
        if (!trip.hasAvailableSeats(reservedSeats)) {
            throw new InsufficientSeatsException("Not enough available seats");
        }
        
        // Calcular monto total
        BigDecimal totalAmount = trip.getPrice().multiply(BigDecimal.valueOf(reservedSeats));
        
        // Crear reserva
        Booking booking = new Booking();
        booking.setId(UUID.randomUUID().toString());
        booking.setTripId(tripId);
        booking.setPassengerId(passengerId);
        booking.setReservedSeats(reservedSeats);
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.PENDING);
        booking.setNotes(notes);
        booking.setBookingDate(LocalDateTime.now());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());
        
        // Reservar cupos en el trip
        trip.reserveSeats(reservedSeats);
        
        // Guardar cambios
        bookingRepository.save(booking);
        tripRepository.save(trip);
        
        // Publicar eventos
        eventPublisher.publishBookingCreated(booking.getId(), tripId, passengerId, 
                                           reservedSeats, totalAmount.toString());
        eventPublisher.publishTripSeatsUpdate(tripId, trip.getAvailableSeats());
        
        return booking;
    }
}