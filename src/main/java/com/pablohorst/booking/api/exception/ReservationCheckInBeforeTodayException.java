package com.pablohorst.booking.api.exception;

import com.pablohorst.booking.api.data.CommonStatusCode;

/**
 * @author Pablo Horst
 */
public class ReservationCheckInBeforeTodayException extends BookingApiException {
    public ReservationCheckInBeforeTodayException() {
        super(CommonStatusCode.RESERVATION_CHECK_IN_BEFORE_TODAY);
    }
}
