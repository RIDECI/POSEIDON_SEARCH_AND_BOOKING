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
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.PostConstruct;

@Slf4j
@RestController
@RequestMapping("/api/v1/bookings")
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

    @PostConstruct
    public void checkDependencies() {
        log.info("=== DEPENDENCY CHECK ===");
        log.info("createBookingUseCase: {}", createBookingUseCase != null ? "INJECTED" : "NULL");
        log.info("confirmBookingUseCase: {}", confirmBookingUseCase != null ? "INJECTED" : "NULL");
        log.info("cancelBookingUseCase: {}", cancelBookingUseCase != null ? "INJECTED" : "NULL");
        log.info("getBookingByIdUseCase: {}", getBookingByIdUseCase != null ? "INJECTED" : "NULL");
        log.info("getBookingsByPassengerIdUseCase: {}", getBookingsByPassengerIdUseCase != null ? "INJECTED" : "NULL");
        log.info("getBookingsByTravelIdUseCase: {}", getBookingsByTravelIdUseCase != null ? "INJECTED" : "NULL");
        log.info("updateBookingSeatsUseCase: {}", updateBookingSeatsUseCase != null ? "INJECTED" : "NULL");
        log.info("completeBookingUseCase: {}", completeBookingUseCase != null ? "INJECTED" : "NULL");
        log.info("validateBookingAvailabilityUseCase: {}", validateBookingAvailabilityUseCase != null ? "INJECTED" : "NULL");
        log.info("bookingMapper: {}", bookingMapper != null ? "INJECTED" : "NULL");
        log.info("=== END DEPENDENCY CHECK ===");
    }

    @GetMapping("/debug-simple")
    public ResponseEntity<String> debugSimple() {
        log.info("=== DEBUG SIMPLE ENDPOINT CALLED ===");
        return ResponseEntity.ok("Debug endpoint working at " + new java.util.Date());
    }

    @GetMapping("/debug-dependencies")
    public ResponseEntity<String> debugDependencies() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dependencies Status:<br/>");
        sb.append("createBookingUseCase: ").append(createBookingUseCase != null ? "OK" : "NULL").append("<br/>");
        sb.append("validateBookingAvailabilityUseCase: ").append(validateBookingAvailabilityUseCase != null ? "OK" : "NULL").append("<br/>");
        sb.append("bookingMapper: ").append(bookingMapper != null ? "OK" : "NULL").append("<br/>");
        return ResponseEntity.ok(sb.toString());
    }

    @GetMapping("/validate-availability")
    public ResponseEntity<Boolean> validateAvailability(
            @RequestParam String travelId,
            @RequestParam int requestedSeats) {

        log.info("=== START validateAvailability ===");
        log.info("travelId: {}, requestedSeats: {}", travelId, requestedSeats);

        try {
            if (validateBookingAvailabilityUseCase == null) {
                log.error("validateBookingAvailabilityUseCase is NULL!");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
            }

            log.info("Calling validateBookingAvailabilityUseCase.validateAvailability()");
            boolean isAvailable = validateBookingAvailabilityUseCase.validateAvailability(travelId, requestedSeats);
            log.info("=== SUCCESS: Result = {} ===", isAvailable);
            return ResponseEntity.ok(isAvailable);

        } catch (Exception e) {
            log.error("=== ERROR in validateAvailability ===", e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @RequestBody BookingRequest bookingRequest) {

        log.info("=== CREATE BOOKING START ===");
        log.info("BookingRequest: {}", bookingRequest);

        try {
            if (createBookingUseCase == null) {
                log.error("createBookingUseCase is NULL!");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

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

            log.info("Command created: {}", command);
            Booking booking = createBookingUseCase.createBooking(command);
            log.info("Booking created: {}", booking);

            if (bookingMapper == null) {
                log.error("bookingMapper is NULL!");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            BookingResponse response = bookingMapper.toResponse(booking);
            log.info("=== CREATE BOOKING SUCCESS ===");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("=== ERROR in createBooking ===", e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmBooking(@PathVariable String id) {
        log.info("=== CONFIRM BOOKING: {} ===", id);
        try {
            confirmBookingUseCase.confirmBooking(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error confirming booking: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable String id) {
        log.info("=== CANCEL BOOKING: {} ===", id);
        try {
            cancelBookingUseCase.cancelBooking(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error canceling booking: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable String id) {
        log.info("=== GET BOOKING BY ID: {} ===", id);
        try {
            Booking booking = getBookingByIdUseCase.getBookingById(id);
            BookingResponse response = bookingMapper.toResponse(booking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting booking by id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByPassengerId(@PathVariable Long passengerId) {
        log.info("=== GET BOOKINGS BY PASSENGER ID: {} ===", passengerId);
        try {
            List<Booking> bookings = getBookingsByPassengerIdUseCase.getBookingsByPassengerId(passengerId);
            List<BookingResponse> responses = bookings.stream()
                    .map(bookingMapper::toResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting bookings by passengerId: {}", passengerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/travel/{travelId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByTravelId(@PathVariable String travelId) {
        log.info("=== GET BOOKINGS BY TRAVEL ID: {} ===", travelId);
        try {
            List<Booking> bookings = getBookingsByTravelIdUseCase.getBookingsByTravelId(travelId);
            List<BookingResponse> responses = bookings.stream()
                    .map(bookingMapper::toResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error getting bookings by travelId: {}", travelId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/seats")
    public ResponseEntity<BookingResponse> updateBookingSeats(
            @PathVariable String id,
            @RequestBody UpdateSeatsRequest request) {
        log.info("=== UPDATE BOOKING SEATS: {}, seats: {} ===", id, request.getReservedSeats());
        try {
            Booking booking = updateBookingSeatsUseCase.updateSeats(id, request.getReservedSeats());
            BookingResponse response = bookingMapper.toResponse(booking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating booking seats: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> completeBooking(@PathVariable String id) {
        log.info("=== COMPLETE BOOKING: {} ===", id);
        try {
            completeBookingUseCase.completeBooking(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error completing booking: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}