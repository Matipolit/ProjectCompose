package com.example.projectcompose.main

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.projectcompose.DataRepo
import com.example.projectcompose.R

class MainViewModel(private val repo: DataRepo): ViewModel() {
    var uiState by mutableStateOf(MainUiState())
        private set

    fun updateUiState() {
        uiState = MainUiState(imageSource = when(repo.getImgNum()){
            2 -> R.drawable.movie_2
            else -> R.drawable.movie_1
        })
    }

}
data class MainUiState(

    val imageSource: Int = R.drawable.movie_1
)
