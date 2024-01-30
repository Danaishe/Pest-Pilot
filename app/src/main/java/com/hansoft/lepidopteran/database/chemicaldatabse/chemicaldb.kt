package com.hansoft.lepidopteran.database.chemicaldatabse

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

@Entity(tableName = "chemical_table")
data class Chemical(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "target") var target: String,
    @ColumnInfo(name = "rate") var rate: String,
    @ColumnInfo(name = "icon") var icon: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "scientificname") var scientificname: String
)


@Dao
interface ChemicalDao {
    @Query("SELECT * FROM chemical_table")
    fun getAllChemical(): LiveData<List<Chemical>>

    @Insert
    suspend fun insert(Chemical: Chemical)

    @Delete
    suspend fun delete(Chemical: Chemical)
}


@Database(entities = [Chemical::class], version = 1, exportSchema = false)
abstract class ChemicalDatabase : RoomDatabase() {
    abstract fun ChemicalDao(): ChemicalDao

    companion object {
        @Volatile
        private var INSTANCE: ChemicalDatabase? = null

        fun getDatabase(context: Context): ChemicalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChemicalDatabase::class.java,
                    "chemical_table"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}



class PestRepository(private val ChemicalDao: ChemicalDao) {

    val getAllChemical: LiveData<List<Chemical>> = ChemicalDao.getAllChemical()

    suspend fun insert(Chemical: Chemical) {
        ChemicalDao.insert(Chemical)
    }

    suspend fun delete(Chemical: Chemical) {
        ChemicalDao.delete(Chemical)
    }
}



class ChemicalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PestRepository
    val allChemical: LiveData<List<Chemical>>

    init {
        val ChemicalDao = ChemicalDatabase.getDatabase(application).ChemicalDao()
        repository = PestRepository(ChemicalDao)
        allChemical = repository.getAllChemical
    }

    fun insert(pest: Chemical) = viewModelScope.launch {
        repository.insert(pest)
    }

    fun delete(pest: Chemical) = viewModelScope.launch {
        repository.delete(pest)
    }
}
