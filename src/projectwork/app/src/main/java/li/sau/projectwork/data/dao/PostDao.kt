package li.sau.projectwork.data.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import li.sau.projectwork.model.wp.blog.Post


/**
 * Data access object for blog post and prove CRUD operations
 */
@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<Post>)

    @Insert
    fun insert(post: Post)

    @Update
    fun update(post: Post)

    @Transaction
    @Query("select * from post where id = :id")
    fun get(id: Long): LiveData<Post>

    @Delete
    fun delete(post: Post)

    @Query("DELETE FROM post")
    fun deleteAll()

    @Transaction
    @Query("SELECT * FROM post WHERE en = id ORDER BY date DESC")
    fun getAll(): DataSource.Factory<Int, Post>

}