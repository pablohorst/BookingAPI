package com.pablohorst.booking.api.service;

import com.pablohorst.booking.api.data.entity.Reservation;
import com.pablohorst.booking.api.exception.*;
import com.pablohorst.booking.api.repository.GuestRepository;
import com.pablohorst.booking.api.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Reservation Service that contains all the logic for the Reservation operations
 *
 * @author Pablo Horst
 */
@Service
public class ReservationService implements IReservationService {
    private final GuestRepository guestRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(GuestRepository guestRepository, ReservationRepository reservationRepository) {
        this.guestRepository = guestRepository;
        this.reservationRepository = reservationRepository;
    }

    /**
     * Method to validate a reservation for creation and update operations
     *
     * @param reservation
     * @param isCreation  boolean to indicate that the Reservation being validated is a new one
     */
    // TODO this can be moved to a validation class
    public void validateReservation(Reservation reservation, boolean isCreation) {
        // Validate here that:
        // 1 - The checkInDate is not before bookingDate.
        // All reservations start at least the next day of booking.
        if (reservation.getCheckInDate().before(Timestamp.valueOf(reservation.getBookingDate()))) {
            throw new ReservationCheckInBeforeTodayException();
        }

        // 2 - The checkInDate is not after checkOutDate
        if (reservation.getCheckInDate().after(reservation.getCheckOutDate())) {
            throw new ReservationCheckInAfterCheckOutException();
        }

        // 3 - The reservation is no longer than 3 days
        TimeUnit time = TimeUnit.DAYS;
        long difference = time.convert(
                reservation.getCheckOutDate().getTime() - reservation.getCheckInDate().getTime(),
                TimeUnit.MILLISECONDS);

        //// Adding 1 to the difference due to the definition of the day for the project.
        if ((difference + 1) > 3) {
            throw new ReservationTooLongException();
        }

        // 4 - The room can’t be reserved more than 30 days in advance
        difference = time.convert(
                reservation.getCheckInDate().getTime() - Timestamp.valueOf(reservation.getBookingDate()).getTime(),
                TimeUnit.MILLISECONDS);

        if ((difference) > 30) {
            throw new ReservationCheckInTooInAdvanceException();
        }

        // 5 - The guest exists
        if (!guestRepository.findByIdAndActive(reservation.getGuest().getId(), true).isPresent()) {
            throw new GuestNotFoundException();
        }

        // 6 - The room is not reserved for the provided day
        if ((isCreation &&
                reservationRepository.findByGivenDates(
                                reservation.getCheckInDate(),
                                reservation.getCheckOutDate())
                        .isPresent())
                || (!isCreation &&
                reservationRepository.findByGivenDatesExceptingId(
                                reservation.getCheckInDate(),
                                reservation.getCheckOutDate(),
                                reservation.getId())
                        .isPresent())) {
            throw new ReservationRoomNotAvailableException();
        }
    }

    public Reservation createReservation(Reservation reservation) {
        // Set Booking Date
        reservation.setBookingDate(LocalDateTime.now());

        // Validate Reservation before saving
        validateReservation(reservation, true);

        return reservationRepository.save(reservation);
    }


    public List<Reservation> getReservationList() {
        return reservationRepository.findAllByActive(true);
    }

    public Reservation getReservationById(long id) {
        return reservationRepository.findByIdAndActive(id, true).orElseThrow(ReservationNotFoundException::new);
    }

    public Reservation updateReservation(long newReservationId, Reservation newReservation) {
        return reservationRepository.findByIdAndActive(newReservationId, true)
                // Note: this could be refactored to a single DB query instead of doing 2 queries. It depends on the
                // scope, requirements and where we want to put the logic

                // Updating a reservation that was found
                .map(foundReservation -> {
                    // Setting bookingDate and Id for validation purposes
                    newReservation.setBookingDate(foundReservation.getBookingDate());
                    newReservation.setId(foundReservation.getId());

                    // Then validate reservation update
                    validateReservation(newReservation, false);

                    // Update only allowed fields
                    foundReservation.setCheckInDate(newReservation.getCheckInDate());
                    foundReservation.setCheckOutDate(newReservation.getCheckOutDate());
                    foundReservation.setGuest(newReservation.getGuest());

                    // Save entity
                    return this.reservationRepository.save(foundReservation);
                })
                // Reservation Not Found
                .orElseThrow(ReservationNotFoundException::new);
    }

    public Reservation deleteById(long id) {
        // Get the reservation from the database
        Reservation reservation = reservationRepository.findByIdAndActive(id, true)
                .orElseThrow(ReservationNotFoundException::new);

        // Set the flag for logical removal
        reservation.setActive(false);

        // Save and return the deleted reservation as an echo
        return reservationRepository.save(reservation);
    }
}
