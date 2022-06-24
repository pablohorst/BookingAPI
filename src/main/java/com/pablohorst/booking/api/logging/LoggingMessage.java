package com.pablohorst.booking.api.logging;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablohorst.booking.api.util.LoggingUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/**
 * Logging message
 * @author Pablo Horst
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@Slf4j
public class LoggingMessage {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private String type;
    private String method;
    private String url;

    @JsonRawValue
    private String body;

    private Map<String, String> headers;
    private String statusCode;
    private String application;
    private String customerId;
    private String username;

    @JsonCreator
    public LoggingMessage(String type, String method, String url, String body, Map<String, String> headers, String statusCode, String application, String customerId, String username) {
        this.type = type;
        this.method = method;
        this.url = url;
        this.body = body;
        this.headers = headers;
        this.statusCode = statusCode;
        this.application = application;
        this.customerId = customerId;
        this.username = username;
    }

    @JsonGetter("body")
    public String serializeBody() {
        if (this.body == null) {
            return null;
        }

        // Check if body is already serialized into JSON
        if (this.isValidJson(this.body)) {
            return LoggingUtil.removeLineBreaks(this.body);
        } else {
            try {
                return objectMapper.writeValueAsString(this.body);
            } catch (JsonProcessingException e) {
                return LoggingUtil.removeLineBreaks(this.body);
            }
        }
    }

    private boolean isValidJson(String test) {
        try {
            objectMapper.readTree(test);
        } catch (IOException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public String toJson() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("Logger toJson conversion exception: {0}", e);
            return "";
        }
    }
}
