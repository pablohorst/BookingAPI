package com.pablohorst.booking.api.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.pablohorst.booking.api.data.CommonStatusCode;
import com.pablohorst.booking.api.data.StatusCode;
import com.pablohorst.booking.api.data.message.response.BaseResponse;
import com.pablohorst.booking.api.data.message.response.ErrorDetails;
import com.pablohorst.booking.api.data.message.response.Status;
import com.pablohorst.booking.api.util.ExceptionHandlerUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.pablohorst.booking.api.util.ExceptionHandlerUtil.createBaseResponse;

/**
 * Global Exception Handler intended to centralize common exception handling using Springboot RestControllerAdvice
 *
 * @author Pablo Horst
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseBody
    @ExceptionHandler(BookingApiException.class)
    private ResponseEntity<BaseResponse> bookingApiExceptionHandler(BookingApiException ex) {
        return new ResponseEntity<>(BaseResponse.builder().status(
                Status.builder()
                        .code(ex.getStatusCode())
                        .description(ex.getStatusCode().getDescription())
                        .build()
        ).build(), ex.getStatusCode().getHttpStatus());
    }

    /**
     * Populates the list of errors provided in a ConstraintViolationException
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<ErrorDetails> body = new ArrayList<>();

        // Sometimes displays an empty error message which is a duplicate
        // ConstrainValidator contains full reference to a property, including called method and argument index
        // This logic removes method and argument and only adds property that would be exposed to a user
        ex.getConstraintViolations().stream()
                .filter(violation -> !violation.getMessage().isEmpty())
                .forEach(violation -> {
                    StringBuilder fieldNameExtractor = new StringBuilder();
                    violation.getPropertyPath().forEach(node -> {
                        if (node.getKind() == ElementKind.PROPERTY) {
                            fieldNameExtractor.append(node.getName()).append(".");
                        }
                    });

                    if (fieldNameExtractor.length() > 0) {
                        fieldNameExtractor.deleteCharAt(fieldNameExtractor.length() - 1);
                    } else {
                        return;
                    }

                    String fieldName = fieldNameExtractor.toString();

                    // Adding constraint violation details into the ErrorDetails object
                    body.add(ErrorDetails.builder()
                            .field(fieldName)
                            .message(violation.getMessage())
                            .build());
                });

        HttpHeaders httpHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        return handleExceptionInternal(ex, body, httpHeaders, httpStatus, request);
    }

    /**
     * Switches the Exception to the proper handler. The default ResponseEntityExceptionHandler is used first, then
     * individual exception workflows can be defined below that. Finally, the "catch all" handler is defined.
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        try {
            return handleException(ex, request);
        } catch (Exception e) {
            return createErrorResponse(ExceptionHandlerUtil.mapCommonStatusCode(e));
        }
    }

    public static ResponseEntity<Object> createErrorResponse(StatusCode statusCode) {
        return new ResponseEntity<>(
                createBaseResponse(statusCode),
                new HttpHeaders(),
                statusCode.getHttpStatus());
    }

    /**
     * Populates the list of errors provided in a MethodArgumentNotValidException
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ErrorDetails> body = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                body.add(ErrorDetails.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build()));

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders httpHeaders,
            HttpStatus httpStatus,
            WebRequest webRequest) {

        // Validation for Malformed JSON request such as missing comma or curly braces
        if (ex.getCause() instanceof JsonParseException || ex.getCause() instanceof JsonMappingException) {
            if (ex.getCause() instanceof UnrecognizedPropertyException) {
                // Unrecognized property or field found
                String propertyName = ((UnrecognizedPropertyException) ex.getCause()).getPropertyName();

                Status status = Status.builder()
                        .code(CommonStatusCode.INVALID_REQUEST)
                        .errors(Collections.singletonList(ErrorDetails.builder()
                                .field(propertyName)
                                .message("parameter not part of the request contract")
                                .build()))
                        .build();

                return new ResponseEntity<>(status, new HttpHeaders(), CommonStatusCode.INVALID_REQUEST.getHttpStatus());
            }

            return createErrorResponse(CommonStatusCode.INVALID_REQUEST);
        }

        return createErrorResponse(CommonStatusCode.MISSING_BODY);
    }

    /**
     * Builds the BaseResponse containing the Exception information
     *
     * @param ex
     * @param body    Null or a list of ErrorDetails objects populated in the Exception handler method for the specific exception
     * @param headers
     * @param status
     * @param request
     * @return response
     */
    @Override
    public ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        Status internalStatus = Status.builder()
                .code(mapCommonStatusCode(ex))
                .build();

        // Inject the Error Details list added on the overridden handlers to the status
        if (body instanceof List) {
            internalStatus.setErrors((List<ErrorDetails>) body);
        }

        BaseResponse response = BaseResponse.builder()
                .status(internalStatus)
                .build();

        return super.handleExceptionInternal(ex, response, headers, status, request);
    }

    /**
     * First try to map the CommonStatusCode then tries on {@link ExceptionHandlerUtil}
     */
    private StatusCode mapCommonStatusCode(Exception ex) {
        try {
            throw ex;
        } catch (Exception e) {
            return ExceptionHandlerUtil.mapCommonStatusCode(e);
        }
    }
}
