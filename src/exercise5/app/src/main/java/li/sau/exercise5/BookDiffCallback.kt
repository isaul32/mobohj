package li.sau.exercise5

import android.support.v7.util.DiffUtil


internal class BookDiffCallback(private val oldBooks: List<Book>, private val newBooks: List<Book>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldBooks.size
    }

    override fun getNewListSize(): Int {
        return newBooks.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldBooks[oldItemPosition].id === newBooks[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldBooks[oldItemPosition].equals(newBooks[newItemPosition])
    }
}