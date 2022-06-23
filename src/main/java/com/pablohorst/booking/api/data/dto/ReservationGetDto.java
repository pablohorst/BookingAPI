package com.pablohorst.booking.api.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Reservation Data Transfer Object for GET operations and responses
 *
 * @author Pablo Horst
 */
@Data
@NoArgsConstructor
public class ReservationGetDto implements Serializable {
    private long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime bookingDate;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private GuestGetDto guest;
}
