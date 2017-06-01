package at.c02.tempus.app.ui.report;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;




@RequiresPresenter(ReportPresenter.class)
public class ReportActivity extends NucleusAppCompatActivity<ReportPresenter> {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.btnDateStart)
    protected Button btnDateStart;


    @BindView(R.id.btnreport)
    protected Button btnreport;

    @BindView(R.id.chart)
    protected HorizontalBarChart chart;

    /*
    @BindView(R.id.recyclerView)
    protected  RecyclerView recyclerView;
*/

    FlexibleAdapter<BookingListItem> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        BarChart chart = (BarChart) findViewById(R.id.chart);

        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("My Chart");
        chart.animateXY(2000, 2000);
        chart.invalidate();
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


//public ArrayList<BarDataSet> getDataSet(List<BookingEntity> bookings) {
    public ArrayList<BarDataSet> getDataSet() {

       // List<BookingEntity> bookings = null;

        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

       /* for (BookingEntity booking : bookings) {

            //if --> Start date missing /filter startDate

            //duration
            Date d1 = booking.getBeginDate();
            Date d2 = booking.getEndDate();
            long sum =  d2.getTime() - d1.getTime() ;


        //    valueSet1.add(new BarEntry(booking.getProject(), sum);


        }*/

    /* adapter?
        if (adapter == null) {
            adapter = new FlexibleAdapter<>(valueSet);
            // chart.setAdapter(adapter);

        } else {
            adapter.updateDataSet(listItems);
        }
    */


       //valueSet in dataSEt



    BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
        valueSet1.add(v1e1);
    BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
    BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
    BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
    BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
    BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);

    ArrayList<BarEntry> valueSet2 = new ArrayList<>();
    BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
        valueSet2.add(v2e1);
    BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        valueSet2.add(v2e2);
    BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        valueSet2.add(v2e3);
    BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        valueSet2.add(v2e4);
    BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
    BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);

    BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
    BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

    dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }



    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAI");
        xAxis.add("JUN");
        return xAxis;
    }




}
