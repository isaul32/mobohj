package li.sau.projectwork.view.models

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import li.sau.projectwork.data.AppDatabase
import li.sau.projectwork.view.PostBoundaryCallback
import li.sau.projectwork.data.dao.PostDao
import li.sau.projectwork.model.wp.blog.Post
import li.sau.projectwork.rest.WordPressAPICalls
import li.sau.projectwork.rest.WordPressAPIServiceImpl
import li.sau.projectwork.utils.NETWORK_PAGE_SIZE
import li.sau.projectwork.utils.NetworkState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class BlogViewModel(private val db: AppDatabase) : ViewModel() {

    private val TAG by lazy { BlogViewModel::class.java.simpleName }

    val postList: LiveData<PagedList<Post>>
    val networkState: LiveData<NetworkState>
    val refreshState: LiveData<NetworkState>

    private val boundaryCallback: PostBoundaryCallback
    private val ioExecutor: Executor
    private val refreshTrigger: MutableLiveData<Unit>

    init {
        ioExecutor = Executors.newSingleThreadExecutor()
        boundaryCallback = PostBoundaryCallback(
                webservice = WordPressAPIServiceImpl.getWordPressAPIService(),
                handleResponse = this::insertResultIntoDb,
                ioExecutor = ioExecutor,
                networkPageSize = NETWORK_PAGE_SIZE
        )

        refreshTrigger = MutableLiveData()
        refreshState = Transformations.switchMap(refreshTrigger) {
            refreshData()
        }

        networkState = boundaryCallback.networkState

        postList = db.postDao().getAll().toLiveData(
                pageSize = 10,
                boundaryCallback = boundaryCallback
        )
    }

    fun retry() {
        boundaryCallback.helper.retryAllFailed()
    }

    fun refresh() {
        refreshTrigger.value = null
    }

    @MainThread
    fun refreshData(): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING

        WordPressAPIServiceImpl.getWordPressAPIService().getPosts(NETWORK_PAGE_SIZE).enqueue(
                object : Callback<List<Post>> {
                    override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                        networkState.value = NetworkState.error(t.message)
                    }

                    override fun onResponse(
                            call: Call<List<Post>>,
                            response: Response<List<Post>>) {
                        ioExecutor.execute {

                            // Run in transaction
                            db.runInTransaction {

                                // Remove all posts
                                db.postDao().deleteAll()

                                // Download all posts again
                                insertResultIntoDb(response.body())
                            }

                            // This is bg thread, so update the result now.
                            networkState.postValue(NetworkState.LOADED)
                        }
                    }
                })

        return networkState
    }

    private fun insertResultIntoDb(body: List<Post>?) {
        body?.let { posts ->

            // Todo: This could be optimized
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
            db.postDao().insertAll(posts)

        }
    }

}
