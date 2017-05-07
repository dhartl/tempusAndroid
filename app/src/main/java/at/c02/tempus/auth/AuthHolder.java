package at.c02.tempus.auth;

import net.openid.appauth.AuthState;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;

/**
 * Created by Daniel Hartl on 05.05.2017.
 */
public class AuthHolder {

    private AuthState authState;

    public AuthHolder() {

    }

    public void setAuthState(AuthState authState) {
        this.authState = authState;
    }

    public AuthState getAuthState() {
        return authState;
    }

    public String getAuthToken() {
        if (authState != null) {
            return authState.getAccessToken();
        }
        return null;
    }
}
