package edu.dosw.rideci.infrastructure.adapters;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import edu.dosw.rideci.application.event.BookingCreatedEvent;
import edu.dosw.rideci.application.ports.out.EventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQEventPublisher implements EventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchanges.booking:booking.exchange}")
    private String bookingExchange;

    @Value("${rabbitmq.routing.booking.created:booking.created}")
    private String bookingCreatedRoutingKey;

    @Override
    public void publishBookingCreatedEvent(BookingCreatedEvent event) {
        try {
            log.info("Publishing booking created event: bookingId={}, travelId={}, origin={}, destination={}", 
                    event.getBookingId(), event.getTravelId(), event.getOrigin(), event.getDestination());
            
            rabbitTemplate.convertAndSend(bookingExchange, bookingCreatedRoutingKey, event);
            
            log.info("Booking created event published successfully");
        } catch (Exception e) {
            log.error("Error publishing booking created event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to publish booking created event", e);
        }
    }

}
