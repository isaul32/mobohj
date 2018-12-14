package li.sau.projectwork

import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.lifecycle.LiveData
import li.sau.projectwork.data.blog.PostDao
import li.sau.projectwork.model.wp.blog.Post


class PostViewModel(postDao: PostDao) : ViewModel() {

    val postList: LiveData<PagedList<Post>>

    init {
        val config = PagedList.Config.Builder()
                .setPageSize(6)
                .setInitialLoadSizeHint(12)
                .setPrefetchDistance(12)
                .setEnablePlaceholders(true)
                .build()

        postList = LivePagedListBuilder(postDao.getAll(), config).build()
    }

}
