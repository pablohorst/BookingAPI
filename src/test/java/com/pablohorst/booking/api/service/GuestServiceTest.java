package com.pablohorst.booking.api.service;

import com.pablohorst.booking.api.data.model.Guest;
import com.pablohorst.booking.api.data.model.Reservation;
import com.pablohorst.booking.api.exception.GuestCannotDeleteWithReservationException;
import com.pablohorst.booking.api.exception.GuestNotFoundException;
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
class GuestServiceTest {
    private Guest validGuest;
    private Guest mockedGuest;

    private Reservation mockedReservation;

    @Mock
    GuestRepository guestRepository;

    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    private GuestService guestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(guestService, "guestRepository", guestRepository);
        ReflectionTestUtils.setField(guestService, "reservationRepository", reservationRepository);

        validGuest = Guest.builder()
                .active(true)
                .firstName("John")
                .lastName("Doe")
                .passport("12345")
                .nationality("USA")
                .profession("engineer")
                .build();

        mockedGuest = Guest.builder()
                .id(1)
                .active(true)
                .firstName("John")
                .lastName("Doe")
                .passport("12345")
                .nationality("USA")
                .profession("engineer")
                .build();

        mockedReservation = Reservation.builder()
                .id(1)
                .active(true)
                .guest(mockedGuest)
                .build();
    }

    @Test
    void Given_ValidGuest_When_createGuest_Then_ReturnGuestSaved() {
        when(guestRepository.save(any(Guest.class))).thenReturn(mockedGuest);
        Guest response = guestService.createGuest(validGuest);

        assertEquals(response.getId(), mockedGuest.getId());
    }

    @Test
    void Given_ValidIdOfList_When_getGuestList_Then_ReturnGuest() {
        when(guestRepository.findByIdAndActive(any(Long.class), any(boolean.class))).thenReturn(Optional.ofNullable(mockedGuest));
        Guest response = guestService.getGuestById(1L);

        assertEquals(response.getId(), mockedGuest.getId());
    }

    @Test
    void Given_ValidGuestWithModifiedValue_When_updateGuest_Then_ReturnGuestWithModifications() {
        when(guestRepository.findByIdAndActive(any(Long.class), any(boolean.class))).thenReturn(Optional.ofNullable(mockedGuest));
        when(guestRepository.save(any(Guest.class))).thenReturn(mockedGuest);

        Guest response = guestService.updateGuest(1L, validGuest);

        assertEquals(response.getId(), mockedGuest.getId());
    }

    @Test
    void Given_ValidGuestWithModifiedValueAndInvalidId_When_updateGuest_Then_ReturnGuestWithModifications() {
        when(guestRepository.findByIdAndActive(any(Long.class), any(boolean.class))).thenReturn(Optional.empty());

        assertThrows(GuestNotFoundException.class, () -> guestService.updateGuest(1L, validGuest));
    }

    @Test
    void Given_ValidIdOfList_When_deleteGuest_Then_Return() {
        when(guestRepository.findByIdAndActive(any(Long.class), any(boolean.class))).thenReturn(Optional.ofNullable(mockedGuest));
        when(reservationRepository.findByGuestIdAndActive(any(Long.class), any(boolean.class))).thenReturn(Optional.ofNullable(mockedReservation));

        assertThrows(GuestCannotDeleteWithReservationException.class, () -> guestService.deleteById(1L));
    }

    @Test
    void Given_Nothing_When_getGuestLists_Then_ReturnGuestLists() {
        when(guestRepository.findAllByActive(any(boolean.class))).thenReturn(Collections.singletonList(mockedGuest));

        List<Guest> response = guestService.getGuestList();

        assertNotNull(response);
    }
}