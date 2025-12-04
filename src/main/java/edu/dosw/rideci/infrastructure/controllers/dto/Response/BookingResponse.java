package edu.dosw.rideci.infrastructure.controllers.dto.Response;

import edu.dosw.rideci.domain.model.BookingBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BookingResponse extends BookingBase {

    private String id;

}