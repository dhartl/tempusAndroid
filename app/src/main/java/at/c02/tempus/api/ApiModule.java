package at.c02.tempus.api;

import javax.inject.Singleton;

import at.c02.tempus.api.api.BookingApi;
import at.c02.tempus.api.api.EmployeeApi;
import at.c02.tempus.api.api.ProjectApi;
import dagger.Module;
import dagger.Provides;

@Module
public class ApiModule {

    // APIs here
    @Provides
    @Singleton
    public EmployeeApi provideEmployeeApi(ApiClient apiClient) {
        return apiClient.createService(EmployeeApi.class);
    }
    @Provides
    @Singleton
    public BookingApi provideBookingApi(ApiClient apiClient) {
        return apiClient.createService(BookingApi.class);
    }
    @Provides
    @Singleton
    public ProjectApi provideProjectApi(ApiClient apiClient) {
        return apiClient.createService(ProjectApi.class);
    }




}
