package com.example.projectcompose.movieentry

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectcompose.AppViewModelProvider
import com.example.projectcompose.Genre
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import kotlinx.coroutines.launch


@Composable
fun MovieEntryScreen(
    navigateBack: () -> Unit,
    viewModel: MovieEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    movieId: Int? = null,
    setToolbarTitle: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        if (movieId != null) {
            viewModel.prepopulateData(movieId)
            setToolbarTitle("Edit Movie - " + viewModel.movieEditUiState.movieDetails.title)
        }else{
            setToolbarTitle("Add Movie")
        }
    }

    MovieEntryBody(
        movieEditUiState = viewModel.movieEditUiState,
        onItemValueChange = viewModel::updateUiState,
        onSaveClick = {
            scope.launch {
                viewModel.saveMovie()
                navigateBack()
            }
        },
        modifier = Modifier.padding(16.dp))
}

@Composable
fun MovieEntryBody(
    movieEditUiState: MovieEditUiState,
    onItemValueChange: (MovieDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(8.dp)
    ) {
        MovieInputForm(
            movieDetails = movieEditUiState.movieDetails,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = movieEditUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieInputForm(
    movieDetails: MovieDetails,
    modifier: Modifier = Modifier,
    onValueChange: (MovieDetails) -> Unit = {},
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        OutlinedTextField(
            value = movieDetails.title,
            onValueChange = { onValueChange(movieDetails.copy(title = it)) },
            label = { Text("Title") },
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = movieDetails.director,
            onValueChange = { onValueChange(movieDetails.copy(director = it)) },
            label = { Text("Director") },
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = movieDetails.year,
            onValueChange = { onValueChange(movieDetails.copy(year = it)) },
            label = { Text("Release Date") },
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
        )
        /*
        RatingBar(
            modifier = Modifier.fillMaxWidth(),
            value = movieDetails.rating,
            style = RatingBarStyle.Default,
            onValueChange = {onValueChange(movieDetails.copy(rating = it))},
            stepSize = StepSize.HALF,
            onRatingChanged = {onValueChange(movieDetails.copy(rating = it))}
        )
        */
        OutlinedTextField(
            value = movieDetails.rating.toString(),
            label = {Text("Rating")},
            enabled = enabled,
            onValueChange = {onValueChange(movieDetails.copy(rating = it.toFloat()))}
        )
        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded ,
            onExpandedChange = {expanded = !expanded},)
        {
            TextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = movieDetails.genre.name,
                onValueChange = {},
                label = {Text("Genre")},
                trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                Genre.entries.forEach { genre ->
                    DropdownMenuItem(
                        onClick = {
                            onValueChange(movieDetails.copy(genre = genre))
                            expanded = false
                    }, text = {Text(text = genre.name)})
                }
            }
        }
    }

}