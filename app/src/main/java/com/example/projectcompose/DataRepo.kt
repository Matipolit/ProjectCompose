package com.example.projectcompose;

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


enum class Genre {
    HORROR, ACTION, COMEDY, DRAMA
}

@Entity
data class Film(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val year: Int,
    val genre: Genre,
    val rating: Float,
)


@Dao
interface FilmDao {
    @Insert
    fun insertAll(vararg films: Film)

    @Delete
    fun delete(film: Film)

    @Query("SELECT * FROM film ORDER BY title")
    fun getAll(): Flow<List<Film>>
    //fun getAll(): LiveData<List<Film>>

    @Update
    fun updateFilms(vararg films: Film)
}

@Database(entities = [Film::class], version = 1)
abstract class FilmDatabase: RoomDatabase() {
    abstract fun filmDao(): FilmDao
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: FilmDatabase? = null

        fun getDatabase(context: Context): FilmDatabase{
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FilmDatabase::class.java,
                    "film_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }

}


class DataRepo(context: Context) {
    private var db: FilmDatabase
    private var dao: FilmDao
    //private var allFilms: LiveData<List<Film>>
    private var allFilms: Flow<List<Film>>
    private var sharedPreferences: SharedPreferences

    init {
        db = FilmDatabase.getDatabase(context)
        dao = db.filmDao()
        allFilms = dao.getAll()
        sharedPreferences = context.getSharedPreferences("data", 0)
    }

    fun getImgNum(): Int{
        return sharedPreferences.getInt("image", 0)
    }

    fun saveImgNum(num: Int){
        sharedPreferences.edit().putInt("image", num).apply()
    }

    //fun getData(): LiveData<List<Film>> {
    fun getData(): Flow<List<Film>> {
        return allFilms
    }

    fun addItem(item: Film) {
        dao.insertAll(item)
    }

    fun editItem(item: Film) {
        dao.updateFilms(item)
    }

    fun deleteItems(films: List<Film>){
        films.forEach {
            dao.delete(it)
        }
    }


}
