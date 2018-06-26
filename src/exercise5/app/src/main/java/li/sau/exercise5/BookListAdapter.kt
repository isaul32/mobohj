package li.sau.exercise5

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.support.v7.util.DiffUtil




class BookListAdapter internal constructor(context: Context) : RecyclerView.Adapter<BookListAdapter.BookViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var books: List<Book>? = null

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val nameView: TextView = itemView.findViewById(R.id.name)
        internal val isbnView: TextView = itemView.findViewById(R.id.isbn)
        internal val yearView: TextView = itemView.findViewById(R.id.year)
        internal val dateView: TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false)
        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        if (books != null) {
            val current = books!![position]
            holder.nameView.text = current.name
            holder.isbnView.text = current.isbn
            holder.yearView.text = current.yearOfPublication.toString()
            // Todo: use date formatter
            holder.dateView.text = current.dateOfAcquisition.toString()
        }
    }

    internal fun setBooks(books: List<Book>) {
        //this.books = books
        if (this.books != null) {
            val bookDiffCallback = BookDiffCallback(this.books!!, books)
            val diffResult = DiffUtil.calculateDiff(bookDiffCallback)


            this.books = books
            diffResult.dispatchUpdatesTo(this)
        } else {
            this.books = books
        }
        //notifyDataSetChanged()
    }

    fun getBook(index: Int): Book? {
        return books?.get(index)
    }

    override fun getItemCount(): Int {
        return if (books != null)
            books!!.size
        else
            0
    }
}