package at.c02.tempus.service;

import android.text.format.DateUtils;
import android.util.Log;

import com.fernandocejas.arrow.optional.Optional;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.entity.EmployeeEntity;
import at.c02.tempus.db.entity.EntityStatus;
import at.c02.tempus.db.entity.ProjectEntity;
import at.c02.tempus.db.repository.BookingRepository;
import at.c02.tempus.service.event.BookingChangedEvent;
import io.reactivex.Observable;

/**
 * Created by Daniel on 09.04.2017.
 */

@Singleton
public class BookingService {
    private static final String TAG = "BookingService";

    @Inject
    protected BookingRepository bookingRepository;

    @Inject
    protected EventBus eventBus;

    @Inject
    protected EmployeeService employeeService;

    @Inject
    protected ProjectService projectService;

    @Inject
    public BookingService() {
    }

    public BookingEntity createOrUpdateBooking(BookingEntity booking) {
        if (booking.getId() == null) {
            booking.setSyncStatus(EntityStatus.NEW);
        } else {
            booking.setSyncStatus(EntityStatus.MODIFIED);
        }
        BookingEntity newBooking = bookingRepository.createOrUpdate(booking);
        eventBus.post(new BookingChangedEvent(newBooking));
        return newBooking;
    }

    public void deleteBooking(BookingEntity booking) {
        if (booking.getExternalId() == null) {
            bookingRepository.delete(booking);
        } else {
            booking.setSyncStatus(EntityStatus.DELETED);
            bookingRepository.createOrUpdate(booking);
        }
        eventBus.post(new BookingChangedEvent(booking));
    }

    public void validateBooking(BookingEntity model) throws RuntimeException {
        if (model.getProjectId() == null) {
            throw new RuntimeException("Es muss ein Projekt ausgewählt werden!");
        }
        if (model.getBeginDate() == null) {
            throw new RuntimeException("Es muss ein Startdatum ausgewählt werden!");
        }
        if (model.getEndDate() == null) {
            throw new RuntimeException("Es muss ein Enddatum ausgewählt werden!");
        }
        if (!model.getEndDate().after(model.getBeginDate())) {
            throw new RuntimeException("Das Enddaum muss nach dem Startdatum sein!");
        }
        //TODO: überschneidende Buchungszeiten
    }

    public Observable<List<BookingEntity>> getBookings() {
        return Observable.fromCallable(() -> bookingRepository.loadAllDeep());
    }

    public Observable<Optional<BookingEntity>> getLastBooking() {
        return Observable.fromCallable(() -> {
            Optional<BookingEntity> lastBooking = Optional.fromNullable(bookingRepository.findLastBooking());
            Log.d(TAG, lastBooking.toString());
            return lastBooking;
        });
    }

    public Observable<BookingEntity> createNewBooking() {
        Observable<Optional<EmployeeEntity>> currentEmployee = employeeService.getCurrentEmployee();
        Observable<Optional<BookingEntity>> lastBooking = getLastBooking();

        return Observable.zip(currentEmployee, lastBooking,
                (employee, previousBooking) -> {
                    BookingEntity newBooking = new BookingEntity();
                    newBooking.setEmployee(employee.get());
                    newBooking.setSyncStatus(EntityStatus.UNKNOWN);
                    Date now = new Date();
                    Date startDate = null;
                    ProjectEntity project = null;
                    if (previousBooking.isPresent()) {
                        project = previousBooking.get().getProject();
                        Date previousEndDate = previousBooking.get().getEndDate();
                        if (DateUtils.isToday(previousEndDate.getTime())) {
                            startDate = previousEndDate;
                        }
                    }
                    if (project == null) {
                        project = projectService.getDefaultProject().singleElement().blockingGet().orNull();
                    }
                    newBooking.setProject(project);
                    if (startDate == null) {
                        startDate = at.c02.tempus.utils.DateUtils.setTime(now, 8, 0);
                    }
                    newBooking.setBeginDate(startDate);

                    newBooking.setEndDate(null);
                    return newBooking;
                });
    }

    public Observable<Optional<BookingEntity>> getBookingById(Long id) {
        return Observable.fromCallable(() -> Optional.fromNullable(bookingRepository.findBookingById(id)));
    }


}
