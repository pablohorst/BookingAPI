package com.pablohorst.booking.api.exception;

import com.pablohorst.booking.api.data.CommonStatusCode;

/**
 * @author Pablo Horst
 */
public class ReservationCheckInTooInAdvanceException extends BookingApiException {
    public ReservationCheckInTooInAdvanceException() {
        super(CommonStatusCode.RESERVATION_CHECK_IN_TOO_IN_ADVANCE);
    }
}
