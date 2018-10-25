package li.sau.exercise7

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.text.DateFormat
import java.util.*

class BookListAdapter internal constructor(context: Context) : RecyclerView.Adapter<BookListAdapter.BookViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val books: ArrayList<Book> = arrayListOf()

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val nameView: TextView = itemView.findViewById(R.id.name)
        internal val isbnView: TextView = itemView.findViewById(R.id.isbn)
        internal val yearView: TextView = itemView.findViewById(R.id.year)
        internal val dateView: TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): BookListAdapter.BookViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val current = books.get(position)

        // Show name
        holder.nameView.text = current.name

        // Show ISBN
        holder.isbnView.text = current.isbn

        // Show year of publication
        if (current.yearOfPublication != null) {
            holder.yearView.text = current.yearOfPublication.toString()
        } else {
            holder.yearView.text = ""
        }

        // Show date of acquisition
        if (current.dateOfAcquisition != null) {
            holder.dateView.text = DateFormat
                    .getDateInstance(DateFormat.SHORT, Locale.getDefault())
                    .format(current.dateOfAcquisition)
        } else {
            holder.dateView.text = ""
        }
    }

    internal fun addBook(book: Book) {
        books.add(book)
        notifyItemInserted(books.size - 1)
    }

    internal fun getBook(index: Int): Book {
        return books[index]
    }

    internal fun removeBook(book: Book) {

        // Should use iterator
        var position = -1
        books.forEachIndexed { index, saved ->
            if (book.id.equals(saved.id)) {
                position = index
            }
        }

        if (position >= 0) {
            removeBook(position)
        }
    }

    internal fun removeBook(bookPosition: Int) {
        books.removeAt(bookPosition)
        notifyItemRemoved(bookPosition)
    }

    internal fun removeBooks() {
        books.clear()
    }

    override fun getItemCount() = books.size

}