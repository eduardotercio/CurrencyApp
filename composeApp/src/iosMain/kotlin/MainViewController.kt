import androidx.compose.ui.window.ComposeUIViewController
import di.KoinInitializer

fun MainViewController() = ComposeUIViewController(
    configure = {
        KoinInitializer().init()
    }
) {
//    HomeScreen(navController) // This will need to be adjusted for iOS navigation
    App()
}