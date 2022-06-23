package com.pablohorst.booking.api.exception;

import com.pablohorst.booking.api.data.CommonStatusCode;

/**
 * @author Pablo Horst
 */
public class ReservationNotFoundException extends BookingApiException {
    public ReservationNotFoundException() {
        super(CommonStatusCode.RESERVATION_NOT_FOUND);
    }
}
