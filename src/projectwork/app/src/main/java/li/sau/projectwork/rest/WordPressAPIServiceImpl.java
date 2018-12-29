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

    public static synchronized WordPressAPIService getWordPressAPIService() {
        if (wordPressAPIService == null) {
            wordPressAPIService = WordPressAPIClient
                    .getApiClient()
                    .create(WordPressAPIService.class);
        }
        return wordPressAPIService;
    }

    public interface WordPressAPIService {

        @GET("/wp-json/wp/v2/posts?lang=en&orderby=date&order=desc")
        Call<List<Post>> getPosts(@Query("per_page") Integer perPage);

        @GET("/wp-json/wp/v2/posts?lang=en&orderby=date&order=desc")
        Call<List<Post>> getPostsBefore(
                @Query("per_page") Integer perPage,
                @Query("before") String before
        );

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