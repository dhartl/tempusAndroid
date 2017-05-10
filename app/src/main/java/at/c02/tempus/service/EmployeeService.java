package at.c02.tempus.service;


import com.fernandocejas.arrow.optional.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.c02.tempus.auth.CurrentUserHolder;
import at.c02.tempus.db.entity.EmployeeEntity;
import at.c02.tempus.db.repository.EmployeeRepository;
import io.reactivex.Observable;

/**
 * Created by Daniel on 09.04.2017.
 */

@Singleton
public class EmployeeService {
    private static final String TAG = "EmployeeService";

    @Inject
    protected EmployeeRepository employeeRepository;

    @Inject
    protected CurrentUserHolder currentUserHolder;

    @Inject
    public EmployeeService() {
    }

    public Observable<Optional<EmployeeEntity>> getCurrentEmployee() {
        return Observable.fromCallable(
                () -> {
                    if (currentUserHolder.getCurrentUser() != null) {
                        return Optional.of(employeeRepository.findByUserName(currentUserHolder.getCurrentUser().getName()));
                    } else {
                        return Optional.absent();
                    }
                }
        );
    }

    public Observable<EmployeeEntity> findEmployeeByExternalId(Long externalEmployeeId) {
        return Observable.fromCallable(
                () -> employeeRepository.findByExternalId(externalEmployeeId)
        );
    }
}
