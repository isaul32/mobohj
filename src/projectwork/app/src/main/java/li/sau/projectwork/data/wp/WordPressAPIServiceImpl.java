package li.sau.projectwork.data.wp;

import java.util.List;

import li.sau.projectwork.model.blog.Post;
import retrofit2.Call;
import retrofit2.http.GET;

public class WordPressAPIServiceImpl {

    private static WordPressAPIService wordPressAPIService;

    static synchronized WordPressAPIService getWordPressAPIService() {
        if (wordPressAPIService == null) {
            wordPressAPIService = WordPressAPIClient
                    .getApiClient()
                    .create(WordPressAPIService.class);
        }
        return wordPressAPIService;
    }

    public interface WordPressAPIService {

        @GET("/wp-json/wp/v2/posts")
        Call<List<Post>> getBlogPosts();

    }

}