package at.c02.tempus.auth.event;

import net.openid.appauth.AuthState;

/**
 * Created by Daniel Hartl on 09.05.2017.
 */

public class TokenEvent {
    private AuthState authState;

    public TokenEvent(AuthState authState) {
        this.authState = authState;
    }

    public AuthState getAuthState() {
        return authState;
    }
}
