package at.c02.tempus.app.ui.report;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import eu.davidea.flexibleadapter.FlexibleAdapter;


@RequiresPresenter(ReportPresenter.class)
public class ReportActivity extends NucleusAppCompatActivity<ReportPresenter> {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.btnDateStart)
    protected Button btnDateStart;


    @BindView(R.id.btnreport)
    protected Button btnreport;

    @BindView(R.id.recyclerView)
    protected  RecyclerView recyclerView;

    FlexibleAdapter<BookingListItem> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

    }

    @OnClick(R.id.btnreport)
    protected void onBtnreportClick() { getPresenter().createReport();}


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
            //if --> starDate
            String projectName = booking.getProject() != null ? booking.getProject().getName() : "";
            listItems.add(new BookingListItem(booking.getId(),
                    projectName,
                    booking.getBeginDate(),
                    booking.getEndDate()));
        }
        if (adapter == null) {
            adapter = new FlexibleAdapter<>(listItems);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateDataSet(listItems);
        }

       // Log.d("CREATION",listItems.toString());

    }



    public void updateStartDate(Date startDate) {
        updateDateTimeButtonText(startDate, btnDateStart, "Start");
    }

    protected void updateDateTimeButtonText(Date date, Button btnDate, String preText) {
        String dateText;

        if (date == null) {
            dateText = preText + "datum";
        } else {
            dateText = DateUtils.getDateFormat().format(date);
        }
        btnDate.setText(dateText);
    }
}
