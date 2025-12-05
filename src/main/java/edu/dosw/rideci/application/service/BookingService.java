package edu.dosw.rideci.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.dto.CreateBookingCommand;
import edu.dosw.rideci.application.event.BookingCreatedEvent;
import edu.dosw.rideci.application.ports.in.CancelBookingUseCase;
import edu.dosw.rideci.application.ports.in.CompleteBookingUseCase;
import edu.dosw.rideci.application.ports.in.ConfirmBookingUseCase;
import edu.dosw.rideci.application.ports.in.CreateBookingUseCase;
import edu.dosw.rideci.application.ports.in.GetBookingByIdUseCase;
import edu.dosw.rideci.application.ports.in.GetBookingsByPassengerIdUseCase;
import edu.dosw.rideci.application.ports.in.GetBookingsByTravelIdUseCase;
import edu.dosw.rideci.application.ports.in.UpdateBookingSeatsUseCase;
import edu.dosw.rideci.application.ports.in.ValidateBookingAvailabilityUseCase;
import edu.dosw.rideci.application.ports.out.BookingRepositoryPort;
import edu.dosw.rideci.application.ports.out.EventPublisherPort;
import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.domain.model.enums.BookingStatus;
import edu.dosw.rideci.exceptions.BookingNotFoundException;
import edu.dosw.rideci.exceptions.InsufficientSeatsException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService implements 
        CreateBookingUseCase, 
        CancelBookingUseCase, 
        ConfirmBookingUseCase,
        GetBookingByIdUseCase,
        GetBookingsByPassengerIdUseCase,
        GetBookingsByTravelIdUseCase,
        UpdateBookingSeatsUseCase,
        CompleteBookingUseCase,
        ValidateBookingAvailabilityUseCase {

    private final BookingRepositoryPort bookingRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    @Override
    public Booking createBooking(CreateBookingCommand command) {

        Booking booking = Booking.builder()
                .travelId(command.getTravelId())
                .passengerId(command.getPassengerId())
                .origin(command.getOrigin())
                .destination(command.getDestination())
                .reservedSeats(command.getReservedSeats())
                .totalAmount(command.getTotalAmount())
                .status(command.getStatus() != null ? command.getStatus() : BookingStatus.PENDING)
                .notes(command.getNotes())
                .bookingDate(command.getBookingDate())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Booking createdBooking = bookingRepositoryPort.createBooking(booking);

        // Publicar evento de reserva creada
        BookingCreatedEvent event = BookingCreatedEvent.builder()
                .bookingId(createdBooking.getId())
                .travelId(createdBooking.getTravelId())
                .origin(createdBooking.getOrigin())
                .destination(createdBooking.getDestination())
                .passengerId(createdBooking.getPassengerId())
                .reservedSeats(createdBooking.getReservedSeats())
                .timestamp(LocalDateTime.now())
                .build();

        eventPublisherPort.publishBookingCreatedEvent(event);

        return createdBooking;

    }

    @Override
    public void confirmBooking(String id) {
        bookingRepositoryPort.confirmBooking(id);
    }

    @Override
    public void cancelBooking(String id) {
        bookingRepositoryPort.cancelBooking(id);
    }

    @Override
    public Booking getBookingById(String id) {
        return bookingRepositoryPort.findBookingById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking with id " + id + " not found"));
    }

    @Override
    public List<Booking> getBookingsByPassengerId(Long passengerId) {
        return bookingRepositoryPort.findBookingsByPassengerId(passengerId);
    }

    @Override
    public List<Booking> getBookingsByTravelId(String travelId) {
        return bookingRepositoryPort.findBookingsByTravelId(travelId);
    }

    @Override
    public Booking updateSeats(String id, int newSeats) {
        Booking booking = bookingRepositoryPort.findBookingById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking with id " + id + " not found"));
        
        if (newSeats <= 0) {
            throw new InsufficientSeatsException("Number of seats must be greater than 0");
        }
        
        booking.setReservedSeats(newSeats);
        return bookingRepositoryPort.updateBooking(booking);
    }

    @Override
    public void completeBooking(String id) {
        bookingRepositoryPort.completeBooking(id);
    }

    @Override
    public boolean validateAvailability(String travelId, int requestedSeats) {
        int reservedSeats = bookingRepositoryPort.countReservedSeatsByTravelId(travelId);
        // Asumiendo que cada viaje tiene un máximo de 50 asientos
        // Este valor debería venir del servicio de viajes
        int totalSeats = 50;
        return (reservedSeats + requestedSeats) <= totalSeats;
    }

}