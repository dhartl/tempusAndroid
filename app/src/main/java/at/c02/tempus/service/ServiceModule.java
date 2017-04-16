package at.c02.tempus.service;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import at.c02.tempus.service.sync.BookingFromServerSyncService;
import at.c02.tempus.service.sync.BookingToServerSyncService;
import at.c02.tempus.service.sync.EmployeeSyncService;
import at.c02.tempus.service.sync.ProjectSyncService;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Daniel on 09.04.2017.
 */
@Module
public class ServiceModule {

    @Provides
    @Singleton
    public SyncService provideSyncService(EventBus eventBus,
                                          BookingFromServerSyncService bookingFromServerSyncService,
                                          BookingToServerSyncService bookingToServerSyncService,
                                          EmployeeSyncService employeeSyncService,
                                          ProjectSyncService projectSyncService) {
        return new SyncService(eventBus,
                bookingFromServerSyncService,
                bookingToServerSyncService,
                employeeSyncService,
                projectSyncService);
    }

    @Provides
    @Singleton
    public EventBus provideEventBus() {
        return EventBus.builder().logSubscriberExceptions(true).installDefaultEventBus();
    }

}
