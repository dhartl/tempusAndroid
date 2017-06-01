package at.c02.tempus.app.ui.report;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;

import java.util.Calendar;
import java.util.Date;

import at.c02.tempus.R;
import at.c02.tempus.utils.DateUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;




@RequiresPresenter(ReportPresenter.class)
public class ReportActivity extends NucleusAppCompatActivity<ReportPresenter> {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.btnDateStart)
    protected Button btnDateStart;

    @BindView(R.id.chart)
    protected PieChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Zeit pro Projekt");
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chart.setDescription(null);
        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(7);
        chart.setTransparentCircleRadius(10);

        Legend legend = chart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setFormSize(14);
        legend.setTextSize(14);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setXEntrySpace(5);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @OnClick(R.id.btnDateStart)
    protected void onBtnDateStartClick() {
        Date date = getPresenter().getStartDate();
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = DateUtils.dateToCalendar(date);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, 0,
                (view, year, month, dayOfMonth) ->
                        getPresenter().createReport(year, month, dayOfMonth),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();

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


    public void publishData(Date startDate, PieData pieData) {
        updateStartDate(startDate);
        chart.setData(pieData);
        chart.highlightValues(null);
        chart.invalidate();
    }
}
