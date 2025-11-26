package edu.dosw.rideci.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    // Exchanges
    @Value("${rabbitmq.exchanges.trip:trip.exchange}")
    private String tripExchange;
    
    @Value("${rabbitmq.exchanges.booking:booking.exchange}")
    private String bookingExchange;
    
    @Value("${rabbitmq.exchanges.payment:payment.exchange}")
    private String paymentExchange;
    
    // Queues para eventos de Trip
    @Value("${rabbitmq.queues.trip.created:trip.created.queue}")
    private String tripCreatedQueue;
    
    @Value("${rabbitmq.queues.trip.updated:trip.updated.queue}")
    private String tripUpdatedQueue;
    
    @Value("${rabbitmq.queues.trip.cancelled:trip.cancelled.queue}")
    private String tripCancelledQueue;
    
    // Queues para eventos de Payment
    @Value("${rabbitmq.queues.payment.successful:payment.successful.queue}")
    private String paymentSuccessfulQueue;
    
    @Value("${rabbitmq.queues.payment.failed:payment.failed.queue}")
    private String paymentFailedQueue;
    
    @Value("${rabbitmq.queues.payment.refund:payment.refund.queue}")
    private String paymentRefundQueue;
    
    // Exchanges
    @Bean
    public TopicExchange tripExchange() {
        return new TopicExchange(tripExchange);
    }
    
    @Bean
    public TopicExchange bookingExchange() {
        return new TopicExchange(bookingExchange);
    }
    
    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(paymentExchange);
    }
    
    // Queues para Trip Events
    @Bean
    public Queue tripCreatedQueue() {
        return QueueBuilder.durable(tripCreatedQueue).build();
    }
    
    @Bean
    public Queue tripUpdatedQueue() {
        return QueueBuilder.durable(tripUpdatedQueue).build();
    }
    
    @Bean
    public Queue tripCancelledQueue() {
        return QueueBuilder.durable(tripCancelledQueue).build();
    }
    
    // Queues para Payment Events
    @Bean
    public Queue paymentSuccessfulQueue() {
        return QueueBuilder.durable(paymentSuccessfulQueue).build();
    }
    
    @Bean
    public Queue paymentFailedQueue() {
        return QueueBuilder.durable(paymentFailedQueue).build();
    }
    
    @Bean
    public Queue paymentRefundQueue() {
        return QueueBuilder.durable(paymentRefundQueue).build();
    }
    
    // Bindings para Trip Events
    @Bean
    public Binding tripCreatedBinding() {
        return BindingBuilder.bind(tripCreatedQueue())
                .to(tripExchange())
                .with("trip.created");
    }
    
    @Bean
    public Binding tripUpdatedBinding() {
        return BindingBuilder.bind(tripUpdatedQueue())
                .to(tripExchange())
                .with("trip.updated");
    }
    
    @Bean
    public Binding tripCancelledBinding() {
        return BindingBuilder.bind(tripCancelledQueue())
                .to(tripExchange())
                .with("trip.cancelled");
    }
    
    // Bindings para Payment Events
    @Bean
    public Binding paymentSuccessfulBinding() {
        return BindingBuilder.bind(paymentSuccessfulQueue())
                .to(paymentExchange())
                .with("payment.successful");
    }
    
    @Bean
    public Binding paymentFailedBinding() {
        return BindingBuilder.bind(paymentFailedQueue())
                .to(paymentExchange())
                .with("payment.failed");
    }
    
    @Bean
    public Binding paymentRefundBinding() {
        return BindingBuilder.bind(paymentRefundQueue())
                .to(paymentExchange())
                .with("payment.refund");
    }
    
    // RabbitTemplate con JSON converter
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}