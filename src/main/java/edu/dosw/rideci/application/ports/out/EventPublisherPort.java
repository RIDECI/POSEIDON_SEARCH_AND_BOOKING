package edu.dosw.rideci.application.ports.out;

import edu.dosw.rideci.application.event.BookingCreatedEvent;

public interface EventPublisherPort {

    void publishBookingCreatedEvent(BookingCreatedEvent event);

}
