package com.pablohorst.booking.api.exception;

import com.pablohorst.booking.api.data.CommonStatusCode;

/**
 * @author Pablo Horst
 */
public class GuestDuplicatedPassportException extends BookingApiException {
    public GuestDuplicatedPassportException() {
        super(CommonStatusCode.GUEST_DUPLICATED);
    }
}
