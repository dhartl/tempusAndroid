package at.c02.tempus.app.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;

import org.greenrobot.eventbus.EventBus;

import java.util.Observable;

import javax.inject.Inject;

import at.c02.tempus.app.TempusApplication;
import at.c02.tempus.auth.AuthException;
import at.c02.tempus.auth.AuthService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nucleus.presenter.Presenter;

/**
 * Created by Daniel Hartl on 04.05.2017.
 */

public class AuthPresenter extends Presenter<AuthActivity> {
    private static final String TAG = "AuthPresenter";

    @Inject
    protected AuthService authService;

    @Inject
    protected EventBus eventBus;

    public AuthPresenter() {
        TempusApplication.getApp().getApplicationComponents().inject(this);
    }

    public void authenticate() {
        io.reactivex.Observable.fromCallable(() -> {
            try {
                AuthorizationService authorizationService = authService.getAuthorizationService(getView());
                PendingIntent postAuthIntent = PendingIntent.getActivity(getView(), 0, new Intent(getView(), AuthActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
                //PendingIntent.getActivity(getView(), 0, new Intent(getView(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                authorizationService.performAuthorizationRequest(authService.getAuthorizationRequest(), postAuthIntent);
            } catch (AuthException e) {
                Log.e(TAG, "Fehler bei der Authentifizierung", e);
                Toast.makeText(getView(), e.getMessage(), Toast.LENGTH_LONG);
            }
            return 1;
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }


    @Override
    protected void onTakeView(AuthActivity authActivity) {
        super.onTakeView(authActivity);
        handleAuth();
    }

    public void handleAuth() {
        if (getView() != null) {
            AuthorizationResponse resp = AuthorizationResponse.fromIntent(getView().getIntent());
            AuthorizationException ex = AuthorizationException.fromIntent(getView().getIntent());
            try {
                authService.onAuthorization(resp, ex, getView());
                if (ex != null) {
                    // show error
                } else if (resp != null) {
                    getView().startActivity(new Intent(getView(), MainActivity.class));
                    getView().finish();
                }
            } catch (AuthException e) {
                e.printStackTrace();
            }
        }
    }
}
