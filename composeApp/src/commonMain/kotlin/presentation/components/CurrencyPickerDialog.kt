package presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import domain.model.Currency
import domain.model.CurrencyType

@Composable
fun CurrencyPickerDialog(
    currencies: List<Currency>,
    onCurrencySelected: (CurrencyType) -> Unit,
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