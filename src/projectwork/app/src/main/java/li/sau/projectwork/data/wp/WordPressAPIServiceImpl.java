package li.sau.projectwork.data.wp;

import java.util.List;

import li.sau.projectwork.model.wp.Tag;
import li.sau.projectwork.model.wp.User;
import li.sau.projectwork.model.wp.blog.Post;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
        Call<List<Post>> getPosts();

        @GET("/wp-json/wp/v2/posts/{postId}")
        Call<Post> getPost(@Path("postId") String postId);

        @GET("/wp-json/wp/v2/users/{userId}")
        Call<User> getUser(@Path("userId") String userId);

        @GET("/wp-json/wp/v2/tags")
        Call<List<Tag>> getTags(@Query("post") String postId);
    }

}