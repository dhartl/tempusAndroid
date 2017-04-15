package at.c02.tempus.service;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import at.c02.tempus.api.api.EmployeeApi;
import at.c02.tempus.db.entity.EmployeeEntity;
import at.c02.tempus.db.repository.EmployeeRepository;
import at.c02.tempus.service.event.EmployeeChangedEvent;
import at.c02.tempus.service.mapping.EmployeeMapping;
import at.c02.tempus.service.sync.SyncResult;
import at.c02.tempus.service.sync.SyncStatusFinder;
import at.c02.tempus.service.sync.UpdateDetectorFactory;
import io.reactivex.Observable;

/**
 * Created by Daniel on 09.04.2017.
 */

public class EmployeeService {
    private static final String TAG = "EmployeeService";

    private static final String CURRENT_USER = "hHUber";

    private EmployeeApi employeeApi;
    private EmployeeRepository employeeRepository;
    private EventBus eventBus;

    private SyncStatusFinder<EmployeeEntity> syncStatusFinder = new SyncStatusFinder<>(EmployeeEntity::getExternalId,
            UpdateDetectorFactory.create(EmployeeEntity::getFirstName,
                    EmployeeEntity::getLastName));

    public EmployeeService(EmployeeApi employeeApi, EmployeeRepository employeeRepository, EventBus eventBus) {
        this.employeeApi = employeeApi;
        this.employeeRepository = employeeRepository;
        this.eventBus = eventBus;
    }

    public void loadEmployee() {
        employeeApi.findEmployeeByUserName(CURRENT_USER).map(EmployeeMapping::toEmployeeEntity)
                .map(employee -> {
                    List<EmployeeEntity> targetEmployee = employeeRepository.findAll();
                    Log.d(TAG, "Syncronisiere Employee: " + targetEmployee.size() + ", externer Employee: "
                            + employee);

                    return syncStatusFinder.findSyncStatus(
                            Collections.singletonList(employee),
                            targetEmployee);
                })
                .subscribe(syncResults -> {
                    boolean emitChangedEvent = false;
                    for (SyncResult<EmployeeEntity> syncResult : syncResults) {
                        try {
                            emitChangedEvent |= applySyncResult(syncResult);
                        } catch (Exception ex) {
                            Log.e(TAG, "Fehler bei der Synchronisation von Employees; " + syncResult, ex);
                        }
                    }
                    Log.d(TAG, "Employee Synchronisation abgeschlossen");
                    if (emitChangedEvent) {
                        eventBus.post(new EmployeeChangedEvent(employeeRepository.findByUserName(CURRENT_USER)));
                    }
                }, throwable -> Log.e(TAG, "Fehler bei der Employeesynchronisation", throwable));
    }

    public Observable<EmployeeEntity> getCurrentEmployee() {
        return Observable.fromCallable(() -> {
            EmployeeEntity currentUser = employeeRepository.findByUserName(CURRENT_USER);
            Log.d(TAG, "current employee: " + currentUser);
            return currentUser;
        });
    }

    private boolean applySyncResult(SyncResult<EmployeeEntity> syncResult) {
        boolean changed = false;
        EmployeeEntity source = syncResult.getSource();
        EmployeeEntity target = syncResult.getTarget();
        switch (syncResult.getItemChange()) {
            case CREATED:
            case UPDATED: {
                changed = true;
                if (target != null) {
                    source.setId(target.getId());
                }
                employeeRepository.createOrUpdate(source);
                break;
            }
            case DELETED: {
                changed = true;
                employeeRepository.delete(target);
                break;
            }
            case NOT_CHANGED:
                //nichts tun
                break;
        }
        return changed;
    }
}
