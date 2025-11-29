package edu.dosw.rideci.application.ports.out;

/**
 * Puerto de salida para publicar eventos
 */
public interface EventPublisherPort {
    
    /**
     * Publica evento de reserva creada
     */
    void publishBookingCreated(String bookingId, String tripId, String passengerId, 
                              int cuposReservados, String totalAmount);
    
    /**
     * Publica evento de reserva confirmada
     */
    void publishBookingConfirmed(String bookingId, String paymentId);
    
    /**
     * Publica evento de reserva cancelada
     */
    void publishBookingCancelled(String bookingId, String tripId, int cuposLiberados);
    
    /**
     * Publica evento de solicitud de pago
     */
    void publishPaymentRequested(String bookingId, String passengerId, String totalAmount);
    
    /**
     * Publica evento de actualización de cupos del viaje
     */
    void publishTripSeatsUpdate(String tripId, int cuposDisponibles);
}