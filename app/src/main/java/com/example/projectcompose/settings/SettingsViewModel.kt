package com.example.projectcompose.settings;

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.projectcompose.DataRepo;
import androidx.lifecycle.ViewModel;

class SettingsViewModel(private val repo: DataRepo): ViewModel() {
    var settingsState by mutableStateOf(SettingsUiState())
        private set

    fun updateSettingsState(newImageInt: Int) {
        repo.saveImgNum(newImageInt)
    }
}

data class SettingsUiState(
    var image: Int = 0
)