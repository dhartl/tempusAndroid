package at.c02.tempus.service;

import com.fernandocejas.arrow.optional.Optional;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.c02.tempus.api.api.BookingApi;
import at.c02.tempus.api.model.Booking;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.entity.EmployeeEntity;
import at.c02.tempus.db.entity.ProjectEntity;
import at.c02.tempus.service.mapping.BookingMapping;
import at.c02.tempus.utils.CollectionUtils;
import at.c02.tempus.utils.DateUtils;
import io.reactivex.Observable;

/**
 * Created by Daniel Hartl on 12.05.2017.
 */
@Singleton
public class ReportService {

    @Inject
    protected BookingApi bookingApi;

    @Inject
    protected EmployeeService employeeService;

    @Inject
    protected ProjectService projectService;

    @Inject
    public ReportService() {
    }

    public Observable<List<BookingEntity>> findBookings(Date beginDate) {
        int employeeId = employeeService
                .getCurrentEmployee()
                .map(Optional::get)
                .map(EmployeeEntity::getExternalId)
                .blockingFirst()
                .intValue();
        String beginDateStr = DateUtils.formatQueryDate(beginDate);
        Boolean completed = true;
        return bookingApi.findBookingsForEmployeeAndDate(employeeId, beginDateStr, completed)
                .map(legacyBookings -> CollectionUtils.convertList(legacyBookings, this::mapBookingToEntity));
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
