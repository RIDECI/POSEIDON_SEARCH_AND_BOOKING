package edu.dosw.rideci.infrastructure.controllers;

import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.application.ports.in.CreateBookingPort;
import edu.dosw.rideci.application.ports.in.CancelBookingPort;
import edu.dosw.rideci.application.ports.in.ConfirmBookingPort;
import edu.dosw.rideci.infrastructure.dto.Request.CreateBookingRequest;
import edu.dosw.rideci.infrastructure.dto.Request.CancelBookingRequest;
import edu.dosw.rideci.infrastructure.dto.Response.BookingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    
    private final CreateBookingPort createBookingPort;
    private final CancelBookingPort cancelBookingPort;
    private final ConfirmBookingPort confirmBookingPort;
    
    public BookingController(CreateBookingPort createBookingPort,
                            CancelBookingPort cancelBookingPort,
                            ConfirmBookingPort confirmBookingPort) {
        this.createBookingPort = createBookingPort;
        this.cancelBookingPort = cancelBookingPort;
        this.confirmBookingPort = confirmBookingPort;
    }
    
    /**
     * Create a new booking
     */
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody CreateBookingRequest request) {
        try {
            Booking booking = createBookingPort.createBooking(
                    request.getTripId(),
                    request.getPassengerId(),
                    request.getReservedSeats(),
                    request.getNotes()
            );
            
            BookingResponse response = mapToBookingResponse(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Cancel a booking
     */
    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable String bookingId,
                                              @RequestBody CancelBookingRequest request) {
        try {
            cancelBookingPort.cancelBooking(
                    bookingId,
                    request.getPassengerId(),
                    request.getCancellationReason()
            );
            
            return ResponseEntity.ok().build();
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Confirm a booking (called by payment events)
     */
    @PutMapping("/{bookingId}/confirm")
    public ResponseEntity<Void> confirmBooking(@PathVariable String bookingId,
                                               @RequestParam String paymentId) {
        try {
            confirmBookingPort.confirmBooking(bookingId, paymentId);
            
            return ResponseEntity.ok().build();
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Map Booking to BookingResponse
     */
    private BookingResponse mapToBookingResponse(Booking booking) {
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