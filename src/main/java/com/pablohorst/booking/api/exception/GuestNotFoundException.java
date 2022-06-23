package com.pablohorst.booking.api.exception;

import com.pablohorst.booking.api.data.CommonStatusCode;

/**
 * @author Pablo Horst
 */
public class GuestNotFoundException extends BookingApiException {
    public GuestNotFoundException() {
        super(CommonStatusCode.GUEST_NOT_FOUND);
    }
}
