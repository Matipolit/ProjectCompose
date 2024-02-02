package com.example.projectcompose.moviedetails

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.projectcompose.DataRepo
import com.example.projectcompose.movieentry.MovieDetails
import com.example.projectcompose.movieentry.toMovieDetails

class MovieDetailsViewModel (private val dataRepo: DataRepo): ViewModel(){
    var movieDetailsUiState by mutableStateOf(MovieDetailsUiState())
        private set

    fun populateData(movieId: Int) {
        Log.i("MovieDetailsViewModel", "getting movie: $movieId")
        val movie = dataRepo.getItem(movieId)
        movieDetailsUiState = MovieDetailsUiState(
            movieDetails = movie.toMovieDetails(),
        )
    }
}
data class MovieDetailsUiState(
    val movieDetails: MovieDetails = MovieDetails(),
)
