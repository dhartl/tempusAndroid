package at.c02.tempus.model.modules;

import android.content.Context;

import at.c02.tempus.AndroidSeedApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final AndroidSeedApplication app;

    public ApplicationModule(AndroidSeedApplication app) {
        this.app = app;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return app;
    }

}
