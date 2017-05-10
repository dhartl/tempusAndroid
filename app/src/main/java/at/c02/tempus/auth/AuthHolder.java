package at.c02.tempus.auth;

import net.openid.appauth.AuthState;

/**
 * Created by Daniel Hartl on 05.05.2017.
 */
public class AuthHolder {

    private AuthState authState;
    private AuthStatePersister authStatePersister;


    public AuthHolder(AuthStatePersister authStatePersister) {
       authState = authStatePersister.readAuthState();
        this.authStatePersister = authStatePersister;
    }

    public void setAuthState(AuthState authState) {
        this.authState = authState;
        authStatePersister.writeAuthState(authState);
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
