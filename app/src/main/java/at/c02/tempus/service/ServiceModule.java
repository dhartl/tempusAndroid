package at.c02.tempus.service;

import android.util.EventLog;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusBuilder;

import javax.inject.Singleton;

import at.c02.tempus.api.api.BookingApi;
import at.c02.tempus.api.api.EmployeeApi;
import at.c02.tempus.api.api.ProjectApi;
import at.c02.tempus.api.model.Booking;
import at.c02.tempus.db.repository.BookingRepository;
import at.c02.tempus.db.repository.EmployeeRepository;
import at.c02.tempus.db.repository.ProjectRepository;
import at.c02.tempus.service.BookingService;
import at.c02.tempus.service.EmployeeService;
import at.c02.tempus.service.ProjectService;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Daniel on 09.04.2017.
 */
@Module
public class ServiceModule {

    @Provides
    @Singleton
    public BookingService provideBookingService(BookingApi bookingApi, BookingRepository bookingRepository, EventBus eventBus) {
        return new BookingService(bookingApi, bookingRepository, eventBus);
    }

    @Provides
    @Singleton
    public EmployeeService provideEmployeeService(EmployeeApi employeeApi, EmployeeRepository employeeRepository) {
        return new EmployeeService(employeeApi, employeeRepository);
    }

    @Provides
    @Singleton
    public ProjectService provideProjectService(ProjectApi projectApi, ProjectRepository projectRepository, EventBus eventBus) {
        return new ProjectService(projectApi, projectRepository, eventBus);
    }

    @Provides
    @Singleton
    public SyncService provideSnycService(ProjectService projectService) {
        return new SyncService(projectService);
    }

    @Provides
    @Singleton
    public EventBus provideEventBus() {
        return EventBus.builder().logSubscriberExceptions(true).installDefaultEventBus();
    }

}
