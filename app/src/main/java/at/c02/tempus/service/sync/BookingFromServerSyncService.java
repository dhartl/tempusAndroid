package at.c02.tempus.service.sync;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.c02.tempus.api.api.BookingApi;
import at.c02.tempus.api.model.Booking;
import at.c02.tempus.api.model.Employee;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.entity.EmployeeEntity;
import at.c02.tempus.db.entity.EntityStatus;
import at.c02.tempus.db.repository.BookingRepository;
import at.c02.tempus.service.EmployeeService;
import at.c02.tempus.service.ProjectService;
import at.c02.tempus.service.event.BookingsChangedEvent;
import at.c02.tempus.service.mapping.BookingMapping;
import at.c02.tempus.service.mapping.MappingUtils;
import at.c02.tempus.service.sync.status.SyncStatusFinder;
import at.c02.tempus.service.sync.status.UpdateDetectorFactory;
import at.c02.tempus.utils.CollectionUtils;
import at.c02.tempus.utils.DateUtils;
import io.reactivex.Observable;

/**
 * Created by Daniel Hartl on 15.04.2017.
 */

@Singleton
public class BookingFromServerSyncService extends AbstractSyncService<BookingEntity> {

    @Inject
    protected BookingApi bookingApi;
    @Inject
    protected BookingRepository bookingRepository;
    @Inject
    protected EventBus eventBus;
    @Inject
    protected EmployeeService employeeService;

    @Inject
    public BookingFromServerSyncService() {
        super(new SyncStatusFinder<>(item -> item.getExternalId(),
                UpdateDetectorFactory.create(BookingEntity::getEmployeeId,
                        BookingEntity::getProjectId,
                        BookingEntity::getBeginDate,
                        BookingEntity::getEndDate)));
    }

    @Override
    protected String getName() {
        return "BookingsFromServer";
    }

    @Override
    protected Observable<List<BookingEntity>> loadLegacyItems() {
        return bookingApi.findBookingsForEmployeeAndDate(
                employeeService.getCurrentEmployee()
                        .map(EmployeeEntity::getExternalId)
                        .map(MappingUtils::fromLong)
                        .blockingFirst(-1),
                DateUtils.formatQueryDate(
                        DateUtils.getDateBefore(7, Calendar.DAY_OF_MONTH)),
                true
        ).map(legacyBookings -> CollectionUtils.convertList(legacyBookings, BookingMapping::toBookingEntity));
    }

    @Override
    protected List<BookingEntity> loadLocalItems() {
        return bookingRepository.findServerBookings();
    }

    @Override
    protected void publishResults() {
        eventBus.post(new BookingsChangedEvent(bookingRepository.loadAllDeep()));
    }

    @Override
    protected void createOrUpdate(BookingEntity source, BookingEntity target) {
        if (target != null) {
            source.setId(target.getId());
            source.setSyncStatus(EntityStatus.SYNCED);
        }
        bookingRepository.createOrUpdate(source);
    }

    @Override
    protected void delete(BookingEntity target) {
        bookingRepository.delete(target);
    }
}
