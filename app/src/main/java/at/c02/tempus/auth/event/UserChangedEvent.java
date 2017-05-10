package at.c02.tempus.auth.event;

/**
 * Created by Daniel Hartl on 09.05.2017.
 */

public class UserChangedEvent {
    private String userName;

    public UserChangedEvent(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
