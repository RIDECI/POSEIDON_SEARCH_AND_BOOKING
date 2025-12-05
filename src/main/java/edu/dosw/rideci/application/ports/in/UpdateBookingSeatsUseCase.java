package edu.dosw.rideci.application.ports.in;

import edu.dosw.rideci.domain.model.Booking;

public interface UpdateBookingSeatsUseCase {

    Booking updateSeats(String id, int newSeats);

}
