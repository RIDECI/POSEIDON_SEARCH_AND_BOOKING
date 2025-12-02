package edu.dosw.rideci.application.ports.in;

import edu.dosw.rideci.domain.model.Booking;

public interface GetBookingByIdUseCase {

    Booking getBookingById(String id);

}
