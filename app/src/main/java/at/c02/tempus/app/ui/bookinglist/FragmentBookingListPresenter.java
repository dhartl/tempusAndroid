package at.c02.tempus.app.ui.bookinglist;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import at.c02.tempus.app.TempusApplication;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.service.BookingService;
import at.c02.tempus.service.event.BookingChangedEvent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import nucleus.presenter.Presenter;

/**
 * Created by Daniel Hartl on 13.04.2017.
 */

public class FragmentBookingListPresenter extends Presenter<FragmentBookingList> {

    @Inject
    protected BookingService bookingService;

    @Inject
    protected EventBus eventBus;

    private List<BookingEntity> bookings;

    private Throwable error;

    public FragmentBookingListPresenter() {
        TempusApplication.getApp().getApplicationComponents().inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        eventBus.register(this);
        loadBookings();
    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onTakeView(FragmentBookingList fragmentBookingList) {
        super.onTakeView(fragmentBookingList);
        if(bookings != null) {
            getView().showItems(bookings);
        }
    }

    public void loadBookings() {
        bookingService.getBookings()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBookingsLoaded,
                        this::onError);
    }

    private void onBookingsLoaded(List<BookingEntity> bookings) {
        this.bookings = bookings;
        if(getView() != null) {
            getView().showItems(bookings);
        }
    }

    private void onError(Throwable error) {
        this.error = error;
        if(getView() != null) {
            getView().onError(error);
        }
    }

    @Subscribe
    public void onBookingChangedEvent(BookingChangedEvent event) {
        loadBookings();
    }
}
