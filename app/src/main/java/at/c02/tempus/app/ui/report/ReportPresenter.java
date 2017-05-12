package at.c02.tempus.app.ui.report;

import javax.inject.Inject;

import at.c02.tempus.app.TempusApplication;
import at.c02.tempus.service.ReportService;
import nucleus.presenter.Presenter;

/**
 * Created by Daniel Hartl on 12.05.2017.
 */

public class ReportPresenter extends Presenter<ReportActivity> {

    @Inject
    protected ReportService reportService;

    public ReportPresenter() {
        TempusApplication.getApp().getApplicationComponents().inject(this);
    }


    public void createReport() {
        reportService.findBookings();
    }
}
