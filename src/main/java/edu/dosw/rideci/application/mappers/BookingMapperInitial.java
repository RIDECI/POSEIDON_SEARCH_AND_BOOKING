package edu.dosw.rideci.application.mappers;

import org.mapstruct.Mapper;

import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.infrastructure.controllers.dto.Request.BookingRequest;
import edu.dosw.rideci.infrastructure.controllers.dto.Response.BookingResponse;


@Mapper(componentModel = "spring")
public interface BookingMapperInitial {

    BookingResponse toResponse(Booking booking);
    Booking toDomain(BookingRequest bookingRequest);
    
}