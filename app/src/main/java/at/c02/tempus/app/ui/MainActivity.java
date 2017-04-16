package at.c02.tempus.app.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import at.c02.tempus.R;


import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusActivity;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(MainActivityPresenter.class)
public class MainActivity extends NucleusAppCompatActivity<MainActivityPresenter> {


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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miSync: {
                getPresenter().startSync();
                return true;
            }
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
