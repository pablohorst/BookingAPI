package com.pablohorst.booking.api.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Guest Data Transfer Object for POST and PUT operations
 *
 * @author Pablo Horst
 */
@Data
@NoArgsConstructor
public class GuestPostDto implements Serializable {
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
