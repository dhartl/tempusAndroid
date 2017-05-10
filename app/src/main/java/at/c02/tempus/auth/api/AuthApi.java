package at.c02.tempus.auth.api;

import at.c02.tempus.auth.model.UserData;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by Daniel Hartl on 08.05.2017.
 */

public interface AuthApi {

    @GET("/connect/userinfo")
    public Observable<UserData> getUserInfo();
}
