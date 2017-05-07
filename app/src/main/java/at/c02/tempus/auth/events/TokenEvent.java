package at.c02.tempus.auth.events;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.TokenResponse;

/**
 * Created by Daniel Hartl on 06.05.2017.
 */

public class TokenEvent {
    private TokenResponse tokenResponse;
    private AuthorizationException authorizationException;

    public TokenEvent(TokenResponse tokenResponse, AuthorizationException authorizationException) {
        this.tokenResponse = tokenResponse;
        this.authorizationException = authorizationException;
    }

    public TokenResponse getTokenResponse() {
        return tokenResponse;
    }

    public AuthorizationException getAuthorizationException() {
        return authorizationException;
    }
}
