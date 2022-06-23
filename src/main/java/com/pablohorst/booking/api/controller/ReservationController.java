package com.pablohorst.booking.api.controller;

import com.pablohorst.booking.api.data.dto.ReservationGetDto;
import com.pablohorst.booking.api.data.dto.ReservationPostDto;
import com.pablohorst.booking.api.data.entity.Reservation;
import com.pablohorst.booking.api.data.message.response.BaseResponse;
import com.pablohorst.booking.api.service.IReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.stream.Collectors;

/**
 * @author Pablo Horst
 */
@RestController
public class ReservationController {
    private final IReservationService reservationService;
    private final ModelMapper modelMapper;

    public ReservationController(IReservationService reservationService, ModelMapper modelMapper) {
        this.reservationService = reservationService;
        this.modelMapper = modelMapper;
    }

    public ReservationGetDto convertToGetDto(Reservation reservation) {
        ReservationGetDto reservationGetDto = modelMapper.map(reservation, ReservationGetDto.class);

        reservationGetDto.setCheckInDate(reservation.getCheckInDate().toLocalDate());
        reservationGetDto.setCheckOutDate(reservation.getCheckOutDate().toLocalDate());

        return reservationGetDto;
    }

    public Reservation convertPostDtoToEntity(ReservationPostDto reservationPostDto) {
        Reservation reservation = modelMapper.map(reservationPostDto, Reservation.class);

        // Setting to 0 to ensure identity will be kept clean
        reservation.setId(0);

        reservation.setCheckInDate(Date.valueOf(reservationPostDto.getCheckInDate()));
        reservation.setCheckOutDate(Date.valueOf(reservationPostDto.getCheckOutDate()));

        return reservation;
    }

    @PostMapping("/reservation")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse createReservation(@Valid @RequestBody ReservationPostDto reservationDto) {
        reservationService.createReservation(convertPostDtoToEntity(reservationDto));
        return BaseResponse.builder().build();
    }

    @GetMapping("/reservations/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse getReservationById(@PathVariable long id) {
        return BaseResponse.builder()
                .body(convertToGetDto(reservationService.getReservationById(id)))
                .build();
    }

    @PutMapping("/reservations/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateReservation(@PathVariable long id, @RequestBody ReservationPostDto reservationDto) {
        reservationService.updateReservation(id, convertPostDtoToEntity(reservationDto));
        return BaseResponse.builder().build();
    }

    @DeleteMapping("/reservations/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteReservation(@PathVariable long id) {
        reservationService.deleteById(id);
        return BaseResponse.builder().build();
    }

    @GetMapping("/reservations")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse getReservationList() {
        return BaseResponse.builder()
                .body(reservationService.getReservationList().stream()
                        .map(this::convertToGetDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
