package at.c02.tempus.app;

import android.app.Application;

import at.c02.tempus.api.ApiModule;
import at.c02.tempus.auth.AuthModule;
import at.c02.tempus.db.DatabaseModule;
import at.c02.tempus.service.ServiceModule;


public class TempusApplication extends Application {
    private static TempusApplication app;
    private ApplicationComponent applicationComponent;

    public static TempusApplication getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //intitialize dagger component to build the module and initialize DI
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .authModule(new AuthModule())
                .databaseModule(new DatabaseModule())
                .apiModule(new ApiModule())
                .serviceModule(new ServiceModule())
                .build();
    }

    public TempusApplication() {
        app = this;
    }

    public ApplicationComponent getApplicationComponents() {
        return applicationComponent;
    }

}
