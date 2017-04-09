package at.c02.tempus.app;

import android.content.Context;

import at.c02.tempus.app.TempusApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final TempusApplication app;

    public ApplicationModule(TempusApplication app) {
        this.app = app;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return app;
    }

}
