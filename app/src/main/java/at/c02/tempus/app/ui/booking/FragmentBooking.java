package at.c02.tempus.app.ui.booking;
import at.c02.tempus.db.entity.BookingEntity;
import nucleus.factory.RequiresPresenter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.List;
import at.c02.tempus.R;
import at.c02.tempus.db.entity.ProjectEntity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.view.NucleusSupportFragment;
import android.os.SystemClock;
import android.os.Handler;
import android.widget.TextView;


/**
 * Created by Daniel on 09.04.2017.
 */
@RequiresPresenter(FragmentBookingPresenter.class)
public class FragmentBooking extends NucleusSupportFragment<FragmentBookingPresenter> {

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
    long StartTime, MillisecondTime, TimeBuff, UpdateTime = 0L;
    Handler handler;
    int Seconds, Minutes, MilliSeconds,Hours;

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

        return root;
    }

    @OnClick(R.id.btnRecordButton)
    public void onRecordClick()  {
        if(Record_Active)
        {
            tvHeading.setText("Wähle ein Projekt und drücke START");
            onRecordClick2();
            this.btnRecordButton.setBackgroundColor(Color.rgb(180,243,184));
            this.btnRecordButton.setText("START");
            getPresenter().saveRecordedBooking();
            Record_Active = false;

        }

        else{

          onRecordClick2();
            Record_Active = true;
            tvHeading.setText("Drücke STOP wenn du fertig bist!");
        btnRecordButton.setBackgroundColor(Color.RED);
        btnRecordButton.setText("STOP");
        if (getPresenter().getModel().getProject() != null) {
            getPresenter().createNewBookingEntity();


        } else {
                //choose project
        }
        }
    }

   public void onRecordClick2()  {

        if(!Record_Active) {

            runnable = new Runnable() {

                @Override
                public void run() {
                    MillisecondTime = SystemClock.uptimeMillis() - StartTime;

                    UpdateTime = TimeBuff + MillisecondTime;

                    Seconds = (int) (UpdateTime / 1000);

                    Minutes = Seconds / 60;

                    Hours = Minutes / 60;

                    Seconds = Seconds % 60;

                    MilliSeconds = (int) (UpdateTime % 1000);

                    textView.setText("" + Hours + ":" + Minutes + ":"
                            + String.format("%02d", Seconds) + ":"
                            + String.format("%03d", MilliSeconds));

                    handler.postDelayed(this, 0);
                }
            };
            runnable.run();
            StartTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);
        }
        else
        {
          onStop();
            textView.setText(""  + String.format("%01d",0) + ":"  + String.format("%02d", 00) + ":" + String.format("%02d", 00) + ":" + String.format("%02d", 00));
        }
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
                String.format("Die Aufzeichnung der Buchung %s hat begonnen!",
                        projectName),
                Toast.LENGTH_SHORT)
                .show();
    }

    public void onSaveSuccessful(BookingEntity model) {
        String projectName = model.getProject() != null ? model.getProject().getName() : "";
        Toast.makeText(this.getContext(),
                String.format("Die Buchung %s wurde gespeichert!",
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
