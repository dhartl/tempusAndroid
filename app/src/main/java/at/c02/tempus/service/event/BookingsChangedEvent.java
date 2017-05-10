package at.c02.tempus.service.event;

import java.util.List;

import at.c02.tempus.db.entity.BookingEntity;

/**
 * Created by Daniel Hartl on 15.04.2017.
 */

public class BookingsChangedEvent {
    private List<BookingEntity> bookings;

    public BookingsChangedEvent(List<BookingEntity> bookingEntities) {
        bookings = bookingEntities;
    }

    public List<BookingEntity> getBookings() {
        return bookings;
    }
}
