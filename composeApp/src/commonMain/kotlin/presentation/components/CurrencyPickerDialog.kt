package presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import domain.model.Currency

@Composable
fun CurrencyPickerDialog(
    currencies: List<Currency>,
    onCurrencySelected: (Currency) -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {

        },
        confirmButton = {

        }
    )
}