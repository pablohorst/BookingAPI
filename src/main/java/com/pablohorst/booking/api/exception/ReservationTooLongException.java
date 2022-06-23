package com.pablohorst.booking.api.exception;

import com.pablohorst.booking.api.data.CommonStatusCode;

/**
 * @author Pablo Horst
 */
public class ReservationTooLongException extends BookingApiException {
    public ReservationTooLongException() {
        super(CommonStatusCode.RESERVATION_TOO_LONG);
    }
}
