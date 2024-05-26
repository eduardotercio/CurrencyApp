package presentation.screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.serialization.Serializable



@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = viewModel<HomeScreenViewModel>()

}