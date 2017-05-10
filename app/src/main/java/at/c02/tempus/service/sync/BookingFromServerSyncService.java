package at.c02.tempus.service.sync;

import com.fernandocejas.arrow.optional.Optional;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.entity.EmployeeEntity;
import at.c02.tempus.service.mapping.MappingUtils;
import at.c02.tempus.utils.CollectionUtils;
import at.c02.tempus.utils.DateUtils;
import io.reactivex.Observable;

/**
 * Created by Daniel Hartl on 15.04.2017.
 */

@Singleton
public class BookingFromServerSyncService extends AbstractBookingSyncService {

    @Inject
    public BookingFromServerSyncService() {
    }

    @Override
    protected String getName() {
        return "BookingsFromServer";
    }

    @Override
    protected Observable<List<BookingEntity>> loadLegacyItems() {
        return bookingApi.findBookingsForEmployeeAndDate(
                employeeService.getCurrentEmployee()
                        .map(Optional::get)
                        .map(EmployeeEntity::getExternalId)
                        .map(MappingUtils::fromLong)
                        .blockingFirst(-1),
                DateUtils.formatQueryDate(
                        DateUtils.getDateBefore(7, Calendar.DAY_OF_MONTH)),
                true
        ).map(legacyBookings -> CollectionUtils.convertList(legacyBookings, this::mapBookingToEntity));
    }

    @Override
    protected List<BookingEntity> loadLocalItems() {
        return bookingRepository.findServerBookings();
    }

}
