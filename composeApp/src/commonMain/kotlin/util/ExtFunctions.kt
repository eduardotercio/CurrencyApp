package util

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.currentKoinScope


@Composable
inline fun <reified T : ViewModel> koinViewModel(): T {
    val scope = currentKoinScope()
    return viewModel {
        scope.get<T>()
    }
}
fun <T> CoroutineScope.debounce(
    waitMs: Long = 300L,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null

    return { param: T ->
        debounceJob?.cancel()
        debounceJob = launch {
            delay(waitMs)
            destinationFunction(param)
        }
    }
}