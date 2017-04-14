package at.c02.tempus.app.ui.bookinglist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.EventLog;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import at.c02.tempus.app.TempusApplication;
import at.c02.tempus.app.ui.utils.DateUtils;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.entity.ProjectEntity;
import at.c02.tempus.service.BookingService;
import at.c02.tempus.service.ProjectService;
import at.c02.tempus.service.event.ProjectsChangedEvent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import nucleus.presenter.Presenter;

/**
 * Created by Daniel on 12.04.2017.
 */

public class BookingActivityPresenter extends Presenter<BookingActivity> {

    private static final String TAG = "BookingActivityPres";

    private BookingEntity model;

    @Inject
    protected BookingService bookingService;

    @Inject
    protected ProjectService projectService;

    @Inject
    protected EventBus eventBus;

    private List<ProjectEntity> projects;
    private Throwable error;

    public BookingActivityPresenter() {
        TempusApplication.getApp().getApplicationComponents().inject(this);
        model = new BookingEntity();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        eventBus.register(this);
    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    public BookingEntity getModel() {
        return model;
    }

    public void setModel(BookingEntity model) {
        this.model = model;
    }

    @Override
    protected void onTakeView(BookingActivity bookingActivity) {
        super.onTakeView(bookingActivity);
        Log.d(TAG, "onTakeView");
        bookingActivity.updateStartDate(model.getBeginDate());
        bookingActivity.updateEndDate(model.getEndDate());
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

    public void setStartDate(int year, int month, int day) {
        Date newBeginDate = DateUtils.setDate(model.getBeginDate(), year, month, day);
        model.setBeginDate(newBeginDate);
        getView().updateStartDate(newBeginDate);
    }

    public void setStartTime(int hours, int minutes) {
        Date newBeginDate = DateUtils.setTime(model.getBeginDate(), hours, minutes);
        model.setBeginDate(newBeginDate);
        getView().updateStartDate(newBeginDate);
    }

    public void setEndDate(int year, int month, int day) {
        Date newEndDate = DateUtils.setDate(model.getEndDate(), year, month, day);
        model.setEndDate(newEndDate);
        getView().updateEndDate(newEndDate);
    }

    public void setEndTime(int hours, int minutes) {
        Date newEndDate = DateUtils.setTime(model.getEndDate(), hours, minutes);
        model.setEndDate(newEndDate);
        getView().updateEndDate(newEndDate);
    }


    public void save() {
        boolean valid = validate(model);
        if (valid) {
            model = bookingService.createOrUpdateBooking(model);
            getView().onSaveSuccessful(model);
        } else {
            getView().onError(error);
        }

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

    public void setProject(ProjectEntity project) {
        model.setProject(project);
    }

    @Subscribe
    public void onProjectsChange(ProjectsChangedEvent event) {
        this.projects = event.getProjects();
        publishProjects();
    }
}
