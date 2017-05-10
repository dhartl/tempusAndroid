package at.c02.tempus.auth;

import at.c02.tempus.auth.api.AuthApi;
import at.c02.tempus.auth.model.UserData;

/**
 * Created by Daniel Hartl on 08.05.2017.
 */

public class CurrentUserHolder {

    private final AuthStatePersister authStatePersister;
    private String currentUser;

    public CurrentUserHolder(AuthStatePersister authStatePersister) {
        this.authStatePersister= authStatePersister;
        currentUser = authStatePersister.readUserName();
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
        authStatePersister.writeUserName(currentUser);
    }
}
