package li.sau.projectwork.data.wp;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class WordPressAPIClient {

    private static Retrofit client;

    static synchronized Retrofit getApiClient() {
        if (client == null) {
            client = new Retrofit
                    .Builder()
                    .baseUrl("https://gofore.com/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();
        }

        return client;
    }
}
