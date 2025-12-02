package edu.dosw.rideci.application.ports.out;

import java.util.List;
import java.util.Optional;

import edu.dosw.rideci.domain.model.Booking;

public interface BookingRepositoryPort {

    Booking createBooking(Booking booking);

    void confirmBooking(String id);

    void cancelBooking(String id);

    Optional<Booking> findBookingById(String id);

    List<Booking> findBookingsByPassengerId(Long passengerId);

    List<Booking> findBookingsByTravelId(String travelId);

    Booking updateBooking(Booking booking);

    void completeBooking(String id);

    int countReservedSeatsByTravelId(String travelId);

}