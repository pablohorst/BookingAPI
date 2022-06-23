package com.pablohorst.booking.api.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Guest Data Transfer Object for GET operations and responses
 *
 * @author Pablo Horst
 */
@Data
@NoArgsConstructor
public class GuestGetDto implements Serializable {
    private long id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String passport;
    @NotNull
    private String nationality;
    private String profession;
}
