package at.c02.tempus.app.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import at.c02.tempus.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(MainActivityPresenter.class)
public class MainActivity extends NucleusAppCompatActivity<MainActivityPresenter> {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.tabLayout)
    protected TabLayout tabLayout;

    @BindView(R.id.pager)
    protected ViewPager viewPager;

    @BindView(R.id.nav_view)
    protected NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawer;

    protected ImageView imgNavHeaderBg;
    protected ImageView imgProfile;
    protected TextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        // Navigation Drawer wie in http://www.androidhive.info/2013/11/android-sliding-menu-using-navigation-drawer/ beschrieben
        View navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        setSupportActionBar(toolbar);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Creating our pager adapter
        MainActivityPager adapter = new MainActivityPager(getSupportFragmentManager(), this);

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        // initializing navigation menu
        setUpNavigationView();
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


    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        // This method will trigger on item Click of navigation menu
        navigationView.setNavigationItemSelectedListener(menuItem -> {

            //Check to see which item was being clicked and perform appropriate action
            switch (menuItem.getItemId()) {
                //Replacing the main content with ContentFragment Which is our Inbox View;
                case R.id.nav_sync:
                    getPresenter().startSync();
                    break;
                case R.id.nav_logout:
                    getPresenter().logout();
                    break;
                default:
            }
            return true;
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    public void setUserData(String userName) {

        txtName.setText(userName);
    }
}
