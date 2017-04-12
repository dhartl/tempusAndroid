package at.c02.tempus.app.ui;

import android.util.Log;

import javax.inject.Inject;

import at.c02.tempus.app.TempusApplication;
import at.c02.tempus.db.repository.EmployeeRepository;
import nucleus.presenter.Presenter;

/**
 * Created by Daniel on 08.04.2017.
 */

public class MainActivityPresenter extends Presenter<MainActivity> {

    @Inject
    protected EmployeeRepository employeeRepository;

    public MainActivityPresenter() {
        TempusApplication.getApp().getApplicationComponents().inject(this);
        Log.d("MainActivityPresenter", "MainActivityPresenter loaded");
    }

}
