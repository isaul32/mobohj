package li.sau.projectwork.data.blog

import androidx.lifecycle.LiveData
import androidx.room.*
import li.sau.projectwork.model.wp.blog.Featuredmedia

@Dao
interface FeaturedmediaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(featuredmedias: List<Featuredmedia>)

    @Insert
    fun insert(featuredmedia: Featuredmedia)

    @Transaction
    @Query("select * from featuredmedia where id = :id")
    fun get(id: Long): LiveData<Featuredmedia>

    @Transaction
    @Query("SELECT * FROM featuredmedia ORDER BY id ASC")
    fun getAll(): List<Featuredmedia>

}