package li.sau.exercise5

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask




class BookRepository(app: Application) {
    private val bookDao: BookDao
    private val allBooks: LiveData<List<Book>>

    init {
        val db: AppDatabase = AppDatabase.getInstance(app)!!
        bookDao = db.bookDao()
        allBooks = bookDao.getAllOrderByName()
    }

    fun getAllBooks() : LiveData<List<Book>> {
        return bookDao.getAllOrderByName()
    }

    fun insert(book: Book) {
        InsertAsyncTask(bookDao).execute(book)
    }

    fun delete(book: Book) {
        DeleteAsyncTask(bookDao).execute(book)
    }

    private class InsertAsyncTask internal constructor(
            private val asyncTaskDao: BookDao
    ) : AsyncTask<Book, Void, Void>() {

        override fun doInBackground(vararg params: Book): Void? {
            asyncTaskDao.insert(params[0])
            return null
        }
    }

    private class DeleteAsyncTask internal constructor(
            private val asyncTaskDao: BookDao
    ) : AsyncTask<Book, Void, Void>() {

        override fun doInBackground(vararg params: Book): Void? {
            asyncTaskDao.delete(params[0])
            return null
        }
    }
}