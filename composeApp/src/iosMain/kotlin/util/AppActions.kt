package util

import androidx.compose.runtime.Composable

@Composable
actual fun appActions(): AppActions {

    return object : AppActions {
        @Composable
        override fun finishApp() {}
    }
}

