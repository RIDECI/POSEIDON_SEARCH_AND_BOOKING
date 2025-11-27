package edu.dosw.rideci.application.ports.in;

/**
 * Input port for cancelling bookings
 */
public interface CancelBookingPort {
    
    /**
     * Cancels an existing booking
     */
    void cancelBooking(String bookingId, String passengerId, String reason);
}