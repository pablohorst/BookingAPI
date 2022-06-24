package com.pablohorst.booking.api.service;

import com.pablohorst.booking.api.data.model.Guest;
import com.pablohorst.booking.api.data.model.Reservation;
import com.pablohorst.booking.api.exception.*;
import com.pablohorst.booking.api.repository.GuestRepository;
import com.pablohorst.booking.api.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ReservationValidationTest {

    Reservation.ReservationBuilder validReservationBuilder;
    @Mock
    GuestRepository guestRepository;

    @Mock
    ReservationRepository reservationRepository;
    @InjectMocks
    ReservationService reservationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(reservationService, "guestRepository", guestRepository);
        ReflectionTestUtils.setField(reservationService, "reservationRepository", reservationRepository);

        validReservationBuilder = Reservation.builder()
                .bookingDate(LocalDateTime.parse("2022-01-01T00:00:00.000"))
                .checkInDate(Date.valueOf("2022-01-02"))
                .checkOutDate(Date.valueOf("2022-01-02"))
                .guest(Guest.builder().id(1).build());
    }

    // 1 - The checkInDate is not before bookingDate.
    @Test
    void Check_CheckInDate_Not_Before_BookingDate() {
        Reservation reservation = validReservationBuilder
                .bookingDate(LocalDateTime.parse("2022-01-01T00:00:00.000"))
                .checkInDate(Date.valueOf("2021-01-01"))
                .build();

        assertThrows(ReservationCheckInBeforeTodayException.class, () -> reservationService.validateReservation(reservation, true));
    }

    // 2 - The checkInDate is not after checkOutDate
    @Test
    void Check_CheckInDate_Not_After_CheckOutDate() {
        Reservation reservation = validReservationBuilder
                .checkInDate(Date.valueOf("2022-01-03"))
                .checkOutDate(Date.valueOf("2022-01-01"))
                .build();

        assertThrows(ReservationCheckInAfterCheckOutException.class, () -> reservationService.validateReservation(reservation, true));
    }

    // 3 - The reservation is no longer than 3 days
    @Test
    void Check_Reservation_No_Longer_Than_3_Days() {
        Reservation reservation = validReservationBuilder
                .checkInDate(Date.valueOf("2022-01-02"))
                .checkOutDate(Date.valueOf("2022-01-06"))
                .build();

        assertThrows(ReservationTooLongException.class, () -> reservationService.validateReservation(reservation, true));
    }

    // 4 - The room can’t be reserved more than 30 days in advance
    @Test
    void Check_Reservation_No_More_Than_30_Days_In_Advance() {
        Reservation reservation = validReservationBuilder
                .bookingDate(LocalDateTime.parse("2021-01-01T00:00:00.000"))
                .build();

        assertThrows(ReservationCheckInTooInAdvanceException.class, () -> reservationService.validateReservation(reservation, true));
    }

    // 5 - The guest exists
    @Test
    void Check_Reservation_Guest_Exists() {
        when(guestRepository.findByIdAndActive(any(Long.class), any(Boolean.class))).thenReturn(Optional.empty());

        Reservation reservation = validReservationBuilder.build();

        assertThrows(GuestNotFoundException.class, () -> reservationService.validateReservation(reservation, true));
    }

    // 6 - The room is not reserved for the provided day
    @Test
    void Check_Room_Available() {
        when(guestRepository.findByIdAndActive(any(Long.class), any(Boolean.class)))
                .thenReturn(Optional.ofNullable(Guest.builder().build()));
        when(reservationRepository.findByGivenDates(any(Date.class), any(Date.class)))
                .thenReturn(Optional.ofNullable(validReservationBuilder.build()));

        Reservation reservation = validReservationBuilder.build();

        assertThrows(ReservationRoomNotAvailableException.class, () -> reservationService.validateReservation(reservation, true));
    }
}
