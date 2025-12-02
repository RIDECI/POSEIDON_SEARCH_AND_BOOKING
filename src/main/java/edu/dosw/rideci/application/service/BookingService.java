package edu.dosw.rideci.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.mappers.BookingMapperInitial;
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
import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.exceptions.BookingNotFoundException;
import edu.dosw.rideci.exceptions.InsufficientSeatsException;
import edu.dosw.rideci.infrastructure.controllers.dto.Request.BookingRequest;
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
    private final BookingMapperInitial bookingMapperInitial;

    @Override
    public Booking createBooking(BookingRequest bookingRequest) {

        Booking booking = bookingMapperInitial.toDomain(bookingRequest);
        return bookingRepositoryPort.createBooking(booking);

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