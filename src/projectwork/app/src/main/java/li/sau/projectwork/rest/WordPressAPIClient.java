package li.sau.projectwork.rest;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;

import li.sau.projectwork.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static li.sau.projectwork.utils.ConstantsKt.BASE_URI;

public class WordPressAPIClient {

    private static Retrofit client;

    static synchronized Retrofit getApiClient() {
        Moshi moshi = new Moshi.Builder().add(new KotlinJsonAdapterFactory()).build();
        if (client == null) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BASE_URI)
                    .addConverterFactory(MoshiConverterFactory.create(moshi));

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
                builder.client(new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .build());
            }

            client = builder.build();
        }

        return client;
    }
}
