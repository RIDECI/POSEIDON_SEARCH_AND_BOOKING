package edu.dosw.rideci.domain.ports.out;

import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.domain.model.enums.BookingStatus;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para el repositorio de reservas
 */
public interface BookingRepositoryPort {
    
    Optional<Booking> findById(String id);
    
    List<Booking> findByPassengerId(String passengerId);
    
    List<Booking> findByTripId(String tripId);
    
    List<Booking> findByTripIdAndStatus(String tripId, BookingStatus status);
    
    List<Booking> findByPassengerIdAndStatus(String passengerId, BookingStatus status);
    
    Booking save(Booking booking);
    
    void deleteById(String id);
    
    boolean existsById(String id);
    
    int countActiveBookingsByTrip(String tripId);
}