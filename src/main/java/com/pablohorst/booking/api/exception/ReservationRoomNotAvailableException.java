package com.pablohorst.booking.api.exception;

import com.pablohorst.booking.api.data.CommonStatusCode;

/**
 * @author Pablo Horst
 */
public class ReservationRoomNotAvailableException extends BookingApiException {
    public ReservationRoomNotAvailableException() {
        super(CommonStatusCode.RESERVATION_ROOM_NOT_AVAILABLE);
    }
}
