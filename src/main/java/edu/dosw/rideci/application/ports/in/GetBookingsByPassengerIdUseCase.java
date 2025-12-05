package edu.dosw.rideci.application.ports.in;

import java.util.List;

import edu.dosw.rideci.domain.model.Booking;

public interface GetBookingsByPassengerIdUseCase {

    List<Booking> getBookingsByPassengerId(Long passengerId);

}
