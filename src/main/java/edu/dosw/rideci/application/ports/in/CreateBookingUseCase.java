package edu.dosw.rideci.application.ports.in;

import edu.dosw.rideci.application.dto.CreateBookingCommand;
import edu.dosw.rideci.domain.model.Booking;


public interface CreateBookingUseCase {

    Booking createBooking(CreateBookingCommand command);

}