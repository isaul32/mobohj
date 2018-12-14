package li.sau.projectwork.rest;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static li.sau.projectwork.utils.ConstantsKt.BASE_URI;

public class WordPressAPIClient {

    private static Retrofit client;

    static synchronized Retrofit getApiClient() {
        Moshi moshi = new Moshi.Builder().add(new KotlinJsonAdapterFactory()).build();
        if (client == null) {
            client = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URI)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build();
        }

        return client;
    }
}
