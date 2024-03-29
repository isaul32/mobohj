package li.sau.projectwork.rest;

import java.util.List;

import li.sau.projectwork.model.wp.User;
import li.sau.projectwork.model.wp.media.Media;
import li.sau.projectwork.model.wp.blog.Post;
import retrofit2.Call;

public class WordPressAPICalls {

    public static Call<List<Post>> getPosts(Integer perPage) {
        return WordPressAPIServiceImpl
                .getWordPressAPIService()
                .getPosts(perPage);
    }

    public static Call<Post> getPost(Long id) {
        return WordPressAPIServiceImpl
                .getWordPressAPIService()
                .getPost(id);
    }

    public static Call<Media> getMedia(Long mediaId) {
        return WordPressAPIServiceImpl
                .getWordPressAPIService()
                .getMedia(mediaId);
    }

    public static Call<User> getUser(Long id) {
        return WordPressAPIServiceImpl
                .getWordPressAPIService()
                .getUser(id);
    }

}
