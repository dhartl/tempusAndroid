package at.c02.tempus.service.mapping;

import at.c02.tempus.api.model.Booking;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.entity.EntityStatus;

/**
 * Created by Daniel Hartl on 15.04.2017.
 */

public class BookingMapping {

    public static Booking fromBookingEntity(BookingEntity entity) {
        Booking booking = new Booking();
        booking.setBookingId(MappingUtils.fromLong(entity.getExternalId()));
        booking.setCompleted(entity.getBeginDate() != null && entity.getEndDate() != null);
        booking.setBeginDate(entity.getBeginDate());
        booking.setEndDate(entity.getEndDate());
        booking.setEmployeeId(MappingUtils.fromLong(entity.getEmployeeId()));
        booking.setProjectId(MappingUtils.fromLong(entity.getProjectId()));
        return booking;
    }

    public static BookingEntity toBookingEntity(Booking booking) {
        BookingEntity entity = new BookingEntity();
        entity.setExternalId(MappingUtils.fromInt(booking.getBookingId()));
        entity.setBeginDate(booking.getBeginDate());
        entity.setEndDate(booking.getEndDate());
        entity.setEmployeeId(MappingUtils.fromInt(booking.getEmployeeId()));
        entity.setProjectId(MappingUtils.fromInt(booking.getProjectId()));
        entity.setSyncStatus(EntityStatus.SYNCED);
        return entity;
    }
}
