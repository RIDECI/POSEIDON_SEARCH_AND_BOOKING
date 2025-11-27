package edu.dosw.rideci.application.ports.in;

/**
 * Input port for confirming bookings
 */
public interface ConfirmBookingPort {
    
    /**
     * Confirms a booking after successful payment
     */
    void confirmBooking(String bookingId, String paymentId);
}