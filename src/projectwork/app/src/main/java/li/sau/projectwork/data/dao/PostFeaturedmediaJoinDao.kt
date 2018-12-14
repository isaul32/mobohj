package li.sau.projectwork.data.dao

import androidx.room.*
import li.sau.projectwork.model.wp.blog.Featuredmedia
import li.sau.projectwork.model.wp.blog.Post
import li.sau.projectwork.model.wp.blog.PostFeaturedmediaJoin

@Dao
interface PostFeaturedmediaJoinDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllJoin(postFeaturedmediaJoin: List<PostFeaturedmediaJoin>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPosts(posts: List<Post>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMedia(featuredmedias: List<Featuredmedia>)

    @Transaction
    fun insertAllWithMedia(posts: List<Post>) {
        val joins: ArrayList<PostFeaturedmediaJoin> = ArrayList()
        posts.forEach { post ->
            val featuredmedia = post.embedded.featuredmedia

            insertAllMedia(featuredmedia)

            featuredmedia.forEach { media ->
                joins.add(PostFeaturedmediaJoin(post.id, media.id))
            }

            if (featuredmedia.isNotEmpty()) {
                post.thumbnail_url = featuredmedia.first().source_url
            }

            val author = post.embedded.author
            if (author.isNotEmpty()) {
                post.author_name = author.first().name
            }
        }

        insertAllPosts(posts)

        insertAllJoin(joins)
    }

    /*@Query("""
        SELECT * FROM post
        INNER JOIN post_featuredmedia_join ON post_featuredmedia_join.post_id = post.id
        INNER JOIN featuredmedia ON featuredmedia.id = post_featuredmedia_join.featuredmedia_id
    """)
    fun getAllWithMedia(): DataSource.Factory<Int, PostWithFeaturedmedia>*/
}