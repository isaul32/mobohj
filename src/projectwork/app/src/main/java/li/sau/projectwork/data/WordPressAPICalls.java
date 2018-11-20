package li.sau.projectwork.data;

import java.util.List;

import li.sau.projectwork.model.BlogPost;
import retrofit2.Call;

public class WordPressAPICalls {

    public static Call<List<BlogPost>> getBlogPosts() {
        return WordPressAPIServiceImpl
                .getWordPressAPIService()
                .getBlogPosts();
    }

}
