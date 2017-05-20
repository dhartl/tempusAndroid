package at.c02.tempus.app.ui.booking;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

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

    private void onError(Throwable error) {
        this.error = error;
        if (getView() != null) {
            getView().onError(error);
        }
    }


    @Subscribe
    public void onProjectsChange(ProjectsChangedEvent event) {
        this.projects = event.getProjects();
        publishProjects();
    }
}
