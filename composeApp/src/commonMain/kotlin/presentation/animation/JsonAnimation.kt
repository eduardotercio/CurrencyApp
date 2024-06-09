package presentation.animation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import currencyapptest.composeapp.generated.resources.Res
import io.github.alexzhirkevich.compottie.LottieAnimation
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.LottieConstants
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun JsonAnimation(
    jsonPath: String,
    iterations: Int = LottieConstants.IterateForever,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    reverseOnRepeat: Boolean = false,
) {
    var jsonString by remember { mutableStateOf("") }

    val composition = rememberLottieComposition(
        LottieCompositionSpec.JsonString(jsonString)
    )

    LaunchedEffect(Unit) {
        jsonString = Res.readBytes(jsonPath).decodeToString()
    }

    val progress by animateLottieCompositionAsState(
        composition = composition.value,
        iterations = iterations,
        reverseOnRepeat = reverseOnRepeat
    )

    LottieAnimation(
        composition = composition.value,
        progress = { progress },
        modifier = modifier,
        contentScale = contentScale,
    )
}