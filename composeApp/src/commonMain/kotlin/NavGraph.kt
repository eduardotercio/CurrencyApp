import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import presentation.screen.home.HomeScreen
import presentation.screen.splash.SplashScreen

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
}

@Composable
fun NavGraph(startDestination: String = Screen.Splash.route) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
    }
}