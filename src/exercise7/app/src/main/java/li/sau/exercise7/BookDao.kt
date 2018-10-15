package li.sau.exercise7

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface BookDao {
    @Insert
    fun insert(book: Book)

    @Query("select * from book")
    fun getAll(): LiveData<List<Book>>

    @Query("select * from book order by name ASC")
    fun getAllOrderByName(): LiveData<List<Book>>

    @Query("select * from book where id = :id")
    fun findById(id: Long): Book

    @Update
    fun update(book: Book)

    @Delete
    fun delete(book: Book)
}