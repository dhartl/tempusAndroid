package at.c02.tempus.app.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import at.c02.tempus.R;


import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private static final String EXTRA_RX = "CALLINWORK";
    private boolean rxCallInWorks = false;
    private MainActivityPresenter presenter;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.tabLayout)
    protected TabLayout tabLayout;

    @BindView(R.id.pager)
    protected ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Creating our pager adapter
        MainActivityPager adapter = new MainActivityPager(getSupportFragmentManager(), this);

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        presenter = new MainActivityPresenter(this);
        if (savedInstanceState != null) {
            rxCallInWorks = savedInstanceState.getBoolean(EXTRA_RX);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.rxUnSubscribe();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save state for activity recreation
        outState.putBoolean(EXTRA_RX, rxCallInWorks);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //resume request if activity recreated due to any reason
        if (rxCallInWorks) {
            //presenter.loadStateList(searchText);
        }
    }

}
