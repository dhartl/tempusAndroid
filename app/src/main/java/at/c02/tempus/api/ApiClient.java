package at.c02.tempus.api;

import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest.AuthenticationRequestBuilder;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.TokenRequestBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import at.c02.tempus.api.auth.ApiKeyAuth;
import at.c02.tempus.api.auth.HttpBasicAuth;
import at.c02.tempus.api.auth.OAuth;
import at.c02.tempus.api.auth.OAuth.AccessTokenListener;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiClient {

    public static final String BASE_URL = "http://10.0.2.2:5000/";

    private Map<String, Interceptor> apiAuthorizations;
    private OkHttpClient.Builder okBuilder;
    private Retrofit.Builder adapterBuilder;

    public ApiClient() {
        apiAuthorizations = new LinkedHashMap<String, Interceptor>();
        createDefaultAdapter();
    }

    public ApiClient(String[] authNames) {
        this();
        for (String authName : authNames) {
            throw new RuntimeException("auth name \"" + authName + "\" not found in available auth names");
        }
    }

    /**
     * Basic constructor for single auth name
     *
     * @param authName Authentication name
     */
    public ApiClient(String authName) {
        this(new String[]{authName});
    }

    /**
     * Helper constructor for single api key
     *
     * @param authName Authentication name
     * @param apiKey   API key
     */
    public ApiClient(String authName, String apiKey) {
        this(authName);
        this.setApiKey(apiKey);
    }

    /**
     * Helper constructor for single basic auth or password oauth2
     *
     * @param authName Authentication name
     * @param username Username
     * @param password Password
     */
    public ApiClient(String authName, String username, String password) {
        this(authName);
        this.setCredentials(username, password);
    }

    /**
     * Helper constructor for single password oauth2
     *
     * @param authName Authentication name
     * @param clientId Client ID
     * @param secret   Client Secret
     * @param username Username
     * @param password Password
     */
    public ApiClient(String authName, String clientId, String secret, String username, String password) {
        this(authName);
        this.getTokenEndPoint()
                .setClientId(clientId)
                .setClientSecret(secret)
                .setUsername(username)
                .setPassword(password);
    }

    public void createDefaultAdapter() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create();

        okBuilder = new OkHttpClient.Builder();
        okBuilder.readTimeout(5, TimeUnit.SECONDS);
        okBuilder.connectTimeout(5, TimeUnit.SECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okBuilder.addInterceptor(logging);


        adapterBuilder = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));


    }

    public <S> S createService(Class<S> serviceClass) {
        return adapterBuilder
                .client(okBuilder.build())
                .build()
                .create(serviceClass);

    }

    /**
     * Helper method to configure the first api key found
     *
     * @param apiKey API key
     */
    private void setApiKey(String apiKey) {
        for (Interceptor apiAuthorization : apiAuthorizations.values()) {
            if (apiAuthorization instanceof ApiKeyAuth) {
                ApiKeyAuth keyAuth = (ApiKeyAuth) apiAuthorization;
                keyAuth.setApiKey(apiKey);
                return;
            }
        }
    }

    /**
     * Helper method to configure the username/password for basic auth or password oauth
     *
     * @param username Username
     * @param password Password
     */
    private void setCredentials(String username, String password) {
        for (Interceptor apiAuthorization : apiAuthorizations.values()) {
            if (apiAuthorization instanceof HttpBasicAuth) {
                HttpBasicAuth basicAuth = (HttpBasicAuth) apiAuthorization;
                basicAuth.setCredentials(username, password);
                return;
            }
            if (apiAuthorization instanceof OAuth) {
                OAuth oauth = (OAuth) apiAuthorization;
                oauth.getTokenRequestBuilder().setUsername(username).setPassword(password);
                return;
            }
        }
    }

    /**
     * Helper method to configure the token endpoint of the first oauth found in the apiAuthorizations (there should be only one)
     *
     * @return Token request builder
     */
    public TokenRequestBuilder getTokenEndPoint() {
        for (Interceptor apiAuthorization : apiAuthorizations.values()) {
            if (apiAuthorization instanceof OAuth) {
                OAuth oauth = (OAuth) apiAuthorization;
                return oauth.getTokenRequestBuilder();
            }
        }
        return null;
    }

    /**
     * Helper method to configure authorization endpoint of the first oauth found in the apiAuthorizations (there should be only one)
     *
     * @return Authentication request builder
     */
    public AuthenticationRequestBuilder getAuthorizationEndPoint() {
        for (Interceptor apiAuthorization : apiAuthorizations.values()) {
            if (apiAuthorization instanceof OAuth) {
                OAuth oauth = (OAuth) apiAuthorization;
                return oauth.getAuthenticationRequestBuilder();
            }
        }
        return null;
    }

    /**
     * Helper method to pre-set the oauth access token of the first oauth found in the apiAuthorizations (there should be only one)
     *
     * @param accessToken Access token
     */
    public void setAccessToken(String accessToken) {
        for (Interceptor apiAuthorization : apiAuthorizations.values()) {
            if (apiAuthorization instanceof OAuth) {
                OAuth oauth = (OAuth) apiAuthorization;
                oauth.setAccessToken(accessToken);
                return;
            }
        }
    }

    /**
     * Helper method to configure the oauth accessCode/implicit flow parameters
     *
     * @param clientId     Client ID
     * @param clientSecret Client secret
     * @param redirectURI  Redirect URI
     */
    public void configureAuthorizationFlow(String clientId, String clientSecret, String redirectURI) {
        for (Interceptor apiAuthorization : apiAuthorizations.values()) {
            if (apiAuthorization instanceof OAuth) {
                OAuth oauth = (OAuth) apiAuthorization;
                oauth.getTokenRequestBuilder()
                        .setClientId(clientId)
                        .setClientSecret(clientSecret)
                        .setRedirectURI(redirectURI);
                oauth.getAuthenticationRequestBuilder()
                        .setClientId(clientId)
                        .setRedirectURI(redirectURI);
                return;
            }
        }
    }

    /**
     * Configures a listener which is notified when a new access token is received.
     *
     * @param accessTokenListener Access token listener
     */
    public void registerAccessTokenListener(AccessTokenListener accessTokenListener) {
        for (Interceptor apiAuthorization : apiAuthorizations.values()) {
            if (apiAuthorization instanceof OAuth) {
                OAuth oauth = (OAuth) apiAuthorization;
                oauth.registerAccessTokenListener(accessTokenListener);
                return;
            }
        }
    }

    /**
     * Adds an authorization to be used by the client
     *
     * @param authName      Authentication name
     * @param authorization Authorization interceptor
     */
    public void addAuthorization(String authName, Interceptor authorization) {
        if (apiAuthorizations.containsKey(authName)) {
            throw new RuntimeException("auth name \"" + authName + "\" already in api authorizations");
        }
        apiAuthorizations.put(authName, authorization);
        okBuilder.addInterceptor(authorization);
    }

    public Map<String, Interceptor> getApiAuthorizations() {
        return apiAuthorizations;
    }

    public void setApiAuthorizations(Map<String, Interceptor> apiAuthorizations) {
        this.apiAuthorizations = apiAuthorizations;
    }

    public Retrofit.Builder getAdapterBuilder() {
        return adapterBuilder;
    }

    public void setAdapterBuilder(Retrofit.Builder adapterBuilder) {
        this.adapterBuilder = adapterBuilder;
    }

    public OkHttpClient.Builder getOkBuilder() {
        return okBuilder;
    }

    public void addAuthsToOkBuilder(OkHttpClient.Builder okBuilder) {
        for (Interceptor apiAuthorization : apiAuthorizations.values()) {
            okBuilder.addInterceptor(apiAuthorization);
        }
    }

    /**
     * Clones the okBuilder given in parameter, adds the auth interceptors and uses it to configure the Retrofit
     *
     * @param okClient An instance of OK HTTP client
     */
    public void configureFromOkclient(OkHttpClient okClient) {
        this.okBuilder = okClient.newBuilder();
        addAuthsToOkBuilder(this.okBuilder);

    }
}

