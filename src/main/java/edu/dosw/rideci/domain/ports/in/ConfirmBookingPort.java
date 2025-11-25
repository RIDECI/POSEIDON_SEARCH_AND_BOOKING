package edu.dosw.rideci.domain.ports.in;

/**
 * Input port for confirming bookings
 */
public interface ConfirmBookingPort {
    
    /**
     * Confirms a booking after successful payment
     */
    void confirmBooking(String bookingId, String paymentId);
}