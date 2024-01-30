package com.hansoft.lepidopteran.database.trackerdatabase

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.launch

@Entity(tableName = "tracker_table")
data class Tracker(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "chemical") var chemical: String,
    @ColumnInfo(name = "target") var target: String,
    @ColumnInfo(name = "area") var area: String,
    @ColumnInfo(name = "effectiveness") var effectiveness: String,
    @ColumnInfo(name = "date") var date: String
)


@Dao
interface TrackerDao {
    @Query("SELECT * FROM tracker_table")
    fun getAllTracker(): LiveData<List<Tracker>>

    @Insert
    suspend fun insert(Tracker: Tracker)

    @Delete
    suspend fun delete(Tracker: Tracker)
}


@Database(entities = [Tracker::class], version = 1, exportSchema = false)
abstract class TrackerDatabase : RoomDatabase() {
    abstract fun TrackerDao(): TrackerDao

    companion object {
        @Volatile
        private var INSTANCE: TrackerDatabase? = null

        fun getDatabase(context: Context): TrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrackerDatabase::class.java,
                    "tracker_table"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}



class TrackerRepository(private val TrackerDao: TrackerDao) {

    val getAllTracker: LiveData<List<Tracker>> = TrackerDao.getAllTracker()

    suspend fun insert(Tracker: Tracker) {
        TrackerDao.insert(Tracker)
    }

    suspend fun delete(Tracker: Tracker) {
        TrackerDao.delete(Tracker)
    }
}



class TrackerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TrackerRepository
    val allTracker: LiveData<List<Tracker>>

    init {
        val TrackerDao = TrackerDatabase.getDatabase(application).TrackerDao()
        repository = TrackerRepository(TrackerDao)
        allTracker = repository.getAllTracker
    }

    fun insert(pest: Tracker) = viewModelScope.launch {
        repository.insert(pest)
    }

    fun delete(pest: Tracker) = viewModelScope.launch {
        repository.delete(pest)
    }
}
