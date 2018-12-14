package li.sau.projectwork.rest;

import java.util.List;

import li.sau.projectwork.model.wp.Tag;
import li.sau.projectwork.model.wp.User;
import li.sau.projectwork.model.wp.media.Media;
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

        @GET("/wp-json/wp/v2/posts?lang=en&per_page=100")
        Call<List<Post>> getPosts();

        @GET("/wp-json/wp/v2/posts?per_page=100")
        Call<List<Post>> getPosts(@Query("page") Long page);

        @GET("/wp-json/wp/v2/posts/{postId}")
        Call<Post> getPost(@Path("postId") Long postId);

        @GET("/wp-json/wp/v2/users/{userId}")
        Call<User> getUser(@Path("userId") Long userId);

        // Todo use this instead _embed
        @GET("/wp-json/wp/v2/media/{mediaId}")
        Call<Media> getMedia(@Path("mediaId") Long mediaId);

        @GET("/wp-json/wp/v2/tags")
        Call<List<Tag>> getTags(@Query("post") Long postId);
    }

}