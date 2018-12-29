package li.sau.projectwork.view.models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.lifecycle.LiveData
import androidx.paging.toLiveData
import li.sau.projectwork.data.PostBoundaryCallback
import li.sau.projectwork.data.dao.PostDao
import li.sau.projectwork.model.wp.blog.Post
import li.sau.projectwork.rest.WordPressAPICalls
import li.sau.projectwork.rest.WordPressAPIServiceImpl
import java.io.IOException
import java.util.concurrent.Executors


class BlogViewModel(private val postDao: PostDao) : ViewModel() {

    private val TAG by lazy { BlogViewModel::class.java.simpleName }

    val postList: LiveData<PagedList<Post>>

    init {
        val boundaryCallback = PostBoundaryCallback(
                webservice = WordPressAPIServiceImpl.getWordPressAPIService(),
                handleResponse = this::insertResultIntoDb,
                ioExecutor = Executors.newSingleThreadExecutor(),
                networkPageSize = 20
        )

        postList = postDao.getAll().toLiveData(
                pageSize = 10,
                boundaryCallback = boundaryCallback
        )
    }

    private fun insertResultIntoDb(body: List<Post>?) {
        body?.let { posts ->

            posts.forEach { post ->
                // Get media for post
                try {
                    val mediaRes = WordPressAPICalls
                            .getMedia(post.featured_media)
                            .execute()

                    if (mediaRes.isSuccessful) {
                        val media = mediaRes.body()
                        post.thumbnail_url = media?.media_details?.sizes?.medium?.source_url
                    }
                } catch (e: IOException) {
                    Log.e(TAG, e.localizedMessage, e)
                }

                // Get author for post
                try {
                    val userRes = WordPressAPICalls
                            .getUser(post.author)
                            .execute()
                    if (userRes.isSuccessful) {
                        val author = userRes.body()
                        post.author_name = author?.name
                    }
                } catch (e: IOException) {
                    Log.e(TAG, e.localizedMessage, e)
                }
            }

            // Todo: This might replace some posts. A better solution would be to insert only new posts.
            postDao.insertAll(posts)

        }
    }

}
