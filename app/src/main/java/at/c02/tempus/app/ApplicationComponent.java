package at.c02.tempus.app;


import javax.inject.Singleton;

import at.c02.tempus.api.ApiModule;
import at.c02.tempus.app.ui.AuthPresenter;
import at.c02.tempus.app.ui.MainActivityPresenter;
import at.c02.tempus.app.ui.bookinglist.BookingActivityPresenter;
import at.c02.tempus.app.ui.bookinglist.FragmentBookingListPresenter;
import at.c02.tempus.app.ui.report.ReportPresenter;
import at.c02.tempus.auth.AuthModule;
import at.c02.tempus.db.DatabaseModule;
import at.c02.tempus.service.ServiceModule;
import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, AuthModule.class, ApiModule.class, DatabaseModule.class, ServiceModule.class})
public interface ApplicationComponent {
    //Inject class which want the providers to be injected
    void inject(MainActivityPresenter mainActivityPresenter);

    void inject(BookingActivityPresenter bookingActivityPresenter);

    void inject(FragmentBookingListPresenter fragmentBookingListPresenter);

    void inject(AuthPresenter authPresenter);

    void inject(ReportPresenter reportPresenter);
}
