package at.c02.tempus.app.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import at.c02.tempus.R;
import at.c02.tempus.app.ui.booking.FragmentBooking;
import at.c02.tempus.app.ui.bookinglist.FragmentBookingList;

/**
 * Created by Daniel on 09.04.2017.
 */

public class MainActivityPager extends FragmentStatePagerAdapter {

    private Context context;

    public MainActivityPager(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentBooking();
            case 1:
                return new FragmentBookingList();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.fragment_booking_title);
            case 1:
                return context.getString(R.string.fragment_booking_list_title);
            default:
                return "";
        }
    }
}
