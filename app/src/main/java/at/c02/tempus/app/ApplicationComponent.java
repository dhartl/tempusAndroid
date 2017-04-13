package at.c02.tempus.app;


import at.c02.tempus.api.ApiModule;
import at.c02.tempus.app.ui.bookinglist.BookingActivityPresenter;
import at.c02.tempus.app.ui.bookinglist.FragmentBookingListPresenter;
import at.c02.tempus.db.DatabaseModule;
import at.c02.tempus.service.ServiceModule;

import javax.inject.Singleton;

import at.c02.tempus.app.ui.MainActivityPresenter;
import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class, DatabaseModule.class, ServiceModule.class})
public interface ApplicationComponent {
    //Inject class which want the providers to be injected
    void inject(MainActivityPresenter mainActivityPresenter);

    void inject(BookingActivityPresenter bookingActivityPresenter);

    void inject(FragmentBookingListPresenter fragmentBookingListPresenter);
}
