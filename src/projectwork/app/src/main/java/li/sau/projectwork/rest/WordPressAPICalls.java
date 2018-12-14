package li.sau.projectwork.rest;

import java.util.List;

import li.sau.projectwork.model.wp.User;
import li.sau.projectwork.model.wp.blog.Post;
import retrofit2.Call;

public class WordPressAPICalls {

    public static Call<List<Post>> getPosts() {
        return WordPressAPIServiceImpl
                .getWordPressAPIService()
                .getPosts();
    }

    public static Call<Post> getPost(Long id) {
        return WordPressAPIServiceImpl
                .getWordPressAPIService()
                .getPost(id);
    }

    public static Call<User> getUser(Long id) {
        return WordPressAPIServiceImpl
                .getWordPressAPIService()
                .getUser(id);
    }

}
