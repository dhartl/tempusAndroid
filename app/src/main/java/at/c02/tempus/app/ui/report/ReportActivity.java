package at.c02.tempus.app.ui.report;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.c02.tempus.R;
import at.c02.tempus.app.ui.bookinglist.BookingActivityPresenter;
import at.c02.tempus.app.ui.bookinglist.BookingListItem;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.utils.DateUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

/**
 * Created by Daniel Hartl on 12.05.2017.
 */
@RequiresPresenter(ReportPresenter.class)
public class ReportActivity extends NucleusAppCompatActivity<ReportPresenter> {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.btnDateStart)
    protected Button btnDateStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.btnDateStart)
    protected void onBtnDateStartClick() {
        Date date = getPresenter().getModel().getBeginDate();
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = DateUtils.dateToCalendar(date);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, 0,
                (view, year, month, dayOfMonth) ->
                        getPresenter().setStartDate(year, month, dayOfMonth),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    public void showItems(List<BookingEntity> bookings) {
        List<BookingListItem> listItems = new ArrayList<>();
        for (BookingEntity booking : bookings) {
            String projectName = booking.getProject() != null ? booking.getProject().getName() : "";
            listItems.add(new BookingListItem(booking.getId(),
                    projectName,
                    booking.getBeginDate(),
                    booking.getEndDate()));
        }
        Log.d("CREATION",listItems.toString());


    }
}
