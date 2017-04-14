package at.c02.tempus.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.inject.Inject;

import at.c02.tempus.app.TempusApplication;
import at.c02.tempus.db.repository.EmployeeRepository;
import at.c02.tempus.service.SyncService;
import nucleus.presenter.Presenter;

/**
 * Created by Daniel on 08.04.2017.
 */

public class MainActivityPresenter extends Presenter<MainActivity> {

    @Inject
    protected SyncService syncService;

    public MainActivityPresenter() {
        TempusApplication.getApp().getApplicationComponents().inject(this);
        Log.d("MainActivityPresenter", "MainActivityPresenter loaded");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        syncService.synchronize();
    }
}
