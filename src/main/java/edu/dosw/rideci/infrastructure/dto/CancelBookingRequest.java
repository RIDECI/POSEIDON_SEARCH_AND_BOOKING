package edu.dosw.rideci.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelBookingRequest {
    
    private String passengerId;
    
    private String cancellationReason;

}