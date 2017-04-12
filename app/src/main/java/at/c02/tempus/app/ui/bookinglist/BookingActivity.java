package at.c02.tempus.app.ui.bookinglist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.c02.tempus.R;
import at.c02.tempus.app.ui.utils.DateUtils;
import at.c02.tempus.db.entity.ProjectEntity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

/**
 * Created by Daniel on 09.04.2017.
 */
@RequiresPresenter(BookingActivityPresenter.class)
public class BookingActivity extends NucleusAppCompatActivity<BookingActivityPresenter> {

    @BindView(R.id.cbProject)
    protected Spinner cbProject;

    @BindView(R.id.btnDateStart)
    protected Button btnDateStart;

    @BindView(R.id.btnTimeStart)
    protected Button btnTimeStart;

    @BindView(R.id.btnDateEnd)
    protected Button btnDateEnd;

    @BindView(R.id.btnTimeEnd)
    protected Button btnTimeEnd;

    private ArrayAdapter<ProjectEntity> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(this);

        adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);
        cbProject.setAdapter(adapter);

    }

    @OnClick(R.id.btnDateStart)
    protected void onBtnDateStartClick() {
        Date date = getPresenter().getModel().getBeginDate();
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = DateUtils.dateToCalendar(date);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, 0, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                getPresenter().setStartDate(year, month, dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    @OnClick(R.id.btnTimeStart)
    protected void onBtnTimeStartClick() {
        Date date = getPresenter().getModel().getBeginDate();
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = DateUtils.dateToCalendar(date);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, 0, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                getPresenter().setStartTime(hourOfDay, minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    @OnClick(R.id.btnDateEnd)
    protected void onBtnDateEndClick() {
        Date date = getPresenter().getModel().getEndDate();
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = DateUtils.dateToCalendar(date);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, 0, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                getPresenter().setEndDate(year, month, dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        Date beginDate = getPresenter().getModel().getBeginDate();
        if(beginDate != null) {
            datePickerDialog.getDatePicker().setMinDate(beginDate.getTime());
        }
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    @OnClick(R.id.btnTimeEnd)
    protected void onBtnTimeEndClick() {
        Date date = getPresenter().getModel().getEndDate();
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = DateUtils.dateToCalendar(date);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, 0, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                getPresenter().setEndTime(hourOfDay, minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }


    public void updateStartDate(Date startDate) {
        updateDateTimeButtonText(startDate, btnDateStart, btnTimeStart, "Start");
    }


    public void updateEndDate(Date endDate) {
        updateDateTimeButtonText(endDate, btnDateEnd, btnTimeEnd, "End");
    }

    protected void updateDateTimeButtonText(Date date, Button btnDate, Button btnTime, String preText) {
        String dateText;
        String timeText;
        if (date == null) {
            dateText = preText + "datum";
            timeText = preText + "zeit";
        } else {
            dateText = DateUtils.getDateFormat().format(date);
            timeText = DateUtils.getTimeFormat().format(date);
        }
        btnDate.setText(dateText);
        btnTime.setText(timeText);
    }


    public void updateProjects(List<ProjectEntity> projects) {
        adapter.clear();
        adapter.addAll(projects);
    }

    public void onError(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
    }
}
