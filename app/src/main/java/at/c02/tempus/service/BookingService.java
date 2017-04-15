package at.c02.tempus.service;

import android.text.format.DateUtils;
import android.util.Log;

import com.fernandocejas.arrow.optional.Optional;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

import at.c02.tempus.api.api.BookingApi;
import at.c02.tempus.api.model.Booking;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.entity.EmployeeEntity;
import at.c02.tempus.db.entity.EntityStatus;
import at.c02.tempus.db.entity.ProjectEntity;
import at.c02.tempus.db.repository.BookingRepository;
import at.c02.tempus.service.event.BookingChangedEvent;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Daniel on 09.04.2017.
 */

public class BookingService {
    private static final String TAG = "BookingService";

    private BookingApi bookingApi;
    private BookingRepository bookingRepository;
    private EventBus eventBus;
    private EmployeeService employeeService;
    private ProjectService projectService;

    public BookingService(BookingApi bookingApi,
                          BookingRepository bookingRepository,
                          EventBus eventBus,
                          EmployeeService employeeService,
                          ProjectService projectService) {
        this.bookingApi = bookingApi;
        this.bookingRepository = bookingRepository;
        this.eventBus = eventBus;
        this.employeeService = employeeService;
        this.projectService = projectService;
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
        return Observable.fromCallable(() -> bookingRepository.loadBookings());
    }

    public Observable<Optional<BookingEntity>> getLastBooking() {
        return Observable.fromCallable(() -> {
            Optional<BookingEntity> lastBooking = Optional.fromNullable(bookingRepository.findLastBooking());
            Log.d(TAG, lastBooking.toString());
            return lastBooking;
        });
    }

    public Observable<BookingEntity> createNewBooking() {
        Observable<EmployeeEntity> currentEmployee = employeeService.getCurrentEmployee();
        Observable<Optional<BookingEntity>> lastBooking = getLastBooking();

        return Observable.zip(currentEmployee, lastBooking,
                (employee, previousBooking) -> {
                    BookingEntity newBooking = new BookingEntity();
                    newBooking.setEmployee(employee);
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
                    if(project == null) {
                        project = projectService.getDefaultProject().singleElement().blockingGet().orNull();
                    }
                    newBooking.setProject(project);
                    if (startDate == null) {
                        startDate = at.c02.tempus.app.ui.utils.DateUtils.setTime(now, 8, 0);
                    }
                    newBooking.setBeginDate(startDate);

                    newBooking.setEndDate(null);
                    return newBooking;
                });
    }
}
