package at.c02.tempus.app.ui.booking;


import at.c02.tempus.app.ui.bookinglist.BookingListItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import nucleus.factory.RequiresPresenter;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
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


/**
 * Created by Daniel on 09.04.2017.
 */
@RequiresPresenter(FragmentBookingPresenter.class)
public class FragmentBooking extends NucleusSupportFragment<FragmentBookingPresenter> {

    @BindView(R.id.cbProject)
    protected Spinner cbProject;

    @BindView(R.id.btnRecordButton)
    protected Button btnRecordButton;

    private ArrayAdapter<ProjectEntity> adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_booking, container, false);
        ButterKnife.bind(this,root);
        return root;
    }

    @OnClick(R.id.btnRecordButton)
    protected  void onRecordClick()
    {
        //do something...
    }


    public void updateProjects(List<ProjectEntity> projects) {
        adapter.clear();
        adapter.addAll(projects);
    }
    public void onError(Throwable error) {
        Toast.makeText(this.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
    }


}
