package li.sau.projectwork.view

import androidx.annotation.MainThread
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import li.sau.projectwork.model.wp.blog.Post
import li.sau.projectwork.rest.WordPressAPIServiceImpl
import li.sau.projectwork.utils.createStatusLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class PostBoundaryCallback(
        private val webservice: WordPressAPIServiceImpl.WordPressAPIService,
        private val handleResponse: (List<Post>?) -> Unit,
        private val ioExecutor: Executor,
        private val networkPageSize: Int
) : PagedList.BoundaryCallback<Post>() {

    val helper = PagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()

    @MainThread
    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            webservice.getPosts(networkPageSize)
                    .enqueue(createWebserviceCallback(it))
        }
    }

    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: Post) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            webservice.getPostsBefore(networkPageSize, itemAtEnd.date)
                    .enqueue(createWebserviceCallback(it))
        }
    }

    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback)
            : Callback<List<Post>> {
        return object : Callback<List<Post>> {
            override fun onFailure(
                    call: Call<List<Post>>,
                    t: Throwable) {
                it.recordFailure(t)
            }

            override fun onResponse(
                    call: Call<List<Post>>,
                    response: Response<List<Post>>) {
                insertItemsIntoDb(response, it)
            }
        }
    }

    private fun insertItemsIntoDb(
            response: Response<List<Post>>,
            it: PagingRequestHelper.Request.Callback) {
        ioExecutor.execute {
            handleResponse(response.body())
            it.recordSuccess()
        }
    }

}