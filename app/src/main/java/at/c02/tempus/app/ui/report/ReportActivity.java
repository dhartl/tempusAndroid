package at.c02.tempus.app.ui.report;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import at.c02.tempus.R;
import at.c02.tempus.app.ui.bookinglist.BookingActivityPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

/**
 * Created by Daniel Hartl on 12.05.2017.
 */
@RequiresPresenter(ReportPresenter.class)
public class ReportActivity extends NucleusAppCompatActivity<ReportPresenter> {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }
}
