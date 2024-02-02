package com.example.projectcompose.movieentry

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.projectcompose.DataRepo
import com.example.projectcompose.Genre
import com.example.projectcompose.Movie

class MovieEntryViewModel(private val dataRepo: DataRepo): ViewModel() {
    var movieEditUiState by mutableStateOf(MovieEditUiState())
        private set

    private var isExistingMovie: Boolean = false

    fun updateUiState(movieDetails: MovieDetails) {
        movieEditUiState = MovieEditUiState(
            movieDetails = movieDetails,
            isEntryValid = movieDetails.title.isNotBlank()
                    && movieDetails.director.isNotBlank()
        )
    }

    fun prepopulateData(movieId: Int) {
        val movie = dataRepo.getItem(movieId)
        movieEditUiState = MovieEditUiState(
            movieDetails = movie.toMovieDetails(),
            isEntryValid = true
        )
        isExistingMovie = true
    }

    suspend fun saveMovie() {
        if (movieEditUiState.isEntryValid) {
            if(isExistingMovie)
                dataRepo.editItem(movieEditUiState.movieDetails.toMovie())
            else
                dataRepo.addItem(movieEditUiState.movieDetails.toMovie())
        }
    }
}

data class MovieEditUiState(
    val movieDetails: MovieDetails = MovieDetails(),
    val isEntryValid: Boolean = false
)
data class MovieDetails(
    val id: Int? = 0,
    val title: String = "",
    val director: String = "",
    val year: String = "2024",
    val genre: Genre = Genre.ACTION,
    val rating: Float = 3.0f,
)

fun MovieDetails.toMovie(): Movie =
    if(id != null)
        Movie(
            id = id,
            director = director,
            title = title,
            year = year.toInt(),
            genre = genre,
            rating = rating
        )else
    Movie(
        director = director,
        title = title,
        year = year.toInt(),
        genre = genre,
        rating = rating
    )

fun Movie.toMovieDetails(): MovieDetails = MovieDetails(
    id = id,
    title = title,
    director = director,
    year = year.toString(),
    genre = genre,
    rating = rating
)