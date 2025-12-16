package edu.dosw.rideci.infrastructure.controllers.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccompanimentRequestResponse {
    private String requestId;
    private String accompanimentId;
    private String passengerId;
    private String status;
    private String notes;
    private String createdAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccompanimentRequestDetailResponse {
        private String requestId;
        private String accompanimentId;
        private String passengerId;
        private String status;
        private String notes;
        private String createdAt;
        private AccompanimentDetailResponse accompanimentDetails;
    }
}
