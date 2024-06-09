package presentation.screen.splash

import Screen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import presentation.animation.JsonAnimation
import util.koinViewModel

@Composable
fun SplashScreen(navController: NavController) {
    val viewModel = koinViewModel<SplashScreenViewModel>()

    LaunchedEffect(Unit) {
        viewModel.setEvent(SplashScreenContract.Event.LoadSplash)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SplashScreenContract.Effect.NavigateToHome -> {
                    navController.navigate(Screen.Home.route)
                }
            }
        }
    }

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(60.dp)
        )
        {
            JsonAnimation(
                jsonPath = ("files/kotlin_android_anim.json"),
                reverseOnRepeat = true,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}