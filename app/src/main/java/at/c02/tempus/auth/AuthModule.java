package at.c02.tempus.auth;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import at.c02.tempus.auth.api.AuthApi;
import at.c02.tempus.service.SyncService;
import dagger.Module;
import dagger.Provides;


/**
 * Created by Daniel Hartl on 08.05.2017.
 */

@Module
public class AuthModule {

    @Provides
    @Singleton
    public AuthApi provideEmployeeApi(AuthApiClient apiClient) {
        return apiClient.createService(AuthApi.class);
    }

    @Provides
    @Singleton
    public AuthHolder provideAuthHolder(AuthStatePersister authStatePersister) {
        return new AuthHolder(authStatePersister);
    }

    @Provides
    @Singleton
    public CurrentUserHolder provideCurrentUserHolder() {
        return new CurrentUserHolder();
    }

    @Provides
    @Singleton
    public AuthService provideAuthService(EventBus eventBus,
                                          AuthHolder authHolder,
                                          TokenChangeHandler tokenChangeHandler) {
        return new AuthService(eventBus, authHolder);
    }

    @Provides
    @Singleton
    public TokenChangeHandler provideTokenChangeHandler(AuthApi authApi,
                                                        CurrentUserHolder currentUserHolder,
                                                        SyncService syncService,
                                                        Context context,
                                                        EventBus eventBus) {
        return new TokenChangeHandler(authApi, currentUserHolder, syncService, context, eventBus);
    }

}
