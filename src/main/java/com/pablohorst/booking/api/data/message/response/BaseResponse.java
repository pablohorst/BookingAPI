package com.pablohorst.booking.api.data.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pablohorst.booking.api.data.CommonStatusCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base Response class
 * @author Pablo Horst
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    @Builder.Default
    private Status status = Status.fromStatusCode(CommonStatusCode.SUCCESS);
    private Object body;
}
