package li.sau.projectwork.data

import androidx.lifecycle.LiveData
import androidx.room.*
import li.sau.projectwork.model.BlogPost

/**
 * Data access object for blog post and prove CRUD operations
 */
@Dao
interface BlogPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<BlogPost>)

    @Insert
    fun insert(post: BlogPost)

    @Update
    fun update(post: BlogPost)

    @Query("select * from blogpost")
    fun getAll(): LiveData<List<BlogPost>>

    @Query("select * from blogpost where id = :id")
    fun get(id: Long): LiveData<BlogPost>

    @Delete
    fun delete(post: BlogPost)

}