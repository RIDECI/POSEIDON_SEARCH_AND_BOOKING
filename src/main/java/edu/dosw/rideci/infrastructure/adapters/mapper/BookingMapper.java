package edu.dosw.rideci.infrastructure.adapters.mapper;

import org.mapstruct.Mapper;

import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.infrastructure.persistence.entity.BookingDocument;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking toDomain(BookingDocument bookingDocument);

    BookingDocument toDocument(Booking booking);
    
}
