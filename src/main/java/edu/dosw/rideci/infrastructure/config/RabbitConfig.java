package edu.dosw.rideci.infrastructure.config;

// FORCE RECOMPILE
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    
    // ============ EXCHANGES ============
    
    @Bean
    public TopicExchange bookingExchange() {
        return new TopicExchange("booking.exchange", true, false);
    }
    
    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange("payment.exchange", true, false);
    }
    
    // ============ QUEUES QUE ESTE MICROSERVICIO PUBLICA ============
    
    @Bean
    public Queue bookingCreatedQueue() {
        return new Queue("booking.created.queue", true);
    }
    
    @Bean
    public Queue bookingConfirmedQueue() {
        return new Queue("booking.confirmed.queue", true);
    }
    
    @Bean
    public Queue bookingCancelledQueue() {
        return new Queue("booking.cancelled.queue", true);
    }
    
    // ============ QUEUES QUE ESTE MICROSERVICIO ESCUCHA ============
    
    @Bean
    public Queue paymentConfirmedQueue() {
        return new Queue("payment.confirmed.queue", true);
    }
    
    @Bean
    public Queue paymentFailedQueue() {
        return new Queue("payment.failed.queue", true);
    }
    
    @Bean
    public Queue travelUpdatedQueue() {
        return new Queue("travel.updated.queue", true);
    }
    
    // ============ BINDINGS - Eventos que PUBLICAMOS ============
    
    @Bean
    public Binding bindingBookingCreated() {
        return BindingBuilder
            .bind(bookingCreatedQueue())
            .to(bookingExchange())
            .with("booking.created");
    }
    
    @Bean
    public Binding bindingBookingConfirmed() {
        return BindingBuilder
            .bind(bookingConfirmedQueue())
            .to(bookingExchange())
            .with("booking.confirmed");
    }
    
    @Bean
    public Binding bindingBookingCancelled() {
        return BindingBuilder
            .bind(bookingCancelledQueue())
            .to(bookingExchange())
            .with("booking.cancelled");
    }
    
    // ============ BINDINGS - Eventos que ESCUCHAMOS ============
    
    @Bean
    public Binding bindingPaymentConfirmed() {
        return BindingBuilder
            .bind(paymentConfirmedQueue())
            .to(paymentExchange())
            .with("payment.confirmed");
    }
    
    @Bean
    public Binding bindingPaymentFailed() {
        return BindingBuilder
            .bind(paymentFailedQueue())
            .to(paymentExchange())
            .with("payment.failed");
    }
    
    @Bean
    public Binding bindingTravelUpdated() {
        return BindingBuilder
            .bind(travelUpdatedQueue())
            .to(new TopicExchange("travel.exchange", true, false))
            .with("travel.updated");
    }
    
    // ============ MESSAGE CONVERTER ============
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}