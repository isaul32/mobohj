package li.sau.projectwork.view.adapters

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import li.sau.projectwork.R
import li.sau.projectwork.ui.EventHandler
import li.sau.projectwork.view.holders.BlogViewHolder
import li.sau.projectwork.model.wp.blog.Post
import li.sau.projectwork.model.wp.blog.Post.Companion.DIFF_CALLBACK
import li.sau.projectwork.utils.NetworkState
import li.sau.projectwork.view.holders.NetworkStateHolder


class BlogAdapter(
        private val mEventListener: EventHandler,
        private val retryCallback: () -> Unit
) : PagedListAdapter<Post, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var mNetworkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_post -> BlogViewHolder.create(parent)
            R.layout.item_network_state -> NetworkStateHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { post ->
            val viewType = getItemViewType(position)
            when (viewType) {
                R.layout.item_post -> (holder as BlogViewHolder).bindTo(post, mEventListener)
                R.layout.item_network_state -> (holder as NetworkStateHolder).bindTo(mNetworkState)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_state
        } else {
            R.layout.item_post
        }
    }

    private fun hasExtraRow() = mNetworkState != null && mNetworkState != NetworkState.LOADED

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = mNetworkState
        val hadExtraRow = hasExtraRow()
        mNetworkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

}
