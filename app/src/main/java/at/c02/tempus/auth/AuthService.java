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
import net.openid.appauth.ClientAuthentication;
import net.openid.appauth.ClientSecretPost;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.connectivity.ConnectionBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import at.c02.tempus.auth.events.AuthenticationEvent;
import at.c02.tempus.auth.events.TokenEvent;

/**
 * Created by Daniel Hartl on 04.05.2017.
 */

public class AuthService {

    private static final String AUTHORIZATION_SERVICE_ENDPOINT = "http://10.0.2.2:5000/.well-known/openid-configuration";
    private static final String REDIRECT_URI = "at.c02.tempus:/oauth2redirect";

    private static final String CLIENT_ID = "tempusAndroid";

    private static final String TAG = "AuthService";

    private AuthorizationServiceConfiguration authorizationServiceConfiguration;


    protected AuthHolder authHolder;

    protected EventBus eventBus;

    private AuthorizationException authException;

    public AuthService(EventBus eventBus, AuthHolder authHolder) {
        this.eventBus = eventBus;
        this.authHolder = authHolder;
        eventBus.register(this);
        initializeAuthServiceConfiguration();
    }

    public void initializeAuthServiceConfiguration() {
        AuthorizationServiceConfiguration.fetchFromUrl(Uri.parse(AUTHORIZATION_SERVICE_ENDPOINT),
                (authorizationServiceConfiguration, e) -> {
                    if (e != null) {
                        this.authException = e;
                        Log.e(TAG, "Fehler bei Discovery des AuthTokenEndpoints " + AUTHORIZATION_SERVICE_ENDPOINT, e);
                    } else {
                        this.authorizationServiceConfiguration = authorizationServiceConfiguration;
                    }
                }, getConnectionBuilder());
    }

    public AuthorizationRequest getAuthorizationRequest() {
        return new AuthorizationRequest.Builder(
                authorizationServiceConfiguration,
                CLIENT_ID,
                ResponseTypeValues.CODE,
                Uri.parse(REDIRECT_URI))
                .setScope("api1")
                .build();
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

    public AuthorizationService getAuthorizationService(Context context) throws AuthException {
        if (authorizationServiceConfiguration == null) {
            if (authException != null) {
                throw new AuthException("Die Authentifizierung wurde nicht korrekt initialisiert!", authException);
            } else {
                throw new AuthException("Die Authentifizierung wurde nicht korrekt initialisiert!");
            }
        }

        AuthorizationService authorizationService = new AuthorizationService(context, getAppAuthConfiguration());

        return authorizationService;
    }

    private ClientAuthentication getClientAuthentication() {
        return new ClientSecretPost("tempusAndroidPassword");
    }

    public void onAuthorization(AuthorizationResponse resp, AuthorizationException ex, Context context) throws AuthException {
        if (resp != null || ex != null) {
            AuthState authState = new AuthState(resp, ex);
            authHolder.setAuthState(authState);
            AuthorizationService authorizationService = getAuthorizationService(context);
            authorizationService.performTokenRequest(resp.createTokenExchangeRequest(),
                    (tokenResponse, e) -> {
                        eventBus.post(new TokenEvent(tokenResponse, e));
                    });
        }
    }

    @Subscribe
    public void onAuthenticationEvent(AuthenticationEvent event) {


    }

    @Subscribe
    public void onTokenEvent(TokenEvent event) {
        if (event.getTokenResponse() != null || event.getAuthorizationException() != null) {
            authHolder.getAuthState().update(event.getTokenResponse(), event.getAuthorizationException());
            Log.d(TAG, "Token: " + authHolder.getAuthToken());
        }
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
