package li.sau.projectwork.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import li.sau.projectwork.data.blog.PostDao
import li.sau.projectwork.model.wp.blog.Post
import li.sau.projectwork.utils.DATABASE_NAME

@Database(entities = [Post::class], version = 7)
abstract class AppDatabase : RoomDatabase() {
    abstract fun blogPostDao(): PostDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    // If I want populate some data on creation
                    /*.addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<DatabasePopulationWorker>().build()
                            WorkManager.getInstance().enqueue(request)
                        }
                    })*/
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}