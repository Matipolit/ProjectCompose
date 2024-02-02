package com.example.projectcompose.settings

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectcompose.AppViewModelProvider
import com.example.projectcompose.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Settings(viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)){
    var tabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(pageCount = {
       2
    })
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        TabRow(selectedTabIndex = pagerState.currentPage,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Tab(selected = pagerState.currentPage == 0, onClick = { scope.launch{pagerState.animateScrollToPage(0)} }) {
                Text(text = "Image 1", Modifier.padding(8.dp))
            }
            Tab(selected = pagerState.currentPage == 1, onClick = { scope.launch{pagerState.animateScrollToPage(1)} }) {
                Text(text = "Image 2", Modifier.padding(8.dp))
            }
        }
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { page ->
            Card(modifier = Modifier
                .padding(16.dp)) {
                Image(modifier = Modifier.fillMaxWidth()
                    .height(312.dp)
                    .padding(16.dp)
                    .clip(shape = MaterialTheme.shapes.medium),
                    painter = painterResource(when(page){
                        0 -> R.drawable.movie_1
                        1 -> R.drawable.movie_2
                        else -> R.drawable.movie_1
                    }),
                    contentDescription = "Hero image"
                )
            }
        }
        Button(onClick = {
            viewModel.updateSettingsState(pagerState.currentPage)
            Toast.makeText(context, "Image set to " + (pagerState.currentPage + 1).toString(), Toast.LENGTH_SHORT).show()
        }){
           Text(text = "Set image " + (pagerState.currentPage + 1).toString())
        }
    }



}
