package presentation.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import koinViewModel


@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = koinViewModel<HomeScreenViewModel>()

    viewModel.setEvent(HomeScreenContract.Event.GetString)

}