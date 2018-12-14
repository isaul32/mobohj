package li.sau.projectwork.data.sources

import androidx.paging.ItemKeyedDataSource
import li.sau.projectwork.model.wp.blog.Post

class PostDataSource() : ItemKeyedDataSource<Int, Post>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Post>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Post>) {

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Post>) {

    }

    override fun getKey(item: Post): Int {
        return 0
    }

}