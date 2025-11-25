package edu.dosw.rideci.exceptions;

public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException(String tripId) {
        super("Trip not found with id: " + tripId);
    }
}