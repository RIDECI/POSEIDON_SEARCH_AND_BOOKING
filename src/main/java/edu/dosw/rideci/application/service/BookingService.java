package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.mappers.BookingMapperInitial;
import edu.dosw.rideci.application.ports.in.CancelBookingUseCase;
import edu.dosw.rideci.application.ports.in.ConfirmBookingUseCase;
import edu.dosw.rideci.application.ports.in.CreateBookingUseCase;
import edu.dosw.rideci.application.ports.out.BookingRepositoryPort;
import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.infrastructure.controllers.dto.Request.BookingRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService implements CreateBookingUseCase, CancelBookingUseCase, ConfirmBookingUseCase {

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

}