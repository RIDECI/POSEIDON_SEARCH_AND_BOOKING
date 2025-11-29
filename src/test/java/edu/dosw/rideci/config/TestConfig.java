package edu.dosw.rideci.config;

import edu.dosw.rideci.application.ports.out.BookingRepositoryPort;
import edu.dosw.rideci.application.ports.out.EventPublisherPort;
import edu.dosw.rideci.application.ports.out.TripProjectionRepositoryPort;
import edu.dosw.rideci.application.ports.out.PaymentEventsPort;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public BookingRepositoryPort mockBookingRepository() {
        return mock(BookingRepositoryPort.class);
    }

    @Bean
    @Primary
    public TripProjectionRepositoryPort mockTripRepository() {
        return mock(TripProjectionRepositoryPort.class);
    }

    @Bean
    @Primary
    public EventPublisherPort mockEventPublisher() {
        return mock(EventPublisherPort.class);
    }

    @Bean
    @Primary
    public PaymentEventsPort mockPaymentEventsPort() {
        return mock(PaymentEventsPort.class);
    }
}