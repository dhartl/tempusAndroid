package at.c02.tempus.app.ui.report;

import java.util.Date;
import javax.inject.Inject;
import at.c02.tempus.app.TempusApplication;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.service.BookingService;
import at.c02.tempus.service.ReportService;
import at.c02.tempus.utils.DateUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import nucleus.presenter.Presenter;
import java.util.List;



public class ReportPresenter extends Presenter<ReportActivity> {


    private BookingEntity model = new BookingEntity();

    @Inject
    protected ReportService reportService;

    @Inject
    protected BookingService bookingService;

    private Throwable error;

    protected List<BookingEntity> bookings;

    //private Date searchDate;

    public ReportPresenter() {
        TempusApplication.getApp().getApplicationComponents().inject(this);
    }


    public BookingEntity getModel() {
        return model;
    }

    public void setModel(BookingEntity model) {
        this.model = model;
    }


    public void createReport() {
        bookingService.getBookings().observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBookingsLoaded);
    }


    private void onBookingsLoaded(List<BookingEntity> bookings) {
        this.bookings = bookings;
        if (getView() != null) {
            getView().showItems(bookings);
        }
    }



    public void setStartDate(int year, int month, int day) {
        Date newBeginDate = DateUtils.setDate(model.getBeginDate(), year, month, day);
        model.setBeginDate(newBeginDate);
       getView().updateStartDate(newBeginDate);
    }

/*
    private void publishStartDate() {
        if (getView() != null) {
            if (model != null) {
                getView().updateStartDate(model.getBeginDate());
            } else {
                getView().updateStartDate(null);
            }
        }
    }
*/


    @Override
    protected void onTakeView(ReportActivity reportActivity) {
        super.onTakeView(reportActivity);
            if (bookings != null) {
                getView().showItems(bookings);
            }
        }

}
