package com.pablohorst.booking.api.util;

import com.pablohorst.booking.api.data.CommonStatusCode;
import com.pablohorst.booking.api.data.StatusCode;
import com.pablohorst.booking.api.data.message.response.BaseResponse;
import com.pablohorst.booking.api.data.message.response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;

public class ExceptionHandlerUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerUtil.class);

    private ExceptionHandlerUtil() {
    }

    public static StatusCode mapCommonStatusCode(Throwable exception) {
        if (exception instanceof HttpMessageNotReadableException) {
            return CommonStatusCode.MISSING_BODY;
        } else if (exception instanceof MethodArgumentNotValidException
                || exception instanceof ConstraintViolationException) {
            return CommonStatusCode.INVALID_REQUEST;
        } else if (exception instanceof HttpMediaTypeNotSupportedException) {
            return CommonStatusCode.UNSUPPORTED_MEDIA_TYPE;
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            return CommonStatusCode.METHOD_NOT_ALLOWED;
        } else {
            logger.error("Unhandled Exception: ", exception);
            return CommonStatusCode.UNHANDLED_SERVER_ERROR;
        }
    }

    public static BaseResponse createBaseResponse(StatusCode statusCode) {
        return BaseResponse.builder().status(
                Status.builder()
                        .code(statusCode)
                        .build()
                ).build();
    }
}

