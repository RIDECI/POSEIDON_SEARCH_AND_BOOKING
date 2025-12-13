package edu.dosw.rideci.infrastructure.controllers.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccompanimentDetailResponse {
    private String id;
    private PassengerInfo passenger;
    private RouteInfo route;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassengerInfo {
        private String name;
        private String rating;
        private String verificationStatus; // "Estudiante Verificado"
        private String avatar;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteInfo {
        private String transportMethod; // "Transmilenio"
        private String meetingPoint; // "Entrada Universidad"
        private String destination; // "Portal 80"
        private String departureTime; // "18:30"
        private String estimatedArrival; // "19:50"
    }
}
