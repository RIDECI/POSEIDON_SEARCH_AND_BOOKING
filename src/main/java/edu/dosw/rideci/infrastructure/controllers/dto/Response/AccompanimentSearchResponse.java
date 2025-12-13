package edu.dosw.rideci.infrastructure.controllers.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccompanimentSearchResponse {
    private String id;
    private String passengerName;
    private String transportMethod; // "Transmilenio", "Bus", "Carro Particular", etc.
    private String rating;
    private String route;
    private String departureTime;
}
