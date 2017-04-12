package at.c02.tempus.app.ui.bookinglist;

import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import at.c02.tempus.app.TempusApplication;
import at.c02.tempus.app.ui.utils.DateUtils;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.entity.ProjectEntity;
import at.c02.tempus.service.BookingService;
import at.c02.tempus.service.ProjectService;
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

    private List<ProjectEntity> projects;
    private Throwable error;

    public BookingActivityPresenter() {
        TempusApplication.getApp().getApplicationComponents().inject(this);
        model = new BookingEntity();
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
        bookingService.createOrUpdateBooking(model);
    }

}
