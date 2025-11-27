package edu.dosw.rideci.application.mappers;

import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.infrastructure.dto.Response.BookingResponse;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {
    
    /**
     * Convierte Booking a BookingResponse
     */
    public BookingResponse toBookingResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setTripId(booking.getTripId());
        response.setPassengerId(booking.getPassengerId());
        response.setReservedSeats(booking.getReservedSeats());
        response.setTotalAmount(booking.getTotalAmount());
        response.setStatus(booking.getStatus().toString());
        response.setNotes(booking.getNotes());
        response.setBookingDate(booking.getBookingDate());
        response.setCancellationDate(booking.getCancellationDate());
        response.setConfirmationDate(booking.getConfirmationDate());
        response.setPaymentId(booking.getPaymentId());
        response.setCreatedAt(booking.getCreatedAt());
        response.setUpdatedAt(booking.getUpdatedAt());
        return response;
    }
}