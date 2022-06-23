package com.pablohorst.booking.api.data;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.http.HttpStatus;

/**
 * Enumeration that contains all the Status Codes for the application
 *
 * @author Pablo Horst
 */
public enum CommonStatusCode implements StatusCode {

    INVALID_REQUEST("InvalidRequest", HttpStatus.BAD_REQUEST, "Invalid request body"),
    MISSING_BODY("MissingBody", HttpStatus.BAD_REQUEST, "Missing required request body"),
    METHOD_NOT_ALLOWED("MethodNotAllowed", HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed"),
    GUEST_CANNOT_DELETE_WITH_RESERVATION("GuestCannotDeleteWithReservation", HttpStatus.BAD_REQUEST, "Cannot delete a guest with a reservation"),
    GUEST_NOT_FOUND("GuestNotFound", HttpStatus.NOT_FOUND, "Guest not found with the provided ID"),
    GUEST_DUPLICATED("GuestDuplicated", HttpStatus.BAD_REQUEST, "There's another guest with the provided passport number"),
    RESERVATION_CHECK_IN_AFTER_CHECK_OUT("ReservationCheckInAfterCheckOut", HttpStatus.BAD_REQUEST, "Reservation checkIn date cannot be after checkOut date"),
    RESERVATION_CHECK_IN_BEFORE_TODAY("ReservationCheckInBeforeToday", HttpStatus.BAD_REQUEST, "CheckIn date should be at least the next day of booking"),
    RESERVATION_CHECK_IN_TOO_IN_ADVANCE("ReservationCheckInTooInAdvance", HttpStatus.BAD_REQUEST, "The room can’t be reserved more than 30 days in advance"),
    RESERVATION_NOT_FOUND("ReservationNotFound", HttpStatus.NOT_FOUND, "Reservation not found with the provided ID"),
    RESERVATION_ROOM_NOT_AVAILABLE("ReservationRoomNotAvailable", HttpStatus.BAD_REQUEST, "The room is not free using the provided checkIn and checkOut dates"),
    RESERVATION_TOO_LONG("ReservationTooLong", HttpStatus.BAD_REQUEST, "The reservation cannot be longer than 3 days"),
    NOT_FOUND("NotFound", HttpStatus.NOT_FOUND, "Resource you requested doesn't exist"),
    SUCCESS("Success", HttpStatus.OK, "Successful Operation"),
    UNHANDLED_SERVER_ERROR("UnhandledSystemError", HttpStatus.INTERNAL_SERVER_ERROR, "Internal system error"),
    UNSUPPORTED_MEDIA_TYPE("UnsupportedMediaType", HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type"),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String description;

    CommonStatusCode(String code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
