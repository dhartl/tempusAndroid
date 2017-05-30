package at.c02.tempus.app.ui.report;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import at.c02.tempus.app.TempusApplication;
import at.c02.tempus.app.ui.bookinglist.BookingActivity;
import at.c02.tempus.app.ui.bookinglist.FragmentBookingList;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.service.ReportService;
import at.c02.tempus.utils.DateUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import nucleus.presenter.Presenter;



public class ReportPresenter extends Presenter<ReportActivity> {

    @Inject
    protected ReportService reportService;

    private BookingEntity model = new BookingEntity();

   // private List<BookingEntity> bookings;

    private Date searchDate;

    public ReportPresenter() {
        TempusApplication.getApp().getApplicationComponents().inject(this);
    }


    public void createReport() {
      reportService.findBookings(searchDate);
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
        searchDate = newBeginDate;
    }


    private void publishStartDate() {
        if (getView() != null) {
            if (model != null) {
                getView().updateStartDate(model.getBeginDate());
            } else {
                getView().updateStartDate(null);
            }
        }
    }


    /*

    @Override
    protected void onTakeView(ReportActivity reportActivity) {
            if (bookings != null) {
                getView().showItems(bookings);
            }
        }
*/
}
