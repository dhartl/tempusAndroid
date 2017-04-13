package at.c02.tempus.service;

import at.c02.tempus.api.api.BookingApi;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.entity.EntityStatus;
import at.c02.tempus.db.repository.BookingRepository;

/**
 * Created by Daniel on 09.04.2017.
 */

public class BookingService {
    private BookingApi bookingApi;
    private BookingRepository bookingRepository;

    public BookingService(BookingApi bookingApi, BookingRepository bookingRepository) {
        this.bookingApi = bookingApi;
        this.bookingRepository = bookingRepository;
    }

    public BookingEntity createOrUpdateBooking(BookingEntity booking) {
        if(booking.getId() == null) {
            booking.setSyncStatus(EntityStatus.NEW);
        } else {
            booking.setSyncStatus(EntityStatus.MODIFIED);
        }
        return bookingRepository.createOrUpdate(booking);
    }

    public void deleteBooking(BookingEntity booking) {
        if(booking.getExternalId() == null) {
            bookingRepository.delete(booking);
        }else {
            booking.setSyncStatus(EntityStatus.DELETED);
            bookingRepository.createOrUpdate(booking);
        }
    }

    public void validateBooking(BookingEntity model) throws RuntimeException{
        if(model.getProjectId() == null) {
            throw new RuntimeException("Es muss ein Projekt ausgewählt werden!");
        }
        if(model.getBeginDate() == null) {
            throw new RuntimeException("Es muss ein Startdatum ausgewählt werden!");
        }
        if(model.getEndDate() == null) {
            throw new RuntimeException("Es muss ein Enddatum ausgewählt werden!");
        }
        if(!model.getEndDate().after(model.getBeginDate())) {
            throw new RuntimeException("Das Enddaum muss nach dem Startdatum sein!");
        }
    }
}
