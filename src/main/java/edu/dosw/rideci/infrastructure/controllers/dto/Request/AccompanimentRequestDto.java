package edu.dosw.rideci.infrastructure.controllers.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccompanimentRequestDto {
    private String accompanimentId;
    private String passengerId;
    private String notes;
}
