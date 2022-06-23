package com.pablohorst.booking.api.controller;

import com.pablohorst.booking.api.data.dto.GuestGetDto;
import com.pablohorst.booking.api.data.dto.GuestPostDto;
import com.pablohorst.booking.api.data.entity.Guest;
import com.pablohorst.booking.api.data.message.response.BaseResponse;
import com.pablohorst.booking.api.service.IGuestService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * @author Pablo Horst
 */
@RestController
public class GuestController {
    private final IGuestService guestService;
    private final ModelMapper modelMapper;

    public GuestController(IGuestService guestService, ModelMapper modelMapper) {
        this.guestService = guestService;
        this.modelMapper = modelMapper;
    }

    public GuestGetDto convertToGetDto(Guest guest) {
        return modelMapper.map(guest, GuestGetDto.class);
    }

    public Guest convertPostDtoToEntity(GuestPostDto guestPostDto) {
        Guest guest = modelMapper.map(guestPostDto, Guest.class);

        // Setting to 0 to ensure identity will be kept clean
        guest.setId(0);

        return guest;
    }

    @PostMapping("/guest")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse createGuest(@Valid @RequestBody GuestPostDto guestPostDto) {
        guestService.createGuest(convertPostDtoToEntity(guestPostDto));
        return BaseResponse.builder().build();
    }

    @GetMapping("/guests/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse getGuestById(@PathVariable long id) {
        return BaseResponse.builder()
                .body(convertToGetDto(guestService.getGuestById(id)))
                .build();
    }

    @PutMapping("/guests/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateGuest(@PathVariable long id, @RequestBody GuestPostDto guestPostDto) {
        guestService.updateGuest(id, convertPostDtoToEntity(guestPostDto));
        return BaseResponse.builder().build();
    }

    @DeleteMapping("/guests/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteGuest(@PathVariable long id) {
        guestService.deleteById(id);
        return BaseResponse.builder().build();
    }

    @GetMapping("/guests")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse getGuestList() {
        return BaseResponse.builder()
                .body(guestService.getGuestList().stream()
                        .map(this::convertToGetDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
