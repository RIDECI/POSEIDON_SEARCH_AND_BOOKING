package edu.dosw.rideci.infrastructure.persistence.entity;

import edu.dosw.rideci.domain.model.enums.TravelType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "bookings")
public class BookingDocument {

    @Id
    private String id;

    @Indexed
    private String rideId;           // Relación con Ride

    private String passengerId;
    private String driverId;
    private Integer seatNumber;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private String transactionId;
    private String status;


}
