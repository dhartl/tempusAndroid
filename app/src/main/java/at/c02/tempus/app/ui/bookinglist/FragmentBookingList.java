package at.c02.tempus.app.ui.bookinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import at.c02.tempus.R;
import at.c02.tempus.db.entity.BookingEntity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusSupportFragment;

/**
 * Created by Daniel on 09.04.2017.
 */
@RequiresPresenter(FragmentBookingListPresenter.class)
public class FragmentBookingList extends NucleusSupportFragment<FragmentBookingListPresenter> {

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;

    FlexibleAdapter<BookingListItem> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_booking_list, container, false);
        ButterKnife.bind(this, root);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

    @OnClick(R.id.floatingActionButton)
    protected void onAddClick() {
        Intent intent = new Intent(getActivity(), BookingActivity.class);
        getActivity().startActivity(intent);
    }

    public void showItems(List<BookingEntity> bookings) {
        List<BookingListItem> listItems = new ArrayList<>();
        for (BookingEntity booking : bookings) {
            String projectName = booking.getProject() != null ? booking.getProject().getName() : "";
            listItems.add(new BookingListItem(booking.getId(),
                    projectName,
                    booking.getBeginDate(),
                    booking.getEndDate()));
        }
        if (adapter == null) {
            adapter = new FlexibleAdapter<>(listItems);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateDataSet(listItems);
        }
    }

    public void onError(Throwable error) {
        Toast.makeText(this.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
    }

}
