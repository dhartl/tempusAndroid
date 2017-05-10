package at.c02.tempus.auth;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import at.c02.tempus.auth.api.AuthApi;
import at.c02.tempus.auth.event.TokenEvent;
import at.c02.tempus.auth.event.UserChangedEvent;
import at.c02.tempus.service.SyncService;

/**
 * Created by Daniel Hartl on 09.05.2017.
 */

public class TokenChangeHandler {

    private static final String TAG = "TokenChangeHandler";
    protected AuthApi authApi;

    protected CurrentUserHolder currentUserHolder;

    protected SyncService syncService;

    protected Context context;


    private final EventBus eventBus;


    public TokenChangeHandler(AuthApi authApi, CurrentUserHolder currentUserHolder, SyncService syncService, Context context, EventBus eventBus) {
        this.authApi = authApi;
        this.currentUserHolder = currentUserHolder;
        this.syncService = syncService;
        this.context = context;
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    @Subscribe
    public void onTokenEvent(TokenEvent tokenEvent) {
        authApi.getUserInfo().subscribe(userData -> {
            String userName = userData.getName();
            Log.d(TAG, "user loaded: " + userName);
            currentUserHolder.setCurrentUser(userData);
            eventBus.post(new UserChangedEvent(userName));
            syncService.synchronize(context);
        });

    }

}
