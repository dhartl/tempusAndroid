package at.c02.tempus.service;

import android.text.format.DateUtils;
import android.util.Log;

import com.fernandocejas.arrow.optional.Optional;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import at.c02.tempus.api.api.BookingApi;
import at.c02.tempus.api.model.Booking;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.entity.EmployeeEntity;
import at.c02.tempus.db.entity.EntityStatus;
import at.c02.tempus.db.entity.ProjectEntity;
import at.c02.tempus.db.repository.BookingRepository;
import at.c02.tempus.service.event.BookingChangedEvent;
import at.c02.tempus.service.event.BookingsChangedEvent;
import at.c02.tempus.service.mapping.BookingMapping;
import at.c02.tempus.service.mapping.MappingUtils;
import at.c02.tempus.service.sync.ItemChange;
import at.c02.tempus.service.sync.SyncResult;
import at.c02.tempus.service.sync.SyncStatusFinder;
import io.reactivex.Observable;

/**
 * Created by Daniel on 09.04.2017.
 */

public class BookingService {
    private static final String TAG = "BookingService";

    private BookingApi bookingApi;
    private BookingRepository bookingRepository;
    private EventBus eventBus;
    private EmployeeService employeeService;
    private ProjectService projectService;

    private SyncStatusFinder<BookingEntity> syncStatusFinder = new SyncStatusFinder<>(item -> item.getExternalId(),
            (source, target) -> !(Objects.equals(source.getEmployeeId(), target.getEmployee()) &&
                    Objects.equals(source.getProjectId(), target.getProjectId()) &&
                    Objects.equals(source.getBeginDate(), target.getBeginDate()) &&
                    Objects.equals(source.getEndDate(), target.getEndDate())));

    public BookingService(BookingApi bookingApi,
                          BookingRepository bookingRepository,
                          EventBus eventBus,
                          EmployeeService employeeService,
                          ProjectService projectService) {
        this.bookingApi = bookingApi;
        this.bookingRepository = bookingRepository;
        this.eventBus = eventBus;
        this.employeeService = employeeService;
        this.projectService = projectService;
    }

    public BookingEntity createOrUpdateBooking(BookingEntity booking) {
        if (booking.getId() == null) {
            booking.setSyncStatus(EntityStatus.NEW);
        } else {
            booking.setSyncStatus(EntityStatus.MODIFIED);
        }
        BookingEntity newBooking = bookingRepository.createOrUpdate(booking);
        eventBus.post(new BookingChangedEvent(newBooking));
        return newBooking;
    }

    public void deleteBooking(BookingEntity booking) {
        if (booking.getExternalId() == null) {
            bookingRepository.delete(booking);
        } else {
            booking.setSyncStatus(EntityStatus.DELETED);
            bookingRepository.createOrUpdate(booking);
        }
        eventBus.post(new BookingChangedEvent(booking));
    }

    public void validateBooking(BookingEntity model) throws RuntimeException {
        if (model.getProjectId() == null) {
            throw new RuntimeException("Es muss ein Projekt ausgewählt werden!");
        }
        if (model.getBeginDate() == null) {
            throw new RuntimeException("Es muss ein Startdatum ausgewählt werden!");
        }
        if (model.getEndDate() == null) {
            throw new RuntimeException("Es muss ein Enddatum ausgewählt werden!");
        }
        if (!model.getEndDate().after(model.getBeginDate())) {
            throw new RuntimeException("Das Enddaum muss nach dem Startdatum sein!");
        }
        //TODO: überschneidende Buchungszeiten
    }

    public Observable<List<BookingEntity>> getBookings() {
        return Observable.fromCallable(() -> bookingRepository.loadBookings());
    }

    public Observable<Optional<BookingEntity>> getLastBooking() {
        return Observable.fromCallable(() -> {
            Optional<BookingEntity> lastBooking = Optional.fromNullable(bookingRepository.findLastBooking());
            Log.d(TAG, lastBooking.toString());
            return lastBooking;
        });
    }

    public Observable<BookingEntity> createNewBooking() {
        Observable<EmployeeEntity> currentEmployee = employeeService.getCurrentEmployee();
        Observable<Optional<BookingEntity>> lastBooking = getLastBooking();

        return Observable.zip(currentEmployee, lastBooking,
                (employee, previousBooking) -> {
                    BookingEntity newBooking = new BookingEntity();
                    newBooking.setEmployee(employee);
                    newBooking.setSyncStatus(EntityStatus.UNKNOWN);
                    Date now = new Date();
                    Date startDate = null;
                    ProjectEntity project = null;
                    if (previousBooking.isPresent()) {
                        project = previousBooking.get().getProject();
                        Date previousEndDate = previousBooking.get().getEndDate();
                        if (DateUtils.isToday(previousEndDate.getTime())) {
                            startDate = previousEndDate;
                        }
                    }
                    if (project == null) {
                        project = projectService.getDefaultProject().singleElement().blockingGet().orNull();
                    }
                    newBooking.setProject(project);
                    if (startDate == null) {
                        startDate = at.c02.tempus.app.ui.utils.DateUtils.setTime(now, 8, 0);
                    }
                    newBooking.setBeginDate(startDate);

                    newBooking.setEndDate(null);
                    return newBooking;
                });
    }

