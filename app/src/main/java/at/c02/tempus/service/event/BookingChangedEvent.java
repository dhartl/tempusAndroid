package at.c02.tempus.service.event;

import at.c02.tempus.db.entity.BookingEntity;

/**
 * Created by Daniel Hartl on 13.04.2017.
 */

public class BookingChangedEvent {
    private BookingEntity changedBooking;

    public BookingChangedEvent(BookingEntity changedBooking) {
        this.changedBooking = changedBooking;
    }

    public BookingEntity getChangedBooking() {
        return changedBooking;
    }
}
