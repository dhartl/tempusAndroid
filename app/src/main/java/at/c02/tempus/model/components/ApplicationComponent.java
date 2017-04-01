package at.c02.tempus.model.components;


import at.c02.tempus.model.modules.ApiModule;
import at.c02.tempus.model.modules.ApplicationModule;
import at.c02.tempus.presentor.PresenterLayer;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent {
    //Inject class which want the providers to be injected
    void inject(PresenterLayer presenterLayer);

}
