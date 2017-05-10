package at.c02.tempus.auth;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.util.Log;

import net.openid.appauth.AppAuthConfiguration;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenResponse;
import net.openid.appauth.connectivity.ConnectionBuilder;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import at.c02.tempus.auth.event.TokenEvent;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.ReplaySubject;

/**
 * Created by Daniel Hartl on 04.05.2017.
 */

public class AuthService {

    private static final String AUTHORIZATION_SERVICE_ENDPOINT = "http://10.0.2.2:5000/.well-known/openid-configuration";
    private static final String REDIRECT_URI = "at.c02.tempus:/oauth2redirect";
    private static final String CLIENT_ID = "tempusAndroid";

    private static final String TAG = "AuthService";

    protected AuthHolder authHolder;

    private ReplaySubject<AuthorizationServiceConfiguration> authorizationServiceConfigurationReplaySubject;

    private EventBus eventBus;


    public AuthService(EventBus eventBus, AuthHolder authHolder) {
        this.eventBus = eventBus;
        this.authHolder = authHolder;

        initializeAuthServiceConfiguration();
    }

    private void initializeAuthServiceConfiguration() {
        authorizationServiceConfigurationReplaySubject = ReplaySubject.create(1);

        AuthorizationServiceConfiguration.fetchFromUrl(Uri.parse(AUTHORIZATION_SERVICE_ENDPOINT),
                (authorizationServiceConfiguration, e) -> {
                    if (e != null) {
                        authorizationServiceConfigurationReplaySubject.onError(e);
                    } else {
                        authorizationServiceConfigurationReplaySubject.onNext(authorizationServiceConfiguration);
                    }
                    authorizationServiceConfigurationReplaySubject.onComplete();
                }, getConnectionBuilder());
    }

    public Observable<AuthorizationRequest> getAuthorizationRequest() {
        return authorizationServiceConfigurationReplaySubject.
                map(authorizationServiceConfiguration -> new AuthorizationRequest.Builder(
                        authorizationServiceConfiguration,
                        CLIENT_ID,
                        ResponseTypeValues.CODE,
                        Uri.parse(REDIRECT_URI))
                        .setScopes("api1",
                                AuthorizationRequest.Scope.OPENID,
                                AuthorizationRequest.Scope.PROFILE)
                        .build());
    }

    private AppAuthConfiguration getAppAuthConfiguration() {
        return new AppAuthConfiguration.Builder()
                .setConnectionBuilder(getConnectionBuilder())
                .build();
    }

    private ConnectionBuilder getConnectionBuilder() {
        return new ConnectionBuilder() {
            public HttpURLConnection openConnection(Uri uri) throws IOException {
                URL url = new URL(uri.toString());
                return (HttpURLConnection) url.openConnection();
            }
        };
    }

    public Observable<AuthorizationService> getAuthorizationService(Context context) {
        return Observable.fromCallable(() -> new AuthorizationService(context, getAppAuthConfiguration()));
    }

    public Observable<TokenResponse> onAuthorization(AuthorizationService authorizationService,
                                                     AuthorizationResponse resp,
                                                     AuthorizationException ex
    ) {
        if (resp != null || ex != null) {
            AuthState authState = new AuthState(resp, ex);
            authHolder.setAuthState(authState);
        }

        return Single.<TokenResponse>create(singleEmitter -> {
            if (authHolder.getAuthState() != null) {
                if (authHolder.getAuthToken() == null && authHolder.getAuthState().getLastAuthorizationResponse() != null) {
                    authorizationService.performTokenRequest(
                            authHolder.getAuthState().getLastAuthorizationResponse().createTokenExchangeRequest(),
                            (tokenResponse, e) -> {
                                authHolder.updateToken(tokenResponse, e);
                                if (e != null) {
                                    singleEmitter.onError(e);
                                } else if (tokenResponse != null) {
                                    eventBus.post(new TokenEvent(authHolder.getAuthState()));
                                    Log.d(TAG, "Token: " + authHolder.getAuthToken());
                                    singleEmitter.onSuccess(tokenResponse);
                                }
                            });
                } else if (authHolder.getAuthState().getLastAuthorizationResponse() != null) {
                    eventBus.post(new TokenEvent(authHolder.getAuthState()));
                    Log.d(TAG, "Token: " + authHolder.getAuthToken());
                    singleEmitter.onSuccess(authHolder.getAuthState().getLastTokenResponse());
                }
            } else {
                singleEmitter.onError(new AuthException("Keine Authentifizierung vorhanden"));
            }
        }).toObservable();
    }


    public void logout(Context context) {
        authHolder.setAuthState(null);
        try {
            String url = "http://10.0.2.2:5000/Account/Logout";

            // Here is a method that returns the chrome package name
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.intent.setPackage("com.android.chrome");
            customTabsIntent.launchUrl(context, Uri.parse(url));

        } catch (Exception ex) {
            Log.e(TAG, "Fehler beim revoken des Access-Tokens");
        }
    }
}
