package li.sau.projectwork.data.wp;

import java.util.List;

import li.sau.projectwork.model.blog.Post;
import retrofit2.Call;

public class WordPressAPICalls {

    public static Call<List<Post>> getBlogPosts() {
        return WordPressAPIServiceImpl
                .getWordPressAPIService()
                .getBlogPosts();
    }

}
