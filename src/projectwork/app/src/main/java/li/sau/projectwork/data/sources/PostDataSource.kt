package li.sau.projectwork.data.sources

import androidx.paging.ItemKeyedDataSource
import li.sau.projectwork.model.wp.blog.Post

class PostDataSource() : ItemKeyedDataSource<Long, Post>() {

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Post>) {
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Post>) {

    }

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Post>) {

    }

    override fun getKey(item: Post): Long = item.id

}