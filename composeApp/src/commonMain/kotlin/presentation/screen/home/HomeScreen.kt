package presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import koinViewModel
import presentation.components.HomeHeader
import surfaceColor


@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = koinViewModel<HomeScreenViewModel>()
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
    ) {
        HomeHeader(
            state = state,
            onRatesRefresh = {
                viewModel.setEvent(HomeScreenContract.Event.RefreshData)
            }
        )
    }

}