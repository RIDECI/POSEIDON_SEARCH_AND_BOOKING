package edu.dosw.rideci.application.usecase;

import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.domain.model.Trip;
import edu.dosw.rideci.domain.ports.in.CancelBookingPort;
import edu.dosw.rideci.domain.ports.out.BookingRepositoryPort;
import edu.dosw.rideci.domain.ports.out.TripProjectionRepositoryPort;
import edu.dosw.rideci.domain.ports.out.EventPublisherPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class CancelBookingUseCase implements CancelBookingPort {
    
    private final BookingRepositoryPort bookingRepository;
    private final TripProjectionRepositoryPort tripRepository;
    private final EventPublisherPort eventPublisher;
    
    public CancelBookingUseCase(BookingRepositoryPort bookingRepository,
                                TripProjectionRepositoryPort tripRepository,
                                EventPublisherPort eventPublisher) {
        this.bookingRepository = bookingRepository;
        this.tripRepository = tripRepository;
        this.eventPublisher = eventPublisher;
    }
    
    @Override
    public void cancelBooking(String bookingId, String passengerId, String reason) {
        // Find the booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));
        
        // Validate that the passenger owns the booking
        if (!booking.getPassengerId().equals(passengerId)) {
            throw new IllegalArgumentException("You don't have permission to cancel this booking");
        }
        
        // Validate that it can be cancelled
        if (!booking.canBeCancelled()) {
            throw new IllegalStateException("The booking cannot be cancelled in its current state");
        }
        
        // Release seats in the trip
        Trip trip = tripRepository.findById(booking.getTripId())
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));
        
        trip.releaseSeats(booking.getReservedSeats());
        
        // Cancel the booking
        booking.cancel();
        
        // Save changes
        bookingRepository.save(booking);
        tripRepository.save(trip);
        
        // Publish events
        eventPublisher.publishBookingCancelled(bookingId, trip.getId(), 
                                              booking.getReservedSeats());
        eventPublisher.publishTripSeatsUpdate(trip.getId(), trip.getAvailableSeats());
    }
}