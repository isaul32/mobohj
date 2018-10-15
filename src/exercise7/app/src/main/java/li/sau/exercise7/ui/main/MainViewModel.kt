package li.sau.exercise7.ui.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import li.sau.exercise7.Book
import li.sau.exercise7.BookRepository

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val bookRepository: BookRepository = BookRepository(app)
    private val allBooks: LiveData<List<Book>>

    init {
        allBooks = bookRepository.getAllBooks()
    }

    fun getAllBooks() : LiveData<List<Book>> {
        return allBooks
    }

    fun insert(book: Book) {
        bookRepository.insert(book)
    }

    fun remove(book: Book) {
        bookRepository.delete(book)
    }
}
