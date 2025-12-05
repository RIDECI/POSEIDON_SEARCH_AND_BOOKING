package edu.dosw.rideci.unit.infrastructure;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import edu.dosw.rideci.application.dto.CreateBookingCommand;
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
import edu.dosw.rideci.domain.model.enums.BookingStatus;
import edu.dosw.rideci.infrastructure.controllers.BookingController;
import edu.dosw.rideci.infrastructure.controllers.dto.Request.BookingRequest;
import edu.dosw.rideci.infrastructure.controllers.dto.Request.UpdateSeatsRequest;
import edu.dosw.rideci.infrastructure.controllers.dto.Response.BookingResponse;

@WebMvcTest(BookingController.class)
@DisplayName("BookingController Tests")
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingMapperInitial bookingMapper;

    @MockBean
    private CreateBookingUseCase createBookingUseCase;

    @MockBean
    private ConfirmBookingUseCase confirmBookingUseCase;

    @MockBean
    private CancelBookingUseCase cancelBookingUseCase;

    @MockBean
    private GetBookingByIdUseCase getBookingByIdUseCase;

    @MockBean
    private GetBookingsByPassengerIdUseCase getBookingsByPassengerIdUseCase;

    @MockBean
    private GetBookingsByTravelIdUseCase getBookingsByTravelIdUseCase;

    @MockBean
    private UpdateBookingSeatsUseCase updateBookingSeatsUseCase;

    @MockBean
    private CompleteBookingUseCase completeBookingUseCase;

    @MockBean
    private ValidateBookingAvailabilityUseCase validateBookingAvailabilityUseCase;

    private ObjectMapper objectMapper;
    private Booking booking;
    private BookingResponse bookingResponse;
    private BookingRequest bookingRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        booking = Booking.builder()
                .id("BOOKING-001")
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .notes("Test booking")
                .bookingDate(LocalDateTime.now())
                .build();

        bookingResponse = BookingResponse.builder()
                .id("BOOKING-001")
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .notes("Test booking")
                .bookingDate(LocalDateTime.now())
                .build();

        bookingRequest = BookingRequest.builder()
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(2)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .notes("Test booking")
                .bookingDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should create booking successfully")
    void shouldCreateBookingSuccessfully() throws Exception {
        // Given
        when(createBookingUseCase.createBooking(any(CreateBookingCommand.class))).thenReturn(booking);
        when(bookingMapper.toResponse(any(Booking.class))).thenReturn(bookingResponse);

        // When & Then
        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("BOOKING-001"))
                .andExpect(jsonPath("$.travelId").value("TRAVEL-001"))
                .andExpect(jsonPath("$.passengerId").value(1))
                .andExpect(jsonPath("$.origin").value("Bogotá"))
                .andExpect(jsonPath("$.destination").value("Medellín"))
                .andExpect(jsonPath("$.reservedSeats").value(2));

        verify(createBookingUseCase, times(1)).createBooking(any(CreateBookingCommand.class));
        verify(bookingMapper, times(1)).toResponse(any(Booking.class));
    }

    @Test
    @DisplayName("Should confirm booking successfully")
    void shouldConfirmBookingSuccessfully() throws Exception {
        // Given
        String bookingId = "BOOKING-001";
        doNothing().when(confirmBookingUseCase).confirmBooking(bookingId);

        // When & Then
        mockMvc.perform(patch("/bookings/{id}/confirm", bookingId))
                .andExpect(status().isOk());

        verify(confirmBookingUseCase, times(1)).confirmBooking(bookingId);
    }

    @Test
    @DisplayName("Should cancel booking successfully")
    void shouldCancelBookingSuccessfully() throws Exception {
        // Given
        String bookingId = "BOOKING-001";
        doNothing().when(cancelBookingUseCase).cancelBooking(bookingId);

        // When & Then
        mockMvc.perform(delete("/bookings/{id}", bookingId))
                .andExpect(status().isNoContent());

        verify(cancelBookingUseCase, times(1)).cancelBooking(bookingId);
    }

    @Test
    @DisplayName("Should get booking by id successfully")
    void shouldGetBookingByIdSuccessfully() throws Exception {
        // Given
        String bookingId = "BOOKING-001";
        when(getBookingByIdUseCase.getBookingById(bookingId)).thenReturn(booking);
        when(bookingMapper.toResponse(booking)).thenReturn(bookingResponse);

        // When & Then
        mockMvc.perform(get("/bookings/{id}", bookingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("BOOKING-001"))
                .andExpect(jsonPath("$.travelId").value("TRAVEL-001"));

        verify(getBookingByIdUseCase, times(1)).getBookingById(bookingId);
        verify(bookingMapper, times(1)).toResponse(booking);
    }

    @Test
    @DisplayName("Should get bookings by passenger id successfully")
    void shouldGetBookingsByPassengerIdSuccessfully() throws Exception {
        // Given
        Long passengerId = 1L;
        List<Booking> bookings = Arrays.asList(booking);
        List<BookingResponse> responses = Arrays.asList(bookingResponse);
        
        when(getBookingsByPassengerIdUseCase.getBookingsByPassengerId(passengerId)).thenReturn(bookings);
        when(bookingMapper.toResponse(any(Booking.class))).thenReturn(bookingResponse);

        // When & Then
        mockMvc.perform(get("/bookings/passenger/{passengerId}", passengerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("BOOKING-001"))
                .andExpect(jsonPath("$[0].passengerId").value(1));

        verify(getBookingsByPassengerIdUseCase, times(1)).getBookingsByPassengerId(passengerId);
    }

    @Test
    @DisplayName("Should get bookings by travel id successfully")
    void shouldGetBookingsByTravelIdSuccessfully() throws Exception {
        // Given
        String travelId = "TRAVEL-001";
        List<Booking> bookings = Arrays.asList(booking);
        
        when(getBookingsByTravelIdUseCase.getBookingsByTravelId(travelId)).thenReturn(bookings);
        when(bookingMapper.toResponse(any(Booking.class))).thenReturn(bookingResponse);

        // When & Then
        mockMvc.perform(get("/bookings/travel/{travelId}", travelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("BOOKING-001"))
                .andExpect(jsonPath("$[0].travelId").value("TRAVEL-001"));

        verify(getBookingsByTravelIdUseCase, times(1)).getBookingsByTravelId(travelId);
    }

    @Test
    @DisplayName("Should update booking seats successfully")
    void shouldUpdateBookingSeatsSuccessfully() throws Exception {
        // Given
        String bookingId = "BOOKING-001";
        UpdateSeatsRequest request = new UpdateSeatsRequest();
        request.setReservedSeats(3);
        
        Booking updatedBooking = Booking.builder()
                .id(bookingId)
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(3)
                .totalAmount(150000.0)
                .status(BookingStatus.PENDING)
                .build();
        
        BookingResponse updatedResponse = BookingResponse.builder()
                .id(bookingId)
                .reservedSeats(3)
                .build();
        
        when(updateBookingSeatsUseCase.updateSeats(eq(bookingId), eq(3))).thenReturn(updatedBooking);
        when(bookingMapper.toResponse(updatedBooking)).thenReturn(updatedResponse);

        // When & Then
        mockMvc.perform(put("/bookings/{id}/seats", bookingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservedSeats").value(3));

        verify(updateBookingSeatsUseCase, times(1)).updateSeats(bookingId, 3);
        verify(bookingMapper, times(1)).toResponse(updatedBooking);
    }

    @Test
    @DisplayName("Should complete booking successfully")
    void shouldCompleteBookingSuccessfully() throws Exception {
        // Given
        String bookingId = "BOOKING-001";
        doNothing().when(completeBookingUseCase).completeBooking(bookingId);

        // When & Then
        mockMvc.perform(patch("/bookings/{id}/complete", bookingId))
                .andExpect(status().isOk());

        verify(completeBookingUseCase, times(1)).completeBooking(bookingId);
    }

    @Test
    @DisplayName("Should validate availability successfully")
    void shouldValidateAvailabilitySuccessfully() throws Exception {
        // Given
        String travelId = "TRAVEL-001";
        int requestedSeats = 2;
        
        when(validateBookingAvailabilityUseCase.validateAvailability(travelId, requestedSeats)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/bookings/validate-availability")
                .param("travelId", travelId)
                .param("requestedSeats", String.valueOf(requestedSeats)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(validateBookingAvailabilityUseCase, times(1)).validateAvailability(travelId, requestedSeats);
    }

    @Test
    @DisplayName("Should return false when no availability")
    void shouldReturnFalseWhenNoAvailability() throws Exception {
        // Given
        String travelId = "TRAVEL-001";
        int requestedSeats = 10;
        
        when(validateBookingAvailabilityUseCase.validateAvailability(travelId, requestedSeats)).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/bookings/validate-availability")
                .param("travelId", travelId)
                .param("requestedSeats", String.valueOf(requestedSeats)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));

        verify(validateBookingAvailabilityUseCase, times(1)).validateAvailability(travelId, requestedSeats);
    }
}
