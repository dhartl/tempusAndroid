package at.c02.tempus.app.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import at.c02.tempus.R;


import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private static final String EXTRA_RX = "CALLINWORK";
    private boolean rxCallInWorks = false;
    private MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
