package edu.dosw.rideci.application.ports.in;

public interface ValidateBookingAvailabilityUseCase {

    boolean validateAvailability(String travelId, int requestedSeats);

}
