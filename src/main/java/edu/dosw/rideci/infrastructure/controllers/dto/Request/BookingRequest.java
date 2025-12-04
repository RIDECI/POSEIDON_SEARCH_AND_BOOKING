package edu.dosw.rideci.infrastructure.controllers.dto.Request;

import edu.dosw.rideci.domain.model.BookingBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BookingRequest extends BookingBase {

    private String id;

}
