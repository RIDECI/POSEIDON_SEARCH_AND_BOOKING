package edu.dosw.rideci.infrastructure.controllers.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSeatsRequest {

    private int reservedSeats;

}
