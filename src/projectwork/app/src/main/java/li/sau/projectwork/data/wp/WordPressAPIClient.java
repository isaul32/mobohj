package li.sau.projectwork.data.wp;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class WordPressAPIClient {

    private static Retrofit client;

    static synchronized Retrofit getApiClient() {
        Moshi moshi = new Moshi.Builder().add(new KotlinJsonAdapterFactory()).build();
        if (client == null) {
            client = new Retrofit
                    .Builder()
                    .baseUrl("https://gofore.com/")
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build();
        }

        return client;
    }
}
