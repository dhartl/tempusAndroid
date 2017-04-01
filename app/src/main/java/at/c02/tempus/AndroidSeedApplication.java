package at.c02.tempus;

import android.app.Application;

import at.c02.tempus.model.components.ApplicationComponent;
import at.c02.tempus.model.components.DaggerApplicationComponent;


public class AndroidSeedApplication extends Application {
    private static AndroidSeedApplication app;
    private ApplicationComponent applicationComponent;

    public static AndroidSeedApplication getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //intitialize dagger component to build the module and initialize DI
        applicationComponent = DaggerApplicationComponent.builder().build();
    }
    public AndroidSeedApplication(){
        app = this;
    }
    public ApplicationComponent getApplicationComponents() {
        return applicationComponent;
    }

}
