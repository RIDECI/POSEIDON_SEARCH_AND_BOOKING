package edu.dosw.rideci.unit.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.domain.model.enums.BookingStatus;
import edu.dosw.rideci.exceptions.BookingNotFoundException;
import edu.dosw.rideci.infrastructure.adapters.BookingRepositoryAdapter;
import edu.dosw.rideci.infrastructure.adapters.mapper.BookingMapper;
import edu.dosw.rideci.infrastructure.persistence.entity.BookingDocument;
import edu.dosw.rideci.infrastructure.persistence.repository.BookingRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookingRepositoryAdapter Tests")
class BookingRepositoryAdapterTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingRepositoryAdapter bookingRepositoryAdapter;

    private Booking booking;
    private BookingDocument bookingDocument;

    @BeforeEach
    void setUp() {
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

        bookingDocument = BookingDocument.builder()
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
    }

    @Test
    @DisplayName("Should create booking successfully")
    void shouldCreateBookingSuccessfully() {
        // Given
        when(bookingMapper.toDocument(booking)).thenReturn(bookingDocument);
        when(bookingRepository.save(bookingDocument)).thenReturn(bookingDocument);
        when(bookingMapper.toDomain(bookingDocument)).thenReturn(booking);

        // When
        Booking result = bookingRepositoryAdapter.createBooking(booking);

        // Then
        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getTravelId(), result.getTravelId());
        verify(bookingMapper, times(1)).toDocument(booking);
        verify(bookingRepository, times(1)).save(bookingDocument);
        verify(bookingMapper, times(1)).toDomain(bookingDocument);
    }

    @Test
    @DisplayName("Should confirm booking successfully")
    void shouldConfirmBookingSuccessfully() {
        // Given
        String bookingId = "BOOKING-001";
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingDocument));
        when(bookingRepository.save(any(BookingDocument.class))).thenReturn(bookingDocument);

        // When
        bookingRepositoryAdapter.confirmBooking(bookingId);

        // Then
        ArgumentCaptor<BookingDocument> captor = ArgumentCaptor.forClass(BookingDocument.class);
        verify(bookingRepository, times(1)).save(captor.capture());
        BookingDocument savedBooking = captor.getValue();
        assertEquals(BookingStatus.CONFIRMED, savedBooking.getStatus());
        assertNotNull(savedBooking.getConfirmationDate());
    }

    @Test
    @DisplayName("Should throw exception when confirming non-existent booking")
    void shouldThrowExceptionWhenConfirmingNonExistentBooking() {
        // Given
        String bookingId = "NON-EXISTENT";
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookingNotFoundException.class, 
                () -> bookingRepositoryAdapter.confirmBooking(bookingId));
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    @DisplayName("Should cancel booking successfully")
    void shouldCancelBookingSuccessfully() {
        // Given
        String bookingId = "BOOKING-001";
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingDocument));
        when(bookingRepository.save(any(BookingDocument.class))).thenReturn(bookingDocument);

        // When
        bookingRepositoryAdapter.cancelBooking(bookingId);

        // Then
        ArgumentCaptor<BookingDocument> captor = ArgumentCaptor.forClass(BookingDocument.class);
        verify(bookingRepository, times(1)).save(captor.capture());
        BookingDocument savedBooking = captor.getValue();
        assertEquals(BookingStatus.CANCELLED, savedBooking.getStatus());
        assertNotNull(savedBooking.getCancellationDate());
    }

    @Test
    @DisplayName("Should throw exception when canceling non-existent booking")
    void shouldThrowExceptionWhenCancelingNonExistentBooking() {
        // Given
        String bookingId = "NON-EXISTENT";
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookingNotFoundException.class, 
                () -> bookingRepositoryAdapter.cancelBooking(bookingId));
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    @DisplayName("Should find booking by id successfully")
    void shouldFindBookingByIdSuccessfully() {
        // Given
        String bookingId = "BOOKING-001";
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingDocument));
        when(bookingMapper.toDomain(bookingDocument)).thenReturn(booking);

        // When
        Optional<Booking> result = bookingRepositoryAdapter.findBookingById(bookingId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(bookingId, result.get().getId());
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(bookingMapper, times(1)).toDomain(bookingDocument);
    }

    @Test
    @DisplayName("Should return empty when booking not found by id")
    void shouldReturnEmptyWhenBookingNotFoundById() {
        // Given
        String bookingId = "NON-EXISTENT";
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // When
        Optional<Booking> result = bookingRepositoryAdapter.findBookingById(bookingId);

        // Then
        assertFalse(result.isPresent());
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    @DisplayName("Should find bookings by passenger id successfully")
    void shouldFindBookingsByPassengerIdSuccessfully() {
        // Given
        Long passengerId = 1L;
        List<BookingDocument> documents = Arrays.asList(bookingDocument);
        when(bookingRepository.findByPassengerId(passengerId)).thenReturn(documents);
        when(bookingMapper.toDomain(bookingDocument)).thenReturn(booking);

        // When
        List<Booking> result = bookingRepositoryAdapter.findBookingsByPassengerId(passengerId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(passengerId, result.get(0).getPassengerId());
        verify(bookingRepository, times(1)).findByPassengerId(passengerId);
    }

    @Test
    @DisplayName("Should find bookings by travel id successfully")
    void shouldFindBookingsByTravelIdSuccessfully() {
        // Given
        String travelId = "TRAVEL-001";
        List<BookingDocument> documents = Arrays.asList(bookingDocument);
        when(bookingRepository.findByTravelId(travelId)).thenReturn(documents);
        when(bookingMapper.toDomain(bookingDocument)).thenReturn(booking);

        // When
        List<Booking> result = bookingRepositoryAdapter.findBookingsByTravelId(travelId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(travelId, result.get(0).getTravelId());
        verify(bookingRepository, times(1)).findByTravelId(travelId);
    }

    @Test
    @DisplayName("Should update booking successfully")
    void shouldUpdateBookingSuccessfully() {
        // Given
        Booking updatedBooking = Booking.builder()
                .id("BOOKING-001")
                .travelId("TRAVEL-001")
                .passengerId(1L)
                .origin("Bogotá")
                .destination("Medellín")
                .reservedSeats(3)
                .totalAmount(200000.0)
                .status(BookingStatus.CONFIRMED)
                .build();

        BookingDocument updatedDocument = BookingDocument.builder()
                .id("BOOKING-001")
                .reservedSeats(3)
                .totalAmount(200000.0)
                .status(BookingStatus.CONFIRMED)
                .build();

        when(bookingMapper.toDocument(updatedBooking)).thenReturn(updatedDocument);
        when(bookingRepository.save(any(BookingDocument.class))).thenReturn(updatedDocument);
        when(bookingMapper.toDomain(updatedDocument)).thenReturn(updatedBooking);

        // When
        Booking result = bookingRepositoryAdapter.updateBooking(updatedBooking);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getReservedSeats());
        verify(bookingMapper, times(1)).toDocument(updatedBooking);
        verify(bookingRepository, times(1)).save(any(BookingDocument.class));
    }

    @Test
    @DisplayName("Should complete booking successfully")
    void shouldCompleteBookingSuccessfully() {
        // Given
        String bookingId = "BOOKING-001";
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingDocument));
        when(bookingRepository.save(any(BookingDocument.class))).thenReturn(bookingDocument);

        // When
        bookingRepositoryAdapter.completeBooking(bookingId);

        // Then
        ArgumentCaptor<BookingDocument> captor = ArgumentCaptor.forClass(BookingDocument.class);
        verify(bookingRepository, times(1)).save(captor.capture());
        BookingDocument savedBooking = captor.getValue();
        assertEquals(BookingStatus.COMPLETED, savedBooking.getStatus());
        assertNotNull(savedBooking.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw exception when completing non-existent booking")
    void shouldThrowExceptionWhenCompletingNonExistentBooking() {
        // Given
        String bookingId = "NON-EXISTENT";
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookingNotFoundException.class, 
                () -> bookingRepositoryAdapter.completeBooking(bookingId));
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    @DisplayName("Should count reserved seats by travel id successfully")
    void shouldCountReservedSeatsByTravelIdSuccessfully() {
        // Given
        String travelId = "TRAVEL-001";
        
        BookingDocument confirmedBooking = BookingDocument.builder()
                .travelId(travelId)
                .reservedSeats(2)
                .status(BookingStatus.CONFIRMED)
                .build();

        BookingDocument pendingBooking = BookingDocument.builder()
                .travelId(travelId)
                .reservedSeats(3)
                .status(BookingStatus.PENDING)
                .build();

        BookingDocument cancelledBooking = BookingDocument.builder()
                .travelId(travelId)
                .reservedSeats(5)
                .status(BookingStatus.CANCELLED)
                .build();

        List<BookingDocument> documents = Arrays.asList(confirmedBooking, pendingBooking, cancelledBooking);
        when(bookingRepository.findByTravelId(travelId)).thenReturn(documents);

        // When
        int result = bookingRepositoryAdapter.countReservedSeatsByTravelId(travelId);

        // Then
        assertEquals(5, result); // Only confirmed (2) + pending (3) = 5, cancelled not counted
        verify(bookingRepository, times(1)).findByTravelId(travelId);
    }

    @Test
    @DisplayName("Should return zero when no reserved seats for travel")
    void shouldReturnZeroWhenNoReservedSeatsForTravel() {
        // Given
        String travelId = "TRAVEL-001";
        when(bookingRepository.findByTravelId(travelId)).thenReturn(Arrays.asList());

        // When
        int result = bookingRepositoryAdapter.countReservedSeatsByTravelId(travelId);

        // Then
        assertEquals(0, result);
        verify(bookingRepository, times(1)).findByTravelId(travelId);
    }
}
