package li.sau.projectwork.workers

import android.content.Context
import androidx.work.Result
import androidx.work.Worker
import androidx.work.WorkerParameters

class DatabasePopulationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val TAG by lazy { DatabasePopulationWorker::class.java.simpleName }

    override fun doWork(): Result {
        // Example database population
        /*
        val plantType = object : TypeToken<List<Plant>>() {}.type
        var jsonReader: JsonReader? = null

        return try {
            val inputStream = applicationContext.assets.open(PLANT_DATA_FILENAME)
            jsonReader = JsonReader(inputStream.reader())
            val plantList: List<Plant> = Gson().fromJson(jsonReader, plantType)
            val database = AppDatabase.getInstance(applicationContext)
            database.plantDao().insertAll(plantList)
            Result.success()
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        } finally {
            jsonReader?.close()
        }
        */

        return Result.failure()
    }
}