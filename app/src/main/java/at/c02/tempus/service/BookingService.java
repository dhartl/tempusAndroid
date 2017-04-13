package at.c02.tempus.service;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import at.c02.tempus.api.api.BookingApi;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.entity.EntityStatus;
import at.c02.tempus.db.repository.BookingRepository;
import at.c02.tempus.service.event.BookingChangedEvent;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Daniel on 09.04.2017.
 */

public class BookingService {
    private BookingApi bookingApi;
    private BookingRepository bookingRepository;
    private EventBus eventBus;

    public BookingService(BookingApi bookingApi, BookingRepository bookingRepository, EventBus eventBus) {
        this.bookingApi = bookingApi;
        this.bookingRepository = bookingRepository;
        this.eventBus = eventBus;
    }

    public BookingEntity createOrUpdateBooking(BookingEntity booking) {
        if(booking.getId() == null) {
            booking.setSyncStatus(EntityStatus.NEW);
        } else {
            booking.setSyncStatus(EntityStatus.MODIFIED);
        }
        BookingEntity newBooking = bookingRepository.createOrUpdate(booking);
        eventBus.post(new BookingChangedEvent(newBooking));
        return newBooking;
    }

    public void deleteBooking(BookingEntity booking) {
        if(booking.getExternalId() == null) {
            bookingRepository.delete(booking);
        }else {
            booking.setSyncStatus(EntityStatus.DELETED);
            bookingRepository.createOrUpdate(booking);
        }
        eventBus.post(new BookingChangedEvent(booking));
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
        //TODO: überschneidende Buchungszeiten
    }

    public Observable<List<BookingEntity>> getBookings() {
        return Observable.fromCallable(()-> bookingRepository.loadBookings());
    }
}
