package li.sau.exercise5

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView


class BookListAdapter internal constructor(context: Context) : RecyclerView.Adapter<BookListAdapter.BookViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var books: List<Book>? = null

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val BookItemView: TextView = itemView.findViewById(R.id.textView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false)
        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val current = books!![position]
        holder.BookItemView.text = current.name
    }

    internal fun setBooks(books: List<Book>) {
        this.books = books
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (books != null)
            books!!.size
        else
            0
    }
}
