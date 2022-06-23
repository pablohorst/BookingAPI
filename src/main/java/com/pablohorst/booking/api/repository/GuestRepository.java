package com.pablohorst.booking.api.repository;

import com.pablohorst.booking.api.data.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Pablo Horst
 */
public interface GuestRepository extends JpaRepository<Guest, Long> {

    // Find all the guests that are either logically deleted or not
    List<Guest> findAllByActive(boolean active);

    Optional<Guest> findByIdAndActive(long id, boolean active);

    // Used to enforce passport uniqueness on non-deleted guests
    Optional<Guest> findByPassportAndActive(String passport, boolean active);
}
