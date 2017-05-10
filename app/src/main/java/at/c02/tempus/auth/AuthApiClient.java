package at.c02.tempus.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.c02.tempus.api.DateDeserializer;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Singleton
public class AuthApiClient {

    // 10.0.2.2 ist ip von Localhost für den Emulator
    // Wenn Handy verwendet wird, dann muss hier die Ip des PCs eingetragen werden!
    public static final String BASE_URL = "http://10.0.2.2:5000/";

    private Map<String, Interceptor> apiAuthorizations;
    private OkHttpClient.Builder okBuilder;
    private Retrofit.Builder adapterBuilder;

    protected AuthInterceptor authInterceptor;

    @Inject
    public AuthApiClient(AuthInterceptor authInterceptor) {

        this.authInterceptor = authInterceptor;
        createDefaultAdapter();
    }

    public void createDefaultAdapter() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        okBuilder = new OkHttpClient.Builder();
        okBuilder.readTimeout(5, TimeUnit.SECONDS);
        okBuilder.connectTimeout(5, TimeUnit.SECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okBuilder.addInterceptor(logging);
        okBuilder.addInterceptor(authInterceptor);


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
}

