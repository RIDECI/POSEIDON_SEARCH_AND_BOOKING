package edu.dosw.rideci.application.ports.in;

import java.util.List;

import edu.dosw.rideci.domain.model.Booking;

public interface GetBookingsByTravelIdUseCase {

    List<Booking> getBookingsByTravelId(String travelId);

}
