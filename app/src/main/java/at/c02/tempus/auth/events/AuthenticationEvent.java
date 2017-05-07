package at.c02.tempus.auth.events;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;

/**
 * Created by Daniel Hartl on 06.05.2017.
 */

public class AuthenticationEvent {
    private AuthorizationResponse authorizationResponse;
    private AuthorizationException authorizationException;

    public AuthenticationEvent(AuthorizationResponse authorizationResponse, AuthorizationException authorizationException) {
        this.authorizationResponse = authorizationResponse;
        this.authorizationException = authorizationException;
    }

    public AuthorizationResponse getAuthorizationResponse() {
        return authorizationResponse;
    }

    public AuthorizationException getAuthorizationException() {
        return authorizationException;
    }
}
