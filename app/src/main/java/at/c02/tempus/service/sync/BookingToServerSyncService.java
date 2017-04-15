package at.c02.tempus.service.sync;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.c02.tempus.api.api.BookingApi;
import at.c02.tempus.api.model.Booking;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.repository.BookingRepository;
import at.c02.tempus.service.EmployeeService;
import at.c02.tempus.service.event.BookingsChangedEvent;
import at.c02.tempus.service.mapping.BookingMapping;
import at.c02.tempus.service.sync.status.ItemChange;
import at.c02.tempus.service.sync.status.SyncResult;
import at.c02.tempus.service.sync.status.SyncStatusFinder;
import at.c02.tempus.utils.CollectionUtils;
import io.reactivex.Observable;

/**
 * Created by Daniel Hartl on 15.04.2017.
 */

@Singleton
public class BookingToServerSyncService extends AbstractSyncService<BookingEntity> {
    @Inject
    protected BookingApi bookingApi;
    @Inject
    protected BookingRepository bookingRepository;
    @Inject
    protected EventBus eventBus;

    @Inject
    public BookingToServerSyncService() {
        super(null /* not needed */);
    }

    @Override
    protected String getName() {
        return "BookingsToServer";
    }

    @Override
    protected Observable<List<BookingEntity>> loadLegacyItems() {
        return Observable.fromCallable(() -> bookingRepository.findModifiedEntries());
    }


    @Override
    protected void publishResults() {
        eventBus.post(new BookingsChangedEvent(bookingRepository.loadAllDeep()));
    }

    @Override
    protected List<SyncResult<BookingEntity>> createSyncStatus(List<BookingEntity> legacyItems) {
        return CollectionUtils.convertList(legacyItems, this::publishBooking);
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
        if (newBooking != null) {
            syncResult.setSource(BookingMapping.toBookingEntity(newBooking));
        }
        return syncResult;
    }

    @Override
    protected List<BookingEntity> loadLocalItems() {
        // not needed
        return null;
    }

    @Override
    protected void createOrUpdate(BookingEntity source, BookingEntity target) {
        // not needed
    }

    @Override
    protected void delete(BookingEntity target) {
        // not needed
    }
}
