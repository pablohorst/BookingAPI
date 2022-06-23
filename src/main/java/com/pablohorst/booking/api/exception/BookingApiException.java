package com.pablohorst.booking.api.exception;

import com.pablohorst.booking.api.data.CommonStatusCode;
import lombok.Getter;

/**
 * Base Booking API Exception that contains its own statusCode
 *
 * @author Pablo Horst
 */
@Getter
public class BookingApiException extends RuntimeException {
    private final CommonStatusCode statusCode;

    public BookingApiException(CommonStatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
