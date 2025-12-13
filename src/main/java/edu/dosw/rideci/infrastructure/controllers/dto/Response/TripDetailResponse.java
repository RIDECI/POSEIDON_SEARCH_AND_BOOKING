package edu.dosw.rideci.infrastructure.controllers.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripDetailResponse {
    private String id;
    private DriverInfo driver;
    private VehicleInfo vehicle;
    private TripInfo trip;
    private PricingInfo pricing;
    private String mapImageUrl;
    private String route;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DriverInfo {
        private String id;
        private String name;
        private String rating;
        private Integer totalTrips;
        private String avatar;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VehicleInfo {
        private String brand;
        private String model;
        private String color;
        private String plate;
        private String type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripInfo {
        private String origin;
        private String destination;
        private String date;
        private String departureTime;
        private String arrivalTime;
        private Integer availableSeats;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PricingInfo {
        private Double total;
        private String currency;
    }
}
