package at.c02.tempus.service.sync;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Daniel Hartl on 16.04.2017.
 */
@Module
public class SyncServiceModule {

    @Provides
    @Singleton
    public BookingFromServerSyncService provideBookingFromServerSyncService() {
        return new BookingFromServerSyncService();
    }

    @Provides
    @Singleton
    public BookingToServerSyncService provideBookingToServerSyncService() {
        return new BookingToServerSyncService();
    }

    @Provides
    @Singleton
    public EmployeeSyncService provideEmployeeSyncService() {
        return new EmployeeSyncService();
    }

    @Provides
    @Singleton
    public ProjectSyncService provideProjectSyncService() {
        return new ProjectSyncService();
    }
}
