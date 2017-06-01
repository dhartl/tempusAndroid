package at.c02.tempus.app.ui.report;

import android.graphics.Color;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import at.c02.tempus.app.TempusApplication;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.service.ReportService;
import at.c02.tempus.utils.DateUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import nucleus.presenter.Presenter;


public class ReportPresenter extends Presenter<ReportActivity> {


    @Inject
    protected ReportService reportService;

    protected PieData pieData;

    protected Date startDate;


    public ReportPresenter() {
        TempusApplication.getApp().getApplicationComponents().inject(this);
    }


    public void createReport(int year, int month, int dayOfMonth) {
        startDate = DateUtils.setDate(null, year, month, dayOfMonth);
        reportService
                .findBookings(startDate)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBookingsLoaded);
        if(getView() != null) {
            getView().updateStartDate(startDate);
        }
    }


    private void onBookingsLoaded(List<BookingEntity> bookings) {

        Map<String, Long> projectToDuration = new HashMap<>();
        for (BookingEntity booking : bookings) {
            Long duration = projectToDuration.get(booking.getProject().getName());
            if (duration == null) {
                duration = getDurationInMinutes(booking);
            } else {
                duration += getDurationInMinutes(booking);
            }
            projectToDuration.put(booking.getProject().getName(), duration);
        }

        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Long> mapEntry : projectToDuration.entrySet()) {
            PieEntry entry = new PieEntry(mapEntry.getValue());
            entry.setLabel(mapEntry.getKey());
            entries.add(entry);
        }

        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        dataSet.setColors(ColorTemplate.PASTEL_COLORS);

        pieData = new PieData(dataSet);
        pieData.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                long valueMinutes = (long) value;
                if (valueMinutes < 60) {
                    return String.format("%02d", valueMinutes);
                } else {
                    long minutes = valueMinutes % 60;
                    long hours = TimeUnit.MINUTES.toHours(valueMinutes);
                    return String.format("%d:%02d", hours, minutes);
                }
            }
        });
        pieData.setValueTextSize(14);
        pieData.setValueTextColor(Color.WHITE);

        if (getView() != null) {
            getView().publishData(startDate, pieData);
        }
    }

    private long getDurationInMinutes(BookingEntity booking) {
        return TimeUnit.MILLISECONDS.toMinutes(booking.getEndDate().getTime() - booking.getBeginDate().getTime());
    }


    @Override
    protected void onTakeView(ReportActivity reportActivity) {
        super.onTakeView(reportActivity);
        if (getView() != null && pieData != null) {
            getView().publishData(startDate, pieData);
        }
    }

    public Date getStartDate() {
        return startDate;
    }
}