package at.c02.tempus.service;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.c02.tempus.service.event.BookingChangedEvent;
import at.c02.tempus.service.sync.BookingFromServerSyncService;
import at.c02.tempus.service.sync.BookingToServerSyncService;
import at.c02.tempus.service.sync.EmployeeSyncService;
import at.c02.tempus.service.sync.ProjectSyncService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Daniel Hartl on 14.04.2017.
 */

@Singleton
public class SyncService {

    private static final String TAG = "SyncService";

    @Inject
    protected EventBus eventBus;

    @Inject
    protected BookingFromServerSyncService bookingFromServerSyncService;

    @Inject
    protected BookingToServerSyncService bookingToServerSyncService;

    @Inject
    protected EmployeeSyncService employeeSyncService;

    @Inject
    protected ProjectSyncService projectSyncService;

    @Inject
    public SyncService(EventBus eventBus,
                       BookingFromServerSyncService bookingFromServerSyncService,
                       BookingToServerSyncService bookingToServerSyncService,
                       EmployeeSyncService employeeSyncService,
                       ProjectSyncService projectSyncService) {
        this.eventBus = eventBus;
        this.bookingFromServerSyncService = bookingFromServerSyncService;
        this.bookingToServerSyncService = bookingToServerSyncService;
        this.employeeSyncService = employeeSyncService;
        this.projectSyncService = projectSyncService;
        eventBus.register(this);
    }

    public void synchronize(Context context) {
        Log.d(TAG, "Sync started");
        employeeSyncService.syncronize().flatMap(result -> projectSyncService.syncronize())
                .flatMap(result -> synchronizeBookings())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    Toast.makeText(context, "Synchronisation abgeschlossen", Toast.LENGTH_SHORT).show();
                }, error -> {
                    Toast.makeText(context, "Fehler bei der Synchronisation", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Fehler bei der Synchronisation", error);
                });

    }

    public Observable<Boolean> synchronizeBookings() {
        Log.d(TAG, "Sync of Bookings started");
        return bookingToServerSyncService.syncronize()
                .flatMap(result -> bookingFromServerSyncService.syncronize());
    }

    @Subscribe
    public void onBookingChangedEvent(BookingChangedEvent booking) {
        synchronizeBookings()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                }, error -> Log.e(TAG, "Fehler bei der Synchronisation", error));
    }
}
