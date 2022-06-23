package com.pablohorst.booking.api.service;

import com.pablohorst.booking.api.data.model.Guest;

import java.util.List;

/**
 * @author Pablo Horst
 */
public interface IGuestService {
    Guest createGuest(Guest guest);

    List<Guest> getGuestList();

    Guest getGuestById(long id);

    Guest updateGuest(long newGuestId, Guest newGuest);

    Guest deleteById(long id);
}
