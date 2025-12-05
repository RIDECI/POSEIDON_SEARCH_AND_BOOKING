package edu.dosw.rideci.unit.infrastructure;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import edu.dosw.rideci.application.event.BookingCreatedEvent;
import edu.dosw.rideci.infrastructure.adapters.RabbitMQEventPublisher;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
@DisplayName("RabbitMQEventPublisher Tests")
class RabbitMQEventPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitMQEventPublisher eventPublisher;

    private BookingCreatedEvent event;

    @BeforeEach
    void setUp() {
        event = BookingCreatedEvent.builder()
                .bookingId("BOOKING-001")
                .travelId("TRAVEL-001")
                .origin("Bogotá")
                .destination("Medellín")
                .passengerId(12345L)
                .reservedSeats(2)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should publish booking created event successfully")
    void shouldPublishEventSuccessfully() {
        // Given
        doNothing().when(rabbitTemplate).convertAndSend(isNull(), isNull(), any(BookingCreatedEvent.class));

        // When & Then
        assertDoesNotThrow(() -> eventPublisher.publishBookingCreatedEvent(event));
        verify(rabbitTemplate, times(1)).convertAndSend(isNull(), isNull(), any(BookingCreatedEvent.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when RabbitMQ fails")
    void shouldThrowExceptionWhenRabbitMQFails() {
        // Given
        doThrow(new RuntimeException("RabbitMQ connection failed"))
                .when(rabbitTemplate).convertAndSend(isNull(), isNull(), any(BookingCreatedEvent.class));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> eventPublisher.publishBookingCreatedEvent(event));
        
        assertTrue(exception.getMessage().contains("Failed to publish booking created event"));
        verify(rabbitTemplate, times(1)).convertAndSend(isNull(), isNull(), any(BookingCreatedEvent.class));
    }
}
