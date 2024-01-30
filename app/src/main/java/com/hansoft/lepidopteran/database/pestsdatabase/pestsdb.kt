package com.hansoft.lepidopteran.database.pestsdatabase

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

@Entity(tableName = "pest_table")
data class Pest(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "image") var image: String, // Consider storing as Uri or Base64
    @ColumnInfo(name = "common_name") var commonName: String,
    @ColumnInfo(name = "scientific_name") var scientificName: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "impact_level") var impactLevel: String
)


@Dao
interface PestDao {
    @Query("SELECT * FROM pest_table")
    fun getAllPests(): LiveData<List<Pest>>

    @Insert
    suspend fun insert(pest: Pest)

    @Delete
    suspend fun delete(pest: Pest)
}


@Database(entities = [Pest::class], version = 1, exportSchema = false)
abstract class PestDatabase : RoomDatabase() {
    abstract fun pestDao(): PestDao

    companion object {
        @Volatile
        private var INSTANCE: PestDatabase? = null

        fun getDatabase(context: Context): PestDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PestDatabase::class.java,
                    "pest_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}



class PestRepository(private val pestDao: PestDao) {

    val allPests: LiveData<List<Pest>> = pestDao.getAllPests()

    suspend fun insert(pest: Pest) {
        pestDao.insert(pest)
    }

    suspend fun delete(pest: Pest) {
        pestDao.delete(pest)
    }
}



class PestViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PestRepository
    val allPests: LiveData<List<Pest>>

    init {
        val pestDao = PestDatabase.getDatabase(application).pestDao()
        repository = PestRepository(pestDao)
        allPests = repository.allPests
    }

    fun insert(pest: Pest) = viewModelScope.launch {
        repository.insert(pest)
    }

    fun delete(pest: Pest) = viewModelScope.launch {
        repository.delete(pest)
    }
}
