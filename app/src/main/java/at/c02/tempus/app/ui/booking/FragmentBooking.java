package at.c02.tempus.app.ui.booking;


import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.utils.DateUtils;
import nucleus.factory.RequiresPresenter;

import android.app.TimePickerDialog;
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import at.c02.tempus.R;
import at.c02.tempus.db.entity.ProjectEntity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.view.NucleusSupportFragment;


/**
 * Created by Daniel on 09.04.2017.
 */
@RequiresPresenter(FragmentBookingPresenter.class)
public class FragmentBooking extends NucleusSupportFragment<FragmentBookingPresenter> {

    @BindView(R.id.cbProject)
    protected Spinner cbProject;

    @BindView(R.id.btnRecordButton)
    protected Button btnRecordButton;

    @Text

   private ArrayAdapter<ProjectEntity> adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_booking, container, false);
        ButterKnife.bind(this,root);
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
    public void onRecordClick()
    {
        if(getPresenter().getModel().getProject()!= null) {
            getPresenter().setBeginTime();
        }
        else
        {
            //choose project
        }

    }


    public void timeCounter()
    {

    }

    public void onSaveSuccessful(BookingEntity model) {
        DateFormat dateTimeFormat = DateUtils.getDateTimeFormat();
        String projectName = model.getProject() != null ? model.getProject().getName() : "";
        Toast.makeText(this.getContext(),
                String.format("Die Buchung %s: %s - %s wurde gespeichert",
                        projectName,
                        dateTimeFormat.format(model.getBeginDate()),
                        dateTimeFormat.format(model.getEndDate())),
                Toast.LENGTH_SHORT)
                .show();

        //TODO:
        // this.getActivity().setResult(RESULT_OK);
        this.getActivity().finish();
    }

    public void updateProjects(List<ProjectEntity> projects) {
        adapter.clear();
        adapter.addAll(projects);
    }
    public void onError(Throwable error) {
        Toast.makeText(this.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
    }


}
