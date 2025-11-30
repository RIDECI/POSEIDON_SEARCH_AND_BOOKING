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
                .orElseThrow(() -> new BookingNotFoundException("The booking with id: {id} not found"));

        booking.setConfirmationDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.CONFIRMED);

        bookingRepository.save(booking);

    }

    @Transactional
    @Override
    public void cancelBooking(String id) {

        BookingDocument booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("The booking with id: {id} not found"));

        booking.setCancellationDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.CANCELLED);

        bookingRepository.save(booking);

    }

}