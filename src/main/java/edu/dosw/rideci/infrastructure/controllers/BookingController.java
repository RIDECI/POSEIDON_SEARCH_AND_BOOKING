package edu.dosw.rideci.infrastructure.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.dosw.rideci.application.mappers.BookingMapperInitial;
import edu.dosw.rideci.application.ports.in.CancelBookingUseCase;
import edu.dosw.rideci.application.ports.in.CompleteBookingUseCase;
import edu.dosw.rideci.application.ports.in.ConfirmBookingUseCase;
import edu.dosw.rideci.application.ports.in.CreateBookingUseCase;
import edu.dosw.rideci.application.ports.in.GetBookingByIdUseCase;
import edu.dosw.rideci.application.ports.in.GetBookingsByPassengerIdUseCase;
import edu.dosw.rideci.application.ports.in.GetBookingsByTravelIdUseCase;
import edu.dosw.rideci.application.ports.in.UpdateBookingSeatsUseCase;
import edu.dosw.rideci.application.ports.in.ValidateBookingAvailabilityUseCase;
import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.infrastructure.controllers.dto.Request.BookingRequest;
import edu.dosw.rideci.infrastructure.controllers.dto.Request.UpdateSeatsRequest;
import edu.dosw.rideci.infrastructure.controllers.dto.Response.BookingResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingMapperInitial bookingMapper;
    private final CreateBookingUseCase createBookingUseCase;
    private final ConfirmBookingUseCase confirmBookingUseCase;
    private final CancelBookingUseCase cancelBookingUseCase;
    private final GetBookingByIdUseCase getBookingByIdUseCase;
    private final GetBookingsByPassengerIdUseCase getBookingsByPassengerIdUseCase;
    private final GetBookingsByTravelIdUseCase getBookingsByTravelIdUseCase;
    private final UpdateBookingSeatsUseCase updateBookingSeatsUseCase;
    private final CompleteBookingUseCase completeBookingUseCase;
    private final ValidateBookingAvailabilityUseCase validateBookingAvailabilityUseCase;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @RequestBody BookingRequest bookingRequest) {
        
        // Mapear BookingRequest (Infrastructure) a CreateBookingCommand (Application)
        edu.dosw.rideci.application.dto.CreateBookingCommand command = 
            edu.dosw.rideci.application.dto.CreateBookingCommand.builder()
                .travelId(bookingRequest.getTravelId())
                .passengerId(bookingRequest.getPassengerId())
                .origin(bookingRequest.getOrigin())
                .destination(bookingRequest.getDestination())
                .reservedSeats(bookingRequest.getReservedSeats())
                .totalAmount(bookingRequest.getTotalAmount())
                .status(bookingRequest.getStatus())
                .notes(bookingRequest.getNotes())
                .bookingDate(bookingRequest.getBookingDate())
                .build();
        
        Booking booking = createBookingUseCase.createBooking(command);
        BookingResponse response = bookingMapper.toResponse(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmBooking(@PathVariable String id) {
        confirmBookingUseCase.confirmBooking(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable String id) {
        cancelBookingUseCase.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable String id) {
        Booking booking = getBookingByIdUseCase.getBookingById(id);
        BookingResponse response = bookingMapper.toResponse(booking);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByPassengerId(@PathVariable Long passengerId) {
        List<Booking> bookings = getBookingsByPassengerIdUseCase.getBookingsByPassengerId(passengerId);
        List<BookingResponse> responses = bookings.stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/travel/{travelId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByTravelId(@PathVariable String travelId) {
        List<Booking> bookings = getBookingsByTravelIdUseCase.getBookingsByTravelId(travelId);
        List<BookingResponse> responses = bookings.stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}/seats")
    public ResponseEntity<BookingResponse> updateBookingSeats(
            @PathVariable String id,
            @RequestBody UpdateSeatsRequest request) {
        Booking booking = updateBookingSeatsUseCase.updateSeats(id, request.getReservedSeats());
        BookingResponse response = bookingMapper.toResponse(booking);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> completeBooking(@PathVariable String id) {
        completeBookingUseCase.completeBooking(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate-availability")
    public ResponseEntity<Boolean> validateAvailability(
            @RequestParam String travelId,
            @RequestParam int requestedSeats) {
        boolean isAvailable = validateBookingAvailabilityUseCase.validateAvailability(travelId, requestedSeats);
        return ResponseEntity.ok(isAvailable);
    }

}