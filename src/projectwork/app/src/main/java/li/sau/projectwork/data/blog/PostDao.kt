package li.sau.projectwork.data.blog

import androidx.lifecycle.LiveData
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

    @Query("select * from post")
    fun getAll(): LiveData<List<Post>>

    @Query("select * from post where id = :id")
    fun get(id: Long): LiveData<Post>

    @Delete
    fun delete(post: Post)

}