    public Observable<Optional<BookingEntity>> getBookingById(Long id) {
        return Observable.fromCallable(() -> Optional.fromNullable(bookingRepository.findBookingById(id)));
    }

    public void synchronizeBookings() {
        Observable<Boolean> bookingsToServer = syncBookingsToServer();
        Observable<Boolean> bookingsFromServer = syncBookingsFromServer();

        Observable.concat(bookingsToServer, bookingsFromServer).subscribe(result -> {
                }, error -> Log.e(TAG, "Fehler bei der Synchronisation von Buchungen", error)
        );

    }

    private Observable<Boolean> syncBookingsFromServer() {
        Log.d(TAG, "Sending Bookings to Server");
        return bookingApi.findBookingsForEmployeeAndDate(
                //FIXME: Buchungssynchronisation darf erst nach Employee-Synchronisation laufen!
                MappingUtils.fromLong(employeeService.getCurrentEmployee().blockingFirst().getExternalId()),
                at.c02.tempus.app.ui.utils.DateUtils.formatQueryDate(
                        at.c02.tempus.app.ui.utils.DateUtils.getDateBefore(7, Calendar.DAY_OF_MONTH)),
                true
        ).map(this::mapBookingsToEntity)
                .map(sourceBookings -> {
                    List<BookingEntity> targetBookings = bookingRepository.findServerBookings();
                    Log.d(TAG, "Syncronisiere Bookings: " + targetBookings.size() + ", externe Bookings: "
                            + sourceBookings.size());

                    return syncStatusFinder.findSyncStatus(
                            sourceBookings,
                            targetBookings);
                }).map(syncResults -> {
                    boolean emitChangedEvent = false;
                    for (SyncResult<BookingEntity> syncResult : syncResults) {
                        try {
                            emitChangedEvent |= applySyncResult(syncResult);
                        } catch (Exception ex) {
                            Log.e(TAG, "Fehler bei der Synchronisation von Bookings; " + syncResult, ex);
                        }
                    }
                    Log.d(TAG, "Bookings Synchronisation abgeschlossen");
                    if (emitChangedEvent) {
                        eventBus.post(new BookingsChangedEvent(bookingRepository.loadBookings()));
                    }
                    return emitChangedEvent;
                });
    }

    private List<BookingEntity> mapBookingsToEntity(List<Booking> bookings) {
        List<BookingEntity> entities = new ArrayList<>();
        for (Booking booking : bookings) {
            entities.add(BookingMapping.toBookingEntity(booking));
        }
        return entities;
    }

    private Observable<Boolean> syncBookingsToServer() {
        return Observable.fromCallable(() -> bookingRepository.findModifiedEntries())
                .map(bookingEntities -> {
                    List<SyncResult> results = new ArrayList<>();
                    for (BookingEntity booking : bookingEntities) {
                        results.add(publishBooking(booking));
                    }
                    return results;
                })
                .map(syncResults -> {
                    boolean emitChangedEvent = false;
                    for (SyncResult<BookingEntity> syncResult : syncResults) {
                        try {
                            emitChangedEvent |= applySyncResult(syncResult);
                        } catch (Exception ex) {
                            Log.e(TAG, "Fehler beim Publishen von Bookings; " + syncResult, ex);
                        }
                    }
                    Log.d(TAG, "Bookings Publishing abgeschlossen");
                    if (emitChangedEvent) {
                        eventBus.post(new BookingsChangedEvent(bookingRepository.loadBookings()));
                    }
                    return emitChangedEvent;
                });
    }

    private SyncResult<BookingEntity> publishBooking(BookingEntity bookingEntity) {
        SyncResult<BookingEntity> syncResult = new SyncResult<>();
        syncResult.setTarget(bookingEntity);
        Booking booking = BookingMapping.fromBookingEntity(bookingEntity);
        Booking newBooking = null;
        switch (bookingEntity.getSyncStatus()) {
            case NEW:
                newBooking = bookingApi.createBooking(booking).blockingFirst();
                syncResult.setItemChange(ItemChange.UPDATED);
                break;
            case MODIFIED:
                newBooking = bookingApi.updateBooking(booking).blockingFirst();
                syncResult.setItemChange(ItemChange.UPDATED);
                break;
            case DELETED:
                bookingApi.deleteBooking(booking.getBookingId()).blockingAwait();
                syncResult.setItemChange(ItemChange.DELETED);
                break;
            case SYNCED:
            case UNKNOWN:
                // Keine Aktion notwendig
                syncResult.setItemChange(ItemChange.NOT_CHANGED);
                break;
        }
        syncResult.setSource(BookingMapping.toBookingEntity(newBooking));

        return syncResult;
    }

    private boolean applySyncResult(SyncResult<BookingEntity> syncResult) {
        boolean changed = false;
        BookingEntity source = syncResult.getSource();
        BookingEntity target = syncResult.getTarget();
        switch (syncResult.getItemChange()) {
            case CREATED:
            case UPDATED: {
                changed = true;
                if (target != null) {
                    source.setId(target.getId());
                    source.setSyncStatus(EntityStatus.SYNCED);
                }
                bookingRepository.createOrUpdate(source);
                break;
            }
            case DELETED: {
                changed = true;
                bookingRepository.delete(target);
                break;
            }
            case NOT_CHANGED:
                //nichts tun
                break;
        }
        return changed;
    }
}
