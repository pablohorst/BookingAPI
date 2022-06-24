package com.pablohorst.booking.api.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Reservation Data Transfer Object for POST and PUT operations
 *
 * @author Pablo Horst
 */
@Data
@NoArgsConstructor
public class ReservationPostDto implements Serializable {
    @NotNull
    private LocalDate checkInDate;
    @NotNull
    private LocalDate checkOutDate;
    @Min(1)
    @NotNull
    private Integer guestId;
}
