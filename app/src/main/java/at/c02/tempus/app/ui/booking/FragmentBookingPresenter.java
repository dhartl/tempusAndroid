package at.c02.tempus.app.ui.booking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import java.util.Date;
import at.c02.tempus.app.TempusApplication;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.entity.ProjectEntity;
import at.c02.tempus.service.BookingService;
import at.c02.tempus.service.ProjectService;
import at.c02.tempus.service.event.ProjectsChangedEvent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import nucleus.presenter.Presenter;
/**
 * Created by Daniel Hartl on 13.04.2017.
 * Modified by Philipp^2
 */

public class FragmentBookingPresenter extends Presenter<FragmentBooking> {

    private BookingEntity model = new BookingEntity();

    @Inject
    protected BookingService bookingService;

    @Inject
    protected ProjectService projectService;

    @Inject
    protected EventBus eventBus;

    private List<ProjectEntity> projects;

    //necessary?
    private Throwable error;

    public FragmentBookingPresenter() {
        TempusApplication.getApp().getApplicationComponents().inject(this);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        eventBus.register(this);

        projectService.getProjects()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            this.projects = result;
                            publishProjects();
                        }, error -> {
                            this.error = error;
                            publishProjects();
                        }
                );
    }


    private void publishProjects() {
        if (getView() != null) {
            if (projects != null) {
                getView().updateProjects(projects);
            } else {
                getView().onError(error);
            }
        }
    }

    public void setProject(ProjectEntity project) {
        if (model != null) {
            model.setProject(project);
        }
    }

    public BookingEntity getModel() {
        return model;
    }

    public void setModel(BookingEntity model) {
        this.model = model;
    }


    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }


    private boolean validate(BookingEntity model) {
        boolean successful = false;
        try {
            bookingService.validateBooking(model);
            successful = true;
        } catch (RuntimeException ex) {
            error = ex;
        }
        return successful;
    }

    @Subscribe
    public void onProjectsChange(ProjectsChangedEvent event) {
        this.projects = event.getProjects();
        publishProjects();
    }
    public void saveRecordedBooking()
    {
        Date endDate = new Date();
        model.setEndDate(endDate);

        saveBooking();
    }


    public void createNewBookingEntity()
    {
        Date startDate = new Date();
        model.setBeginDate(startDate);

        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour

        model.setEndDate(cal.getTime() );

        createBooking();
    }

    public void createBooking() {
        boolean valid = validate(model);
        if (valid) {
            model = bookingService.createOrUpdateBooking(model);
            getView().onCreateSuccessful(model);
        } else {
            getView().onError(error);
        }
    }
    public void saveBooking() {
        boolean valid = validate(model);
        if (valid) {
            model = bookingService.createOrUpdateBooking(model);
            getView().onSaveSuccessful(model);
        } else {
            getView().onError(error);
        }
    }

  
}
