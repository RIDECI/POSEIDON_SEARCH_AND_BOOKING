package edu.dosw.rideci.infrastructure.adapters;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.dosw.rideci.application.ports.out.BookingRepositoryPort;
import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.domain.model.enums.BookingStatus;
import edu.dosw.rideci.exceptions.BookingNotFoundException;
import edu.dosw.rideci.infrastructure.adapters.mapper.BookingMapper;
import edu.dosw.rideci.infrastructure.persistence.repository.BookingRepository;
import edu.dosw.rideci.infrastructure.persistence.entity.BookingDocument;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryAdapter implements BookingRepositoryPort {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    @Override
    public Booking createBooking(Booking booking) {

        BookingDocument bookingToCreate = bookingMapper.toDocument(booking);

        BookingDocument bookingToSave = bookingRepository.save(bookingToCreate);

        return bookingMapper.toDomain(bookingToSave);
    }

    @Transactional
    @Override
    public void confirmBooking(String id) {

        BookingDocument booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("The booking with id: " + id + " not found"));

        booking.setConfirmationDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.CONFIRMED);

        bookingRepository.save(booking);

    }

    @Transactional
    @Override
    public void cancelBooking(String id) {

        BookingDocument booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("The booking with id:" +id+ " not found"));

        booking.setCancellationDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.CANCELLED);

        bookingRepository.save(booking);

    }

    @Override
    public java.util.Optional<Booking> findBookingById(String id) {
        return bookingRepository.findById(id)
                .map(bookingMapper::toDomain);
    }

    @Override
    public java.util.List<Booking> findBookingsByPassengerId(Long passengerId) {
        return bookingRepository.findByPassengerId(passengerId)
                .stream()
                .map(bookingMapper::toDomain)
                .toList();
    }

    @Override
    public java.util.List<Booking> findBookingsByTravelId(String travelId) {
        return bookingRepository.findByTravelId(travelId)
                .stream()
                .map(bookingMapper::toDomain)
                .toList();
    }

    @Transactional
    @Override
    public Booking updateBooking(Booking booking) {
        BookingDocument bookingDocument = bookingMapper.toDocument(booking);
        bookingDocument.setUpdatedAt(LocalDateTime.now());
        BookingDocument savedDocument = bookingRepository.save(bookingDocument);
        return bookingMapper.toDomain(savedDocument);
    }

    @Transactional
    @Override
    public void completeBooking(String id) {
        BookingDocument booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("The booking with id: +"+ id + " not found"));

        booking.setStatus(BookingStatus.COMPLETED);
        booking.setUpdatedAt(LocalDateTime.now());

        bookingRepository.save(booking);
    }

    @Override
    public int countReservedSeatsByTravelId(String travelId) {
        return bookingRepository.findByTravelId(travelId)
                .stream()
                .filter(booking -> booking.getStatus() == BookingStatus.CONFIRMED 
                        || booking.getStatus() == BookingStatus.PENDING)
                .mapToInt(BookingDocument::getReservedSeats)
                .sum();
    }

}