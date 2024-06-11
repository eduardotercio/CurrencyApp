package util

import androidx.compose.runtime.Composable

@Composable
expect fun appActions(): AppActions


interface AppActions {
    @Composable
    fun finishApp()
}