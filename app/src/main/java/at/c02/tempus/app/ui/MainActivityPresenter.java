package at.c02.tempus.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import at.c02.tempus.app.TempusApplication;
import at.c02.tempus.auth.AuthService;
import at.c02.tempus.db.entity.EmployeeEntity;
import at.c02.tempus.service.EmployeeService;
import at.c02.tempus.service.SyncService;
import at.c02.tempus.service.event.EmployeeChangedEvent;
import nucleus.presenter.Presenter;

/**
 * Created by Daniel on 08.04.2017.
 */

public class MainActivityPresenter extends Presenter<MainActivity> {

    @Inject
    protected SyncService syncService;

    @Inject
    protected EmployeeService employeeService;

    @Inject
    protected AuthService authService;

    @Inject
    protected EventBus eventBus;

    private EmployeeEntity employeeEntity;

    public MainActivityPresenter() {
        TempusApplication.getApp().getApplicationComponents().inject(this);
        Log.d("MainActivityPresenter", "MainActivityPresenter loaded");
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

    @Override
    protected void onTakeView(MainActivity mainActivity) {
        super.onTakeView(mainActivity);
        loadEmployee();
    }

    private void loadEmployee() {
        employeeService.getCurrentEmployee().subscribe(employeeEntity -> {
            this.employeeEntity = employeeEntity.orNull();
            publishEmployee();
        });
    }

    public void startSync() {
        syncService.synchronize(getView());
    }

    protected void publishEmployee() {
        String userName = "";
        if (employeeEntity == null) {
            userName = "Benutzername";
        } else {
            userName = employeeEntity.getFirstName() + " " + employeeEntity.getLastName();
        }
        getView().setUserData(userName);
    }

    public void logout() {
        authService.logout(getView());
        getView().finish();
    }

    @Subscribe
    public void onEmployeeChangedEvent(EmployeeChangedEvent event) {
        this.employeeEntity = event.getEmployee();
        publishEmployee();
    }
}
