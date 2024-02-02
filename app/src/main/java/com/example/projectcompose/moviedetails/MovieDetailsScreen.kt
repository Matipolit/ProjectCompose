package com.example.projectcompose.moviedetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectcompose.AppViewModelProvider
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel= viewModel(factory = AppViewModelProvider.Factory),
    movieId: Int,
    navigateToEdit: (Int) -> Unit,
    navigateBack: () -> Unit,
    setToolbarTitle: (String) -> Unit,
) {
    LaunchedEffect(Unit){
        viewModel.populateData(movieId)
        setToolbarTitle("Movie Details - " + viewModel.movieDetailsUiState.movieDetails.title)
    }
    val movieDetails = viewModel.movieDetailsUiState.movieDetails
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)){
        Text("Title: " + movieDetails.title)
        Text("Year: " + movieDetails.year)
        Text("Director: " + movieDetails.director)
        Text("Genre: " + movieDetails.genre)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
            Text("Rating")
            RatingBar(
                modifier = Modifier.fillMaxWidth(),
                value = movieDetails.rating,
                style = RatingBarStyle.Default,
                onValueChange = {},
                stepSize = StepSize.HALF,
                onRatingChanged = {}
            )
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
            Button(onClick = {navigateBack()}) {
                Text("Go back")
            }
            Button(onClick = {navigateToEdit(movieId)}) {
                Text("Edit")
            }
        }


    }
}