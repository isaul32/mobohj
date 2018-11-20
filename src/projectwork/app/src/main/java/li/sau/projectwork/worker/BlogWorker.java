package li.sau.projectwork.worker;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import li.sau.projectwork.data.WordPressAPICalls;
import li.sau.projectwork.model.BlogPost;
import retrofit2.Response;

public class BlogWorker extends Worker {

    public BlogWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Response<List<BlogPost>> res = WordPressAPICalls.getBlogPosts().execute();
            if (res.isSuccessful()) {
                List<BlogPost> blogPosts = res.body();

                HashMap<String, Object> data = new HashMap<>();
                data.put("posts", blogPosts);

                Data output = new Data.Builder()
                        .putAll(data)
                        .build();
                setOutputData(output);
            }
        } catch (IOException ex) {
            Log.e("BlogWorker", ex.getLocalizedMessage(), ex);
            return Result.FAILURE;
        }

        return Result.SUCCESS;
    }



}
