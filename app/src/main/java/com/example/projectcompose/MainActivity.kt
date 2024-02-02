package com.example.projectcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projectcompose.main.Main
import com.example.projectcompose.moviedetails.MovieDetailsScreen
import com.example.projectcompose.movieentry.MovieEntryScreen
import com.example.projectcompose.movielist.MovieListScreen
import com.example.projectcompose.settings.Settings
import com.example.projectcompose.ui.theme.ProjectComposeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProjectComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    Navigation()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(){
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedOption by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    var canNavigateBack by remember { mutableStateOf(false) }
    var navigateBackTo by remember { mutableStateOf("")}

    data class NavItem(val route: String, val label: String, var icon: Int)
    val items = listOf(NavItem("main", "Home", R.drawable.ic_home), NavItem("settings", "Settings", R.drawable.ic_settings), NavItem("movie_list", "Movie List", R.drawable.icons8_film_reel_96))
    var toolbarTitle by remember { mutableStateOf("Main") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp))
                    .fillMaxWidth()){
                    Text(
                        "Movie list - compose",
                        fontWeight = FontWeight(600),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Divider()
                items.forEach { item ->
                    NavigationDrawerItem(
                        modifier = Modifier.padding(8.dp),
                        label = {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically){
                                Icon(modifier = Modifier.height(24.dp), painter = painterResource(item.icon), contentDescription = "Navigation icon")
                                Text(text = item.label)
                            }
                        },
                        selected = selectedOption == items.indexOf(item),
                        onClick = {
                            navController.navigate(item.route)
                            selectedOption = items.indexOf(item)
                            scope.launch{
                                drawerState.close()
                            }
                        }
                    )
                }
            }
        }
    ) {
        Column {
            TopAppBar(title = { Text(toolbarTitle)},
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(

                        onClick = {
                            when(canNavigateBack){
                                false -> scope.launch {
                                    drawerState.open()
                                }
                                true -> navController.navigate(navigateBackTo)
                            }
                        }
                    ){
                        Icon(
                            imageVector = when(canNavigateBack){
                                false -> Icons.Filled.Menu
                                true -> Icons.Filled.ArrowBack
                            },
                            contentDescription = "Menu"
                    )}
                },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
                )
            )
            NavHost(navController = navController, startDestination = "main") {
                composable("main", ) {
                    LaunchedEffect(Unit) {
                        toolbarTitle = "Home"
                    }

                    canNavigateBack = false
                    Main()
                }
                composable("settings") {
                    LaunchedEffect(Unit) {
                        toolbarTitle = "Settings"
                    }
                    canNavigateBack = false
                    Settings()
                }
                composable(
                    "movie_entry/{movieId}",
                    arguments = listOf(
                        navArgument("movieId") {
                            defaultValue = -1
                            type = NavType.IntType
                        })
                ){
                    val movieId = when(it.arguments?.getInt("movieId")){
                        -1 -> null
                        else -> it.arguments?.getInt("movieId")
                    }
                    canNavigateBack = true
                    navigateBackTo = "movie_list"
                    MovieEntryScreen(
                        navigateBack = {navController.navigate("movie_list")},
                        movieId = movieId,
                        setToolbarTitle = {title -> toolbarTitle = title}
                    )
                }
                composable(
                    "movie_details/{movieId}",
                    arguments = listOf(
                        navArgument("movieId") {
                            defaultValue = 0
                            type = NavType.IntType
                        })
                    )
                { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getInt("movieId")?: 0
                    canNavigateBack = true
                    navigateBackTo = "movie_list"
                    MovieDetailsScreen(
                        navigateToEdit = {navController.navigate("movie_entry/$movieId")},
                        navigateBack = {
                            navController.navigate("movie_list")
                       },
                        movieId = movieId,
                        setToolbarTitle = {title -> toolbarTitle = title}
                    )
                }
                composable("movie_list") {
                    LaunchedEffect(Unit){
                        toolbarTitle = "Movie List"
                    }
                    canNavigateBack = false
                    MovieListScreen(
                    navigateToItemEntry = {navController.navigate("movie_entry/-1")},
                    navigateToItemDetails = {id -> navController.navigate("movie_details/${id}")},
                ) }
            }
        }

    }
}


