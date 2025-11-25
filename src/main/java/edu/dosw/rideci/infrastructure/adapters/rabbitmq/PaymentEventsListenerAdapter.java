package edu.dosw.rideci.infrastructure.adapters.rabbitmq;

import edu.dosw.rideci.domain.ports.in.ConfirmBookingPort;
import edu.dosw.rideci.domain.ports.in.CancelBookingPort;
import edu.dosw.rideci.domain.ports.out.PaymentEventsPort;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventsListenerAdapter implements PaymentEventsPort {
    
    private final ConfirmBookingPort confirmBookingPort;
    private final CancelBookingPort cancelBookingPort;
    private final ObjectMapper objectMapper;
    
    public PaymentEventsListenerAdapter(ConfirmBookingPort confirmBookingPort,
                                       CancelBookingPort cancelBookingPort,
                                       ObjectMapper objectMapper) {
        this.confirmBookingPort = confirmBookingPort;
        this.cancelBookingPort = cancelBookingPort;
        this.objectMapper = objectMapper;
    }
    
    @RabbitListener(queues = "${rabbitmq.queues.payment.successful}")
    public void handlePaymentSuccessfulEvent(String message) {
        try {
            JsonNode event = objectMapper.readTree(message);
            String paymentId = event.get("paymentId").asText();
            String bookingId = event.get("bookingId").asText();
            String amount = event.get("amount").asText();
            
            handlePaymentSuccessful(paymentId, bookingId, amount);
            
        } catch (Exception e) {
            System.err.println("Error processing PaymentSuccessful event: " + e.getMessage());
        }
    }
    
    @RabbitListener(queues = "${rabbitmq.queues.payment.failed}")
    public void handlePaymentFailedEvent(String message) {
        try {
            JsonNode event = objectMapper.readTree(message);
            String paymentId = event.get("paymentId").asText();
            String bookingId = event.get("bookingId").asText();
            String reason = event.get("reason").asText();
            
            handlePaymentFailed(paymentId, bookingId, reason);
            
        } catch (Exception e) {
            System.err.println("Error processing PaymentFailed event: " + e.getMessage());
        }
    }
    
    @RabbitListener(queues = "${rabbitmq.queues.payment.refund}")
    public void handleRefundProcessedEvent(String message) {
        try {
            JsonNode event = objectMapper.readTree(message);
            String paymentId = event.get("paymentId").asText();
            String bookingId = event.get("bookingId").asText();
            String amount = event.get("amount").asText();
            
            handleRefundProcessed(paymentId, bookingId, amount);
            
        } catch (Exception e) {
            System.err.println("Error processing RefundProcessed event: " + e.getMessage());
        }
    }
    
    @Override
    public void handlePaymentSuccessful(String paymentId, String bookingId, String amount) {
        try {
            // Confirm booking when payment is successful
            confirmBookingPort.confirmBooking(bookingId, paymentId);
            System.out.println("Booking confirmed after successful payment: " + bookingId);
            
        } catch (Exception e) {
            System.err.println("Error confirming booking after successful payment: " + e.getMessage());
        }
    }
    
    @Override
    public void handlePaymentFailed(String paymentId, String bookingId, String reason) {
        try {
            // Here you could implement logic to handle failed payments
            // For example, automatically cancel the booking or notify the user
            cancelBookingPort.cancelBooking(bookingId, "system", "Payment failed: " + reason);
            System.out.println("Booking cancelled due to payment failure: " + bookingId + ". Reason: " + reason);
            
        } catch (Exception e) {
            System.err.println("Error handling payment failure: " + e.getMessage());
        }
    }
    
    @Override
    public void handleRefundProcessed(String paymentId, String bookingId, String amount) {
        try {
            // Handle processed refund
            System.out.println("Refund processed for booking " + bookingId + ". Amount: " + amount);
            
        } catch (Exception e) {
            System.err.println("Error handling refund: " + e.getMessage());
        }
    }
}