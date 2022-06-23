package com.pablohorst.booking.api.data.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pablohorst.booking.api.data.StatusCode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pablo Horst
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Status {
    private StatusCode code;
    private String description;
    List<ErrorDetails> errors;
    private String timestamp;


    // Builder Design Pattern with default timestamp behavior
    @Builder
    public Status(StatusCode code, String description, String timestamp, List<ErrorDetails> errors) {
        this.code = code;
        this.description = description;

        this.errors = errors;

        if (timestamp != null) {
            this.timestamp = timestamp;
        } else {
            this.timestamp = LocalDateTime.now().toString();
        }
    }

    public static Status fromStatusCode(StatusCode code) {
        return Status.builder().code(code).description(code.getDescription()).build();
    }
}
