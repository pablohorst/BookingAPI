package com.pablohorst.booking.api.repository;

import com.pablohorst.booking.api.data.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Pablo Horst
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Find all the reservations that are either logically deleted or not
    List<Reservation> findAllByActive(boolean active);

    Optional<Reservation> findByIdAndActive(long id, boolean active);

    Optional<Reservation> findByGuestIdAndActive(long guestId, boolean active);

    @Query(
            // First looking that the reservation is active
            "SELECT r FROM Reservation r WHERE r.active = TRUE" +
            // Then that the new reservation dates don't overlap other reservations
            " and (" +
                    "(r.checkInDate >= ?1 and r.checkOutDate <= ?2)" +
                    " or (r.checkInDate <= ?1 and r.checkOutDate >= ?1)" +
                    " or (r.checkInDate <= ?2 and r.checkOutDate >= ?2)" +
                ")")
    Optional<Reservation> findByGivenDates(Date checkInDate, Date checkOutDate);

    @Query(
            // First looking that the reservation is active
            "SELECT r FROM Reservation r WHERE r.active = TRUE and r.id != ?3" +
                    // Then that the new reservation dates don't overlap other reservations
                    " and (" +
                    "(r.checkInDate >= ?1 and r.checkOutDate <= ?2)" +
                    " or (r.checkInDate <= ?1 and r.checkOutDate >= ?1)" +
                    " or (r.checkInDate <= ?2 and r.checkOutDate >= ?2)" +
                    ")")
    Optional<Reservation> findByGivenDatesExceptingId(Date checkInDate, Date checkOutDate, long id);
}
