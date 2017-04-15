package at.c02.tempus.service.sync;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.c02.tempus.api.api.EmployeeApi;
import at.c02.tempus.db.entity.EmployeeEntity;
import at.c02.tempus.db.repository.EmployeeRepository;
import at.c02.tempus.service.EmployeeService;
import at.c02.tempus.service.event.EmployeeChangedEvent;
import at.c02.tempus.service.mapping.EmployeeMapping;
import at.c02.tempus.service.sync.status.SyncStatusFinder;
import at.c02.tempus.service.sync.status.UpdateDetectorFactory;
import io.reactivex.Observable;

/**
 * Created by Daniel Hartl on 15.04.2017.
 */

@Singleton
public class EmployeeSyncService extends AbstractSyncService<EmployeeEntity> {

    @Inject
    protected EmployeeApi employeeApi;
    @Inject
    protected EmployeeRepository employeeRepository;
    @Inject
    protected EventBus eventBus;

    @Inject
    public EmployeeSyncService() {
        super(new SyncStatusFinder<>(EmployeeEntity::getExternalId,
                UpdateDetectorFactory.create(EmployeeEntity::getFirstName,
                        EmployeeEntity::getLastName)));
    }

    @Override
    protected String getName() {
        return "Employee";
    }

    @Override
    protected Observable<List<EmployeeEntity>> loadLegacyItems() {
        return employeeApi.findEmployeeByUserName(EmployeeService.CURRENT_USER)
                .map(EmployeeMapping::toEmployeeEntity)
                .map(Collections::singletonList);
    }

    @Override
    protected List<EmployeeEntity> loadLocalItems() {
        return employeeRepository.findAll();
    }

    @Override
    protected void publishResults() {
        eventBus.post(new EmployeeChangedEvent(
                employeeRepository.findByUserName(EmployeeService.CURRENT_USER)));
    }

    @Override
    protected void createOrUpdate(EmployeeEntity source, EmployeeEntity target) {
        if (target != null) {
            source.setId(target.getId());
        }
        employeeRepository.createOrUpdate(source);
    }

    @Override
    protected void delete(EmployeeEntity target) {
        employeeRepository.delete(target);
    }
}
