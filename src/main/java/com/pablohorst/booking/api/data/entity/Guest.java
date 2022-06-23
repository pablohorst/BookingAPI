package com.pablohorst.booking.api.data.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * The guest entity that includes the main necessary information about the guest along with some optional data
 *
 * @author Pablo Horst
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long id;

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    // Guest Passport Number (must be unique)
    @NotNull
    private String passport;
    @NotNull
    private String nationality;

    // Optional Guest profession
    private String profession;

    // Boolean flag intended for logical deletion
    @Builder.Default
    @NotNull
    private boolean active = true;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "guest")
    private List<Reservation> reservation;
}
