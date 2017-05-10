package at.c02.tempus.service.sync;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import at.c02.tempus.api.api.BookingApi;
import at.c02.tempus.api.model.Booking;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.entity.EmployeeEntity;
import at.c02.tempus.db.entity.EntityStatus;
import at.c02.tempus.db.entity.ProjectEntity;
import at.c02.tempus.db.repository.BookingRepository;
import at.c02.tempus.db.repository.ProjectRepository;
import at.c02.tempus.service.EmployeeService;
import at.c02.tempus.service.ProjectService;
import at.c02.tempus.service.event.BookingsChangedEvent;
import at.c02.tempus.service.mapping.BookingMapping;
import at.c02.tempus.service.sync.status.SyncStatusFinder;
import at.c02.tempus.service.sync.status.UpdateDetectorFactory;

/**
 * Created by Daniel Hartl on 16.04.2017.
 */

public abstract class AbstractBookingSyncService extends AbstractSyncService<BookingEntity> {

    @Inject
    protected BookingApi bookingApi;
    @Inject
    protected BookingRepository bookingRepository;
    @Inject
    protected EmployeeService employeeService;
    @Inject
    protected ProjectService projectService;
    @Inject
    protected EventBus eventBus;

    public AbstractBookingSyncService() {
        super(new SyncStatusFinder<>(item -> item.getExternalId(),
                UpdateDetectorFactory.create(BookingEntity::getEmployeeId,
                        BookingEntity::getProjectId,
                        BookingEntity::getBeginDate,
                        BookingEntity::getEndDate)));
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

    protected BookingEntity mapBookingToEntity(Booking booking) {
        return BookingMapping.toBookingEntity(booking,
                findEmployeeByExternalId(Long.valueOf(booking.getEmployeeId())),
                findProjectByExternalId(Long.valueOf(booking.getProjectId())));
    }

    protected ProjectEntity findProjectByExternalId(Long externalProjectId) {
        return projectService.findProjectByExternalId(externalProjectId).blockingFirst();
    }

    protected EmployeeEntity findEmployeeByExternalId(Long externalEmployeeId) {
        return employeeService.findEmployeeByExternalId(externalEmployeeId).blockingFirst();
    }
}
