package edu.dosw.rideci.infrastructure.adapters.rabbitmq;

import edu.dosw.rideci.domain.ports.out.EventPublisherPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class BookingEventsPublisherAdapter implements EventPublisherPort {
    
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    
    @Value("${rabbitmq.exchanges.booking}")
    private String bookingExchange;
    
    @Value("${rabbitmq.exchanges.payment}")
    private String paymentExchange;
    
    @Value("${rabbitmq.exchanges.trip}")
    private String tripExchange;
    
    public BookingEventsPublisherAdapter(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }
    
    @Override
    public void publishBookingCreated(String bookingId, String tripId, String passengerId,
                                     int cuposReservados, String totalAmount) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("bookingId", bookingId);
            event.put("tripId", tripId);
            event.put("passengerId", passengerId);
            event.put("cuposReservados", cuposReservados);
            event.put("totalAmount", totalAmount);
            event.put("timestamp", LocalDateTime.now());
            
            String message = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(bookingExchange, "booking.created", message);
            
        } catch (Exception e) {
            System.err.println("Error publicando evento BookingCreated: " + e.getMessage());
        }
    }
    
    @Override
    public void publishBookingConfirmed(String bookingId, String paymentId) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("bookingId", bookingId);
            event.put("paymentId", paymentId);
            event.put("timestamp", LocalDateTime.now());
            
            String message = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(bookingExchange, "booking.confirmed", message);
            
        } catch (Exception e) {
            System.err.println("Error publicando evento BookingConfirmed: " + e.getMessage());
        }
    }
    
    @Override
    public void publishBookingCancelled(String bookingId, String tripId, int cuposLiberados) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("bookingId", bookingId);
            event.put("tripId", tripId);
            event.put("cuposLiberados", cuposLiberados);
            event.put("timestamp", LocalDateTime.now());
            
            String message = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(bookingExchange, "booking.cancelled", message);
            
        } catch (Exception e) {
            System.err.println("Error publicando evento BookingCancelled: " + e.getMessage());
        }
    }
    
    @Override
    public void publishPaymentRequested(String bookingId, String passengerId, String totalAmount) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("bookingId", bookingId);
            event.put("passengerId", passengerId);
            event.put("totalAmount", totalAmount);
            event.put("timestamp", LocalDateTime.now());
            
            String message = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(paymentExchange, "payment.requested", message);
            
        } catch (Exception e) {
            System.err.println("Error publicando evento PaymentRequested: " + e.getMessage());
        }
    }
    
    @Override
    public void publishTripSeatsUpdate(String tripId, int cuposDisponibles) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("tripId", tripId);
            event.put("cuposDisponibles", cuposDisponibles);
            event.put("timestamp", LocalDateTime.now());
            
            String message = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(tripExchange, "trip.seats.updated", message);
            
        } catch (Exception e) {
            System.err.println("Error publicando evento TripSeatsUpdate: " + e.getMessage());
        }
    }
}