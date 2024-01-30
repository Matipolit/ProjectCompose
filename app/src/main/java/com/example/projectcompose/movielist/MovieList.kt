package com.example.projectcompose.movielist

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.projectcompose.DataRepo

@Composable
fun MovieList(){
     Text(text = "Movie list")
     FloatingActionButton(onClick = { /*TODO*/ }) {
         Icon(Icons.Filled.Add, contentDescription = "Add")
     }
}