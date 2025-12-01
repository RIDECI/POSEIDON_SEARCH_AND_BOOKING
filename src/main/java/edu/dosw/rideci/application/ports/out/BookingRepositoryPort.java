package edu.dosw.rideci.application.ports.out;

import edu.dosw.rideci.domain.model.Booking;

public interface BookingRepositoryPort {

    Booking createBooking(Booking booking);

    void confirmBooking(String id);

    void cancelBooking(String id);

}