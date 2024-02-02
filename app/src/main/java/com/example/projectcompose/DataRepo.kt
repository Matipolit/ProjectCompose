package com.example.projectcompose;

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
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val director: String,
    val year: Int,
    val genre: Genre,
    val rating: Float,
)


@Dao
interface MovieDao {
    @Insert
    fun insertAll(vararg movies: Movie)

    @Delete
    fun delete(movie: Movie)

    @Query("SELECT * FROM movie ORDER BY title")
    fun getAll(): Flow<List<Movie>>
    //fun getAll(): LiveData<List<Film>>

    @Query("SELECT * FROM movie WHERE id = :id LIMIT 1")
    fun getMovie(id: Int): Movie

    @Update
    fun updateFilms(vararg movies: Movie)
}

@Database(entities = [Movie::class], version = 1)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun filmDao(): MovieDao
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase{
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "film_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }

}


class DataRepo(context: Context) {
    private var db: MovieDatabase
    private var dao: MovieDao
    //private var allFilms: LiveData<List<Film>>
    private var allFilms: Flow<List<Movie>>
    private var sharedPreferences: SharedPreferences

    init {
        db = MovieDatabase.getDatabase(context)
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
    fun getData(): Flow<List<Movie>> {
        return allFilms
    }

    fun getItem(id: Int): Movie {
        return dao.getMovie(id)
    }

    fun addItem(item: Movie) {
        dao.insertAll(item)
    }

    fun editItem(item: Movie) {
        dao.updateFilms(item)
    }

    fun deleteItems(movies: List<Movie>){
        movies.forEach {
            dao.delete(it)
        }
    }


}
