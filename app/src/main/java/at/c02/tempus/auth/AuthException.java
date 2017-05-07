package at.c02.tempus.auth;

/**
 * Created by Daniel Hartl on 04.05.2017.
 */

public class AuthException extends Exception {

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

}
