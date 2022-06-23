package com.pablohorst.booking.api.data.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDateTime;

/**
 * Reservation entity that has all the necessary data for the hotel room booking
 *
 * @author Pablo Horst
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long id;

    // Indicates the date the reservation was booked
    @NotNull
    private LocalDateTime bookingDate;

    // Indicates the Check-In Date for the reservation
    @NotNull
    private Date checkInDate;

    // Indicates the Check-Out Date for the reservation
    @NotNull
    private Date checkOutDate;

    // Boolean flag intended for logical deletion
    @Builder.Default
    @NotNull
    private boolean active = true;

    @ManyToOne
    @NotNull
    private Guest guest;
}
