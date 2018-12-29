package li.sau.projectwork.data.sources

import androidx.paging.PageKeyedDataSource
import li.sau.projectwork.model.wp.blog.Post

class PostDataSource : PageKeyedDataSource<Long, Post>() {
    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Post>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Post>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Post>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}