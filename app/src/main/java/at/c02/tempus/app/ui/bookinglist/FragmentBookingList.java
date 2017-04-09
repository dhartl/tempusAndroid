package at.c02.tempus.app.ui.bookinglist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.c02.tempus.R;

/**
 * Created by Daniel on 09.04.2017.
 */

public class FragmentBookingList extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_booking_list, container, false);
    }
}
