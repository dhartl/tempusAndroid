package at.c02.tempus.service;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.c02.tempus.api.api.EmployeeApi;
import at.c02.tempus.db.entity.EmployeeEntity;
import at.c02.tempus.db.repository.EmployeeRepository;
import at.c02.tempus.service.event.EmployeeChangedEvent;
import at.c02.tempus.service.mapping.EmployeeMapping;
import at.c02.tempus.service.sync.status.SyncResult;
import at.c02.tempus.service.sync.status.SyncStatusFinder;
import at.c02.tempus.service.sync.status.UpdateDetectorFactory;
import io.reactivex.Observable;

/**
 * Created by Daniel on 09.04.2017.
 */

@Singleton
public class EmployeeService {
    private static final String TAG = "EmployeeService";

    public static final String CURRENT_USER = "ckelley0";

    @Inject
    protected EmployeeRepository employeeRepository;

    @Inject
    public EmployeeService() {
    }

    public Observable<EmployeeEntity> getCurrentEmployee() {
        return Observable.fromCallable(
                () -> employeeRepository.findByUserName(CURRENT_USER)
        );
    }

}
