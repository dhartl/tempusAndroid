package at.c02.tempus.service;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import at.c02.tempus.api.api.BookingApi;
import at.c02.tempus.api.api.EmployeeApi;
import at.c02.tempus.api.api.ProjectApi;
import at.c02.tempus.db.repository.BookingRepository;
import at.c02.tempus.db.repository.EmployeeRepository;
import at.c02.tempus.db.repository.ProjectRepository;
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
    public EmployeeService provideEmployeeService(EmployeeApi employeeApi, EmployeeRepository employeeRepository, EventBus eventBus) {
        return new EmployeeService(employeeApi, employeeRepository, eventBus);
    }

    @Provides
    @Singleton
    public ProjectService provideProjectService(ProjectApi projectApi, ProjectRepository projectRepository, EventBus eventBus) {
        return new ProjectService(projectApi, projectRepository, eventBus);
    }

    @Provides
    @Singleton
    public SyncService provideSnycService(ProjectService projectService, EmployeeService employeeService) {
        return new SyncService(projectService, employeeService);
    }

    @Provides
    @Singleton
    public EventBus provideEventBus() {
        return EventBus.builder().logSubscriberExceptions(true).installDefaultEventBus();
    }

}
