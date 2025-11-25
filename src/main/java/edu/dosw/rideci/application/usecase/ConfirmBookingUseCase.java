package edu.dosw.rideci.application.usecase;

import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.domain.ports.in.ConfirmBookingPort;
import edu.dosw.rideci.domain.ports.out.BookingRepositoryPort;
import edu.dosw.rideci.domain.ports.out.EventPublisherPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ConfirmBookingUseCase implements ConfirmBookingPort {
    
    private final BookingRepositoryPort bookingRepository;
    private final EventPublisherPort eventPublisher;
    
    public ConfirmBookingUseCase(BookingRepositoryPort bookingRepository,
                                EventPublisherPort eventPublisher) {
        this.bookingRepository = bookingRepository;
        this.eventPublisher = eventPublisher;
    }
    
    @Override
    public void confirmBooking(String bookingId, String paymentId) {
        // Find the booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));
        
        // Confirm the booking
        booking.confirm(paymentId);
        
        // Save changes
        bookingRepository.save(booking);
        
        // Publish event
        eventPublisher.publishBookingConfirmed(bookingId, paymentId);
    }
}