package at.c02.tempus.app.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.util.Log;
import android.widget.Toast;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import at.c02.tempus.app.TempusApplication;
import at.c02.tempus.auth.AuthException;
import at.c02.tempus.auth.AuthHolder;
import at.c02.tempus.auth.AuthService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import nucleus.presenter.Presenter;

/**
 * Created by Daniel Hartl on 04.05.2017.
 */

public class AuthPresenter extends Presenter<AuthActivity> {
    private static final String TAG = "AuthPresenter";

    @Inject
    protected AuthService authService;

    @Inject
    protected AuthHolder authHolder;

    @Inject
    protected EventBus eventBus;

    private AuthorizationService authorizationService;

    private String authMessage;
    private boolean btnAuthenticateEnabled = true;

    public AuthPresenter() {
        TempusApplication.getApp().getApplicationComponents().inject(this);
    }

    public void authenticate() {
        PendingIntent postAuthIntent = PendingIntent.getActivity(getView(), 0, new Intent(getView(), AuthActivity.class), PendingIntent.FLAG_ONE_SHOT);
        authService.getAuthorizationRequest()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(authorizationRequest -> {
                    authorizationService.performAuthorizationRequest(authorizationRequest, postAuthIntent);
                    getView().finish();
                }, ex -> {
                    Log.e(TAG, "Fehler bei der Authentifizierung", ex);
                    authMessage = ex.getMessage();
                    btnAuthenticateEnabled = true;
                    publishState();
                });
    }

    @Override
    protected void onTakeView(AuthActivity authActivity) {
        super.onTakeView(authActivity);
        authService.getAuthorizationService(getView())
                .map(authorizationService -> {
                    this.authorizationService = authorizationService;
                    handleAuth();
                    return authorizationService;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(authorizationService -> {
                    authMessage = "";
                    btnAuthenticateEnabled = true;
                    publishState();
                }, ex -> {
                    Log.e(TAG, "Fehler bei der Authentifizierung", ex);
                    authMessage = ex.getMessage();
                    btnAuthenticateEnabled = false;
                    publishState();
                });
        publishState();
    }

    @Override
    protected void onDropView() {
        if (authorizationService != null) {
            authorizationService.dispose();
            authorizationService = null;
        }
        super.onDropView();
    }

    public void handleAuth() throws AuthorizationException {
        if (getView() != null) {
            AuthorizationResponse resp = AuthorizationResponse.fromIntent(getView().getIntent());
            AuthorizationException ex = AuthorizationException.fromIntent(getView().getIntent());
            authService.onAuthorization(authorizationService, resp, ex);
            if (ex != null) {
                throw ex;
            } else if (resp != null) {
                forwardToMainView();
            }
        }
    }

    private void forwardToMainView() {
        getView().startActivity(new Intent(getView(), MainActivity.class));
        getView().finish();
    }

    @MainThread
    private void publishState() {
        if(getView() != null) {
            getView().setAuthMessage(authMessage);
            getView().enableLoginButton(btnAuthenticateEnabled);
        }
    }
}
