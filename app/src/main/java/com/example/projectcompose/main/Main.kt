package com.example.projectcompose.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectcompose.AppViewModelProvider
import com.example.projectcompose.R

@Composable
fun Main(viewModel: MainViewModel = viewModel(factory = AppViewModelProvider.Factory)){
    Column(modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        Text(text = "Film list", fontSize=24.sp)
        Image(
            painter = painterResource(id = viewModel.uiState.imageSource),
            contentDescription = "Hero image",
        )
        Text(text = "Author: Mateusz Polito")
    }
}