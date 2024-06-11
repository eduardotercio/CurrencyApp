package presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import snackbarColor

@Composable
fun CustomSnackbar(snackbarData: SnackbarData) {
    Box(
        modifier = Modifier
            .padding(32.dp)
            .background(
                color = snackbarColor,
                shape = MaterialTheme.shapes.extraLarge
            )
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = snackbarData.visuals.message,
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}