package com.pablohorst.booking.api.service;

import com.pablohorst.booking.api.data.model.Guest;
import com.pablohorst.booking.api.data.model.Reservation;
import com.pablohorst.booking.api.exception.ReservationNotFoundException;
import com.pablohorst.booking.api.repository.GuestRepository;
import com.pablohorst.booking.api.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Pablo Horst
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    private Guest mockedGuest;

    private Reservation validReservation;
    private Reservation mockedReservation;

    @Mock
    GuestRepository guestRepository;

    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(reservationService, "guestRepository", guestRepository);
        ReflectionTestUtils.setField(reservationService, "reservationRepository", reservationRepository);

        mockedGuest = Guest.builder()
                .id(1)
                .active(true)
                .firstName("John")
                .lastName("Doe")
                .passport("12345")
                .nationality("USA")
                .profession("engineer")
                .build();

        Guest requestGuest = Guest.builder()
                .id(1)
                .build();

        validReservation = Reservation.builder()
                .active(true)
                .bookingDate(LocalDateTime.parse("2022-06-23T00:00:00.000"))
                .checkInDate(Date.valueOf("2022-07-20"))
                .checkOutDate(Date.valueOf("2022-07-21"))
                .guest(requestGuest)
                .build();

        mockedReservation = Reservation.builder()
                .id(1)
                .active(true)
                .bookingDate(LocalDateTime.parse("2022-06-23T00:00:00.000"))
                .checkInDate(Date.valueOf("2022-07-20"))
                .checkOutDate(Date.valueOf("2022-07-21"))
                .guest(mockedGuest)
                .build();
    }

    @Test
    void Given_ValidReservation_When_createReservation_Then_ReturnReservationSaved() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(mockedReservation);
        when(guestRepository.findByIdAndActive(any(Long.class), any(Boolean.class))).thenReturn(Optional.ofNullable(mockedGuest));

        Reservation response = reservationService.createReservation(validReservation);

        assertEquals(response.getId(), mockedReservation.getId());
    }

    @Test
    void Given_ValidIdOfList_When_getReservationList_Then_ReturnReservationList() {
        when(reservationRepository.findByIdAndActive(any(Long.class), any(boolean.class))).thenReturn(Optional.ofNullable(mockedReservation));
        Reservation response = reservationService.getReservationById(1L);

        assertEquals(response.getId(), mockedReservation.getId());
    }

    @Test
    void Given_ValidReservationWithModifiedValue_When_updateReservation_Then_ReturnReservationWithModifications() {
        when(reservationRepository.findByIdAndActive(any(Long.class), any(boolean.class))).thenReturn(Optional.ofNullable(mockedReservation));
        when(guestRepository.findByIdAndActive(any(Long.class), any(Boolean.class))).thenReturn(Optional.ofNullable(mockedGuest));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(mockedReservation);

        Reservation response = reservationService.updateReservation(1L, validReservation);

        assertEquals(response.getId(), mockedReservation.getId());
    }

    @Test
    void Given_ValidReservationWithModifiedValueAndInvalidId_When_updateReservation_Then_ReturnReservationWithModifications() {
        when(reservationRepository.findByIdAndActive(any(Long.class), any(boolean.class))).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> reservationService.updateReservation(1L, validReservation));
    }

    @Test
    void Given_ValidIdOfList_When_deleteReservation_Then_Return() {
        when(reservationRepository.findByIdAndActive(any(Long.class), any(boolean.class))).thenReturn(Optional.ofNullable(mockedReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(mockedReservation);

        Reservation response = reservationService.deleteById(1L);

        assertEquals(response.getId(), mockedReservation.getId());
    }

    @Test
    void Given_Nothing_When_getReservationLists_Then_ReturnReservationLists() {
        when(reservationRepository.findAllByActive(any(boolean.class))).thenReturn(Collections.singletonList(mockedReservation));

        List<Reservation> response = reservationService.getReservationList();

        assertNotNull(response);
    }
}