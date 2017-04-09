package at.c02.tempus.app.ui;

import android.util.Log;

import javax.inject.Inject;

import at.c02.tempus.app.TempusApplication;
import at.c02.tempus.db.repository.EmployeeRepository;

/**
 * Created by Daniel on 08.04.2017.
 */

public class MainActivityPresenter extends AbstractPresenter {

    @Inject
    protected EmployeeRepository employeeRepository;

    public MainActivityPresenter(MainActivity view) {
        super(view);
        TempusApplication.getApp().getApplicationComponents().inject(this);
        Log.d("MainActivityPresenter", "MainActivityPresenter loaded");
    }
}
