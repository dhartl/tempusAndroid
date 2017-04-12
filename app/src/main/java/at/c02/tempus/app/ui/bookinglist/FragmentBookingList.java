package at.c02.tempus.app.ui.bookinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.c02.tempus.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Daniel on 09.04.2017.
 */

public class FragmentBookingList extends Fragment {

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_booking_list, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @OnClick(R.id.floatingActionButton)
    protected void onAddClick() {
        Intent intent = new Intent(getActivity(), BookingActivity.class);
        getActivity().startActivity(intent);
    }
}
