package li.sau.projectwork.view.models

import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.lifecycle.LiveData
import li.sau.projectwork.data.dao.PostDao
import li.sau.projectwork.model.wp.blog.Post


class PostViewModel(postDao: PostDao) : ViewModel() {

    val postList: LiveData<PagedList<Post>>

    init {
        val config = PagedList.Config.Builder()
                .setPageSize(2)
                .setInitialLoadSizeHint(4)
                .setPrefetchDistance(4)
                .setEnablePlaceholders(true)
                .build()

        val livePagedListBuilder = LivePagedListBuilder(postDao.getAll(), config)
        postList = livePagedListBuilder.build()
    }
}
