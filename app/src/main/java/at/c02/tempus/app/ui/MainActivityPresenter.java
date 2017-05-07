package at.c02.tempus.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.inject.Inject;

import at.c02.tempus.app.TempusApplication;
import at.c02.tempus.db.entity.EmployeeEntity;
import at.c02.tempus.service.EmployeeService;
import at.c02.tempus.service.SyncService;
import nucleus.presenter.Presenter;

/**
 * Created by Daniel on 08.04.2017.
 */

public class MainActivityPresenter extends Presenter<MainActivity> {

    @Inject
    protected SyncService syncService;

    @Inject
    protected EmployeeService employeeService;

    private EmployeeEntity employeeEntity;

    public MainActivityPresenter() {
        TempusApplication.getApp().getApplicationComponents().inject(this);
        Log.d("MainActivityPresenter", "MainActivityPresenter loaded");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);

    }

    @Override
    protected void onTakeView(MainActivity mainActivity) {
        super.onTakeView(mainActivity);
        syncService.synchronize(getView());
        employeeService.getCurrentEmployee().subscribe(employeeEntity -> {
            this.employeeEntity = employeeEntity;
            publishEmployee();
        });
    }

    public void startSync() {
        syncService.synchronize(getView());
    }

    protected void publishEmployee() {
        String userName = "";
        if(employeeEntity == null) {
            userName = "Benutzername";
        }else {
          userName = employeeEntity.getFirstName() +" "+employeeEntity.getLastName();
        }
        getView().setUserData(userName);
    }
}
