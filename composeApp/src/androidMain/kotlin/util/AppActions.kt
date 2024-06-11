package util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun appActions(): AppActions {
    val context = LocalContext.current
    val activity = context.getActivity()

    return object : AppActions {
        @Composable
        override fun finishApp() {
            activity?.finish()
        }
    }
}

