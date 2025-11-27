package edu.dosw.rideci.infrastructure.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {
    
    private String tripId;
    private String passengerId;
    private Integer reservedSeats;
    
    private String notes;

}