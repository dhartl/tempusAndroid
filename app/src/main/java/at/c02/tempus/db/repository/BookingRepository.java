package at.c02.tempus.db.repository;

import at.c02.tempus.db.entity.BookingEntityDao;

/**
 * Klasse für Queries auf Booking-Tabelle
 * Created by Daniel on 09.04.2017.
 */
public class BookingRepository {

    private BookingEntityDao dao;

    public BookingRepository(BookingEntityDao dao) {
        this.dao = dao;
    }
}
