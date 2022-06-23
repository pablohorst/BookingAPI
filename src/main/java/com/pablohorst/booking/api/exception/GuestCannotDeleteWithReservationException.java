package com.pablohorst.booking.api.exception;

import com.pablohorst.booking.api.data.CommonStatusCode;

/**
 * @author Pablo Horst
 */
public class GuestCannotDeleteWithReservationException extends BookingApiException {
    public GuestCannotDeleteWithReservationException() {
        super(CommonStatusCode.GUEST_CANNOT_DELETE_WITH_RESERVATION);
    }
}
