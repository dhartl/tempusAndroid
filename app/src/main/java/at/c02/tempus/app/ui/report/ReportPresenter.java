package at.c02.tempus.app.ui.report;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import at.c02.tempus.app.TempusApplication;
import at.c02.tempus.app.ui.bookinglist.FragmentBookingList;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.service.ReportService;
import at.c02.tempus.utils.DateUtils;
import nucleus.presenter.Presenter;

/**
 * Created by Daniel Hartl on 12.05.2017.
 */

public class ReportPresenter extends Presenter<ReportActivity> {

    @Inject
    protected ReportService reportService;

    private BookingEntity model = new BookingEntity();

    //private List<BookingEntity> bookings;

    public ReportPresenter() {
        TempusApplication.getApp().getApplicationComponents().inject(this);
    }


    public void createReport() {
      //  reportService.findBookings();
    }


    public BookingEntity getModel() {
        return model;
    }

    public void setModel(BookingEntity model) {
        this.model = model;
    }

    public void setStartDate(int year, int month, int day) {
        Date newBeginDate = DateUtils.setDate(model.getBeginDate(), year, month, day);
        model.setBeginDate(newBeginDate);
    }
/*
    @Override
    protected void onTakeView(ReportActivity reportActivity) {
        super.onTakeView(reportActivity);
        if (bookings != null) {
            getView().showItems(bookings);
        }
    }*/
}
