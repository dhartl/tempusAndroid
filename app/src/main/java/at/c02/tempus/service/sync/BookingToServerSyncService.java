package at.c02.tempus.service.sync;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.c02.tempus.api.model.Booking;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.service.mapping.BookingMapping;
import at.c02.tempus.service.sync.status.ItemChange;
import at.c02.tempus.service.sync.status.SyncResult;
import at.c02.tempus.utils.CollectionUtils;
import io.reactivex.Observable;

/**
 * Created by Daniel Hartl on 15.04.2017.
 */

@Singleton
public class BookingToServerSyncService extends AbstractBookingSyncService {

    @Inject
    public BookingToServerSyncService() {
    }

    @Override
    protected String getName() {
        return "BookingsToServer";
    }

    @Override
    protected Observable<List<BookingEntity>> loadLegacyItems() {
        return employeeService.getCurrentEmployee()
                .map(currentEmployee -> bookingRepository.findModifiedEntries(currentEmployee.get().getId()));
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
            syncResult.setSource(mapBookingToEntity(newBooking));
        }
        return syncResult;
    }

    @Override
    protected List<BookingEntity> loadLocalItems() {
        // not needed
        return null;
    }


}
