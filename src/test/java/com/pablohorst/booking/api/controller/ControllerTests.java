package com.pablohorst.booking.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablohorst.booking.api.data.dto.GuestPostDto;
import com.pablohorst.booking.api.data.dto.ReservationPostDto;
import com.pablohorst.booking.api.data.model.Guest;
import com.pablohorst.booking.api.data.model.Reservation;
import com.pablohorst.booking.api.service.GuestService;
import com.pablohorst.booking.api.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class intended to test Bean Validations
 *
 * @author Pablo Horst
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = {"spring.jackson.deserialization.fail-on-unknown-properties=false"})
@AutoConfigureMockMvc
@DisplayName("ControllerTests")
class ControllerTests {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Guest mockedGuest;
    private GuestPostDto mockedGuestPostDto;

    private Reservation mockedReservation;
    private ReservationPostDto mockedReservationPostDto;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    GuestService guestService;
    @MockBean
    ReservationService reservationService;

    @BeforeEach
    void setUp() {
        mockedGuest = Guest.builder()
                .id(1)
                .active(true)
                .firstName("John")
                .lastName("Doe")
                .passport("12345")
                .nationality("USA")
                .profession("engineer")
                .build();

        mockedGuestPostDto = GuestPostDto.builder()
                .firstName("John")
                .lastName("Doe")
                .passport("12345")
                .nationality("USA")
                .profession("engineer")
                .build();

        mockedReservation = Reservation.builder()
                .id(1)
                .active(true)
                .bookingDate(LocalDateTime.parse("2022-06-23T00:00:00.000"))
                .checkInDate(Date.valueOf("2022-07-20"))
                .checkOutDate(Date.valueOf("2022-07-21"))
                .guest(mockedGuest)
                .build();

        mockedReservationPostDto = ReservationPostDto.builder()
                .checkInDate(LocalDate.parse("2022-07-20"))
                .checkOutDate(LocalDate.parse("2022-07-21"))
                .guestId(1)
                .build();
    }

    @Test
    void testGuestCreation_With_Valid_Params() throws Exception {
        when(guestService.createGuest(any())).thenReturn(mockedGuest);

        mockMvc.perform(post("/guest")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockedGuestPostDto)))
                .andExpect(status().is2xxSuccessful());
    }

    void testReservationCreation_With_Valid_Params() throws Exception {
        when(reservationService.createReservation(any())).thenReturn(mockedReservation);

        mockMvc.perform(post("/reservation")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockedReservationPostDto)))
                .andExpect(status().is2xxSuccessful());
    }
}
