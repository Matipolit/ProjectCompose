package com.example.projectcompose.movielist

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectcompose.AppViewModelProvider
import com.example.projectcompose.Genre
import com.example.projectcompose.Movie
import com.example.projectcompose.R
import kotlinx.coroutines.flow.Flow

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToItemEntry: () -> Unit,
    navigateToItemDetails: (Int) -> Unit,
){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToItemEntry) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ){ _ ->
        MovieList(
            movieList = viewModel.movieListState.movieList,
            movieOnClick =  navigateToItemDetails,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        )
    }
}

@Composable
private fun MovieList(
    movieList: Flow<List<Movie>>,
    movieOnClick: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    val movieItems = movieList.collectAsState(initial = emptyList()).value

    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)){
        items(items = movieItems, key = {movie -> movie.id}){ item ->
            MovieItem(movie = item, movieOnClick = movieOnClick)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieItem(movie: Movie, movieOnClick: (Int) -> Unit){
    Card(
        onClick = {
            movieOnClick(movie.id)
            Log.d("MovieItem", "Movie clicked: ${movie.id}")
        }){
        Row(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)){
                Icon(
                    when(movie.genre){
                        Genre.HORROR -> painterResource(R.drawable.ic_horror)
                        Genre.ACTION -> painterResource(R.drawable.ic_action)
                        Genre.COMEDY -> painterResource(R.drawable.ic_comedy)
                        Genre.DRAMA -> painterResource(R.drawable.ic_drama)
                    },
                    "Genre icon")
                Column(){
                    Text(text = movie.title, style = MaterialTheme.typography.bodyLarge)
                    Text(text = movie.year.toString(), style = MaterialTheme.typography.bodySmall)
                }
            }

            Text(text = "Rating: ${movie.rating}")
        }
    }

}