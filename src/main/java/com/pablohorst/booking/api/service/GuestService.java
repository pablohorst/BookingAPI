package com.pablohorst.booking.api.service;

import com.pablohorst.booking.api.data.model.Guest;
import com.pablohorst.booking.api.exception.GuestCannotDeleteWithReservationException;
import com.pablohorst.booking.api.exception.GuestDuplicatedPassportException;
import com.pablohorst.booking.api.exception.GuestNotFoundException;
import com.pablohorst.booking.api.repository.GuestRepository;
import com.pablohorst.booking.api.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Guest Service that contains all the logic for the Guest operations
 *
 * @author Pablo Horst
 */
@Service
public class GuestService implements IGuestService {
    private final GuestRepository guestRepository;
    private final ReservationRepository reservationRepository;

    public GuestService(GuestRepository guestRepository, ReservationRepository reservationRepository) {
        this.guestRepository = guestRepository;
        this.reservationRepository = reservationRepository;
    }

    /**
     * Method to validate passport uniqueness
     *
     * @param guest
     */
    public void validatePassportUniqueness(Guest guest) {
        // Checking that there isn't another non-deleted guest with the same passport number.
        // This uniqueness is enforced here instead of using a uniqueness constraint to avoid
        // identity ID advancing unnecessarily
        if (guestRepository.findByPassportAndActive(guest.getPassport(), true).isPresent()) {
            throw new GuestDuplicatedPassportException();
        }
    }

    /**
     * Method to validate a guest being updated
     *
     * @param oldGuest
     * @param newGuest
     */
    public void validateGuestUpdate(Guest oldGuest, Guest newGuest) {
        // If passport is trying to be updated
        if (!oldGuest.getPassport().equals(newGuest.getPassport())) {
            // Validate passport uniqueness
            validatePassportUniqueness(newGuest);
        }
    }

    @Transactional
    public Guest createGuest(Guest guest) {
        // Validate passport uniqueness before saving
        validatePassportUniqueness(guest);

        return guestRepository.save(guest);
    }

    public List<Guest> getGuestList() {
        return guestRepository.findAllByActive(true);
    }

    public Guest getGuestById(long id) {
        return guestRepository.findByIdAndActive(id, true).orElseThrow(GuestNotFoundException::new);
    }

    @Transactional
    public Guest updateGuest(long newGuestId, Guest newGuest) {
        return guestRepository.findByIdAndActive(newGuestId, true)
                // Note: this could be refactored to a single DB query instead of doing 2 queries. It depends on the
                // scope, requirements and where we want to put the logic

                // Updating a guest that was found
                .map(foundGuest -> {
                    // Then validate guest update
                    validateGuestUpdate(foundGuest, newGuest);

                    // Update fields
                    foundGuest.setFirstName(newGuest.getFirstName());
                    foundGuest.setLastName(newGuest.getLastName());
                    foundGuest.setNationality(newGuest.getNationality());
                    foundGuest.setPassport(newGuest.getPassport());
                    foundGuest.setProfession(newGuest.getProfession());

                    // Save entity
                    return this.guestRepository.save(foundGuest);
                })
                // Guest Not Found
                .orElseThrow(GuestNotFoundException::new);
    }

    @Transactional
    public Guest deleteById(long id) {
        // Get the guest from the database
        Guest guest = guestRepository.findByIdAndActive(id, true).orElseThrow(GuestNotFoundException::new);

        // Validate that the guest doesn't have a reservation
        if (reservationRepository.findByGuestIdAndActive(guest.getId(), true).isPresent()) {
            throw new GuestCannotDeleteWithReservationException();
        }

        // Set the flag for logical removal
        guest.setActive(false);

        // Save and return the deleted guest as an echo
        return guestRepository.save(guest);
    }
}
