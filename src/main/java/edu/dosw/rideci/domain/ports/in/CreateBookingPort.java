package edu.dosw.rideci.domain.ports.in;

import edu.dosw.rideci.domain.model.Booking;

/**
 * Input port for creating bookings
 */
public interface CreateBookingPort {
    
    /**
     * Creates a new booking
     */
    Booking createBooking(String tripId, 
                         String passengerId, 
                         int reservedSeats,
                         String notes);
}