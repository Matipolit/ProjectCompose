package com.example.projectcompose.settings;

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.projectcompose.DataRepo;
import androidx.lifecycle.ViewModel;

class SettingsViewModel(private val repo: DataRepo): ViewModel() {
    var uiState by mutableStateOf(SettingsUiState())
        private set

    fun updateUiState(newImageInt: Int) {
        uiState = SettingsUiState(image = newImageInt)
        repo.saveImgNum(newImageInt)
        }
}

data class SettingsUiState(
        val image: Int = 1
)