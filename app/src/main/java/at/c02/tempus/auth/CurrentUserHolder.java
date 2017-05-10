package at.c02.tempus.auth;

import at.c02.tempus.auth.api.AuthApi;
import at.c02.tempus.auth.model.UserData;

/**
 * Created by Daniel Hartl on 08.05.2017.
 */

public class CurrentUserHolder {

    private UserData currentUser;

    public CurrentUserHolder() {
    }

    public UserData getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserData currentUser) {
        this.currentUser = currentUser;
    }
}
