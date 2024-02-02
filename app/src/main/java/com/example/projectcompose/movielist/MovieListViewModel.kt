package com.example.projectcompose.movielist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.projectcompose.DataRepo
import com.example.projectcompose.Movie
import com.example.projectcompose.settings.SettingsUiState
import kotlinx.coroutines.flow.Flow

class MovieListViewModel(private val repo: DataRepo): ViewModel() {
    var movieListState by mutableStateOf(MovieListUiState(repo.getData()))
        private set
}

data class MovieListUiState(
    val movieList: Flow<List<Movie>>
)