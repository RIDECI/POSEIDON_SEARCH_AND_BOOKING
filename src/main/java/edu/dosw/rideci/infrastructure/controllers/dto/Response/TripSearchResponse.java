package edu.dosw.rideci.infrastructure.controllers.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripSearchResponse {
    private String id;
    private String driverName;
    private String vehicleType;
    private String rating;
    private String route;
    private String departureTime;
    private Double price;
    private Integer availableSeats;
}
