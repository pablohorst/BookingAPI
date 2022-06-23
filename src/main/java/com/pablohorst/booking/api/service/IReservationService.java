package com.pablohorst.booking.api.service;

import com.pablohorst.booking.api.data.model.Reservation;

import java.util.List;

/**
 * @author Pablo Horst
 */
public interface IReservationService {
    Reservation createReservation(Reservation reservation);

    List<Reservation> getReservationList();

    Reservation getReservationById(long id);

    Reservation updateReservation(long newReservationId, Reservation newReservation);

    Reservation deleteById(long id);
}
