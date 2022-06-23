package com.pablohorst.booking.api.data;

import org.springframework.http.HttpStatus;

/**
 * @author Pablo Horst
 */
public interface StatusCode {
    String getCode();
    HttpStatus getHttpStatus();
    String getDescription();
}
