package at.c02.tempus.service;


import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import at.c02.tempus.api.model.Booking;

/**
 * Created by Daniel Hartl on 14.04.2017.
 */

public class SyncService {

    private static final String TAG = "SyncService";

    private ProjectService projectService;
    private EmployeeService employeeService;
    private BookingService bookingService;
    private EventBus eventBus;

    public SyncService(ProjectService projectService,
                       EmployeeService employeeService,
                       BookingService bookingService,
                       EventBus eventBus) {
        this.projectService = projectService;
        this.employeeService = employeeService;
        this.bookingService = bookingService;
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    public void synchronize() {
        Log.d(TAG, "Sync started");
        projectService.loadProjects();
        employeeService.loadEmployee();
        bookingService.synchronizeBookings();
    }

    @Subscribe
    public void onBookingChangedEvent(Booking booking) {
        Log.d(TAG, "Sync of Bookings started");
        bookingService.synchronizeBookings();
    }
}
