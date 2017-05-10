package at.c02.tempus.auth;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import at.c02.tempus.auth.AuthHolder;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Daniel Hartl on 05.05.2017.
 */

public class AuthInterceptor implements Interceptor {

    @Inject
    protected AuthHolder authHolder;

    @Inject
    public AuthInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder ongoing = chain.request().newBuilder();
        String authToken = authHolder.getAuthToken();
        if (authToken != null) {
            ongoing.addHeader("Authorization", "Bearer " + authToken);
        }
        return chain.proceed(ongoing.build());
    }
}
