package edu.dosw.rideci.domain.ports.out;

/**
 * Puerto de salida para manejar eventos de pagos
 */
public interface PaymentEventsPort {
    
    /**
     * Procesa evento de pago exitoso
     */
    void handlePaymentSuccessful(String paymentId, String bookingId, String amount);
    
    /**
     * Procesa evento de pago fallido
     */
    void handlePaymentFailed(String paymentId, String bookingId, String reason);
    
    /**
     * Procesa evento de reembolso
     */
    void handleRefundProcessed(String paymentId, String bookingId, String amount);
}