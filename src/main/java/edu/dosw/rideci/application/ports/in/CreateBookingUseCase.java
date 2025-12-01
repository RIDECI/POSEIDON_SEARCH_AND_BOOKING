package edu.dosw.rideci.application.ports.in;

import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.infrastructure.controllers.dto.Request.BookingRequest;


public interface CreateBookingUseCase {

    Booking createBooking(BookingRequest booking);

}