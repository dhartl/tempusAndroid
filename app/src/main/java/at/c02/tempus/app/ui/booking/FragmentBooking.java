package at.c02.tempus.app.ui.booking;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import at.c02.tempus.R;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.entity.ProjectEntity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusSupportFragment;


/**
 * Created by Daniel on 09.04.2017.
 */
@RequiresPresenter(FragmentBookingPresenter.class)
public class FragmentBooking extends NucleusSupportFragment<FragmentBookingPresenter> {

    private static final String CLOCK_FORMAT = "%02d:%02d:%02d:%03d";

    @BindView(R.id.cbProject)
    protected Spinner cbProject;

    @BindView(R.id.tvHeading)
    protected TextView tvHeading;

    @BindView(R.id.btnRecordButton)
    protected Button btnRecordButton;

    @BindView(R.id.textView)
    protected TextView textView;

    private ArrayAdapter<ProjectEntity> adapter;

    //Stopwatch
    long StartTime, TimeBuff, UpdateTime = 0L;
    Handler handler;

    public boolean Record_Active;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_booking, container, false);
        ButterKnife.bind(this, root);
        Record_Active = false;

        handler = new Handler();
        adapter = new ArrayAdapter<>(this.getContext(), R.layout.support_simple_spinner_dropdown_item);
        cbProject.setAdapter(adapter);
        cbProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getPresenter().setProject(adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getPresenter().setProject(null);
            }
        });
        updateDisplayTime(0);

        return root;
    }

    @OnClick(R.id.btnRecordButton)
    public void onRecordClick() {
        if (Record_Active) {
            tvHeading.setText(R.string.recording_start_hint);
            onRecordClick2();
            this.btnRecordButton.setBackgroundColor(Color.rgb(180, 243, 184));
            this.btnRecordButton.setText(R.string.recording_start);
            getPresenter().saveRecordedBooking();
            Record_Active = false;

        } else {

            onRecordClick2();
            Record_Active = true;
            tvHeading.setText(R.string.recording_stop_hint);
            btnRecordButton.setBackgroundColor(Color.RED);
            btnRecordButton.setText(R.string.recording_stop);
            if (getPresenter().getModel().getProject() != null) {
                getPresenter().createNewBookingEntity();


            } else {
                //choose project
            }
        }
    }

    public void onRecordClick2() {

        if (!Record_Active) {

            runnable = new Runnable() {

                @Override
                public void run() {
                    UpdateTime = TimeBuff + (SystemClock.uptimeMillis() - StartTime);

                    updateDisplayTime(UpdateTime);

                    handler.postDelayed(this, 0);
                }
            };
            runnable.run();
            StartTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);
        } else {
            onStop();
            updateDisplayTime(0);
        }
    }

    public void updateDisplayTime(long timeInMilliseconds) {

        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMilliseconds) % 60;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds) % 60;
        long hours = TimeUnit.MILLISECONDS.toHours(timeInMilliseconds);
        long milliSeconds = timeInMilliseconds % 1000;

        textView.setText(String.format(CLOCK_FORMAT, hours, minutes, seconds, milliSeconds));
    }

    @Override
    public void onStop() {
        super.onStop();
        Record_Active = false;
        handler.removeCallbacks(runnable);
    }


    public Runnable runnable;

    public void onCreateSuccessful(BookingEntity model) {
        String projectName = model.getProject() != null ? model.getProject().getName() : "";
        Toast.makeText(this.getContext(),
                String.format(getString(R.string.recording_started),
                        projectName),
                Toast.LENGTH_SHORT)
                .show();
    }

    public void onSaveSuccessful(BookingEntity model) {
        String projectName = model.getProject() != null ? model.getProject().getName() : "";
        Toast.makeText(this.getContext(),
                String.format(getString(R.string.recording_stopped),
                        projectName),
                Toast.LENGTH_SHORT)
                .show();
    }


    public void updateProjects(List<ProjectEntity> projects) {
        adapter.clear();
        adapter.addAll(projects);
    }

    public void onError(Throwable error) {
        Toast.makeText(this.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
    }


}
