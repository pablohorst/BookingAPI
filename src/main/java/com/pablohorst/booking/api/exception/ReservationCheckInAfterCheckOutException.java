package com.pablohorst.booking.api.exception;

import com.pablohorst.booking.api.data.CommonStatusCode;

/**
 * @author Pablo Horst
 */
public class ReservationCheckInAfterCheckOutException extends BookingApiException {
    public ReservationCheckInAfterCheckOutException() {
        super(CommonStatusCode.RESERVATION_CHECK_IN_AFTER_CHECK_OUT);
    }
}
