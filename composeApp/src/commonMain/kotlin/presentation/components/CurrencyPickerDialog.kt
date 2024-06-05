package presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import domain.model.Currency
import domain.model.CurrencyCode
import domain.model.CurrencyType
import primaryColor
import surfaceColor
import textColor

@Composable
fun CurrencyPickerDialog(
    currenciesList: List<Currency>,
    currencyType: CurrencyType,
    onDismiss: () -> Unit,
    onConfirmSelected: (CurrencyCode) -> Unit
) {
    val currenciesToShow by remember(currenciesList) {
        mutableStateOf(currenciesList.toMutableStateList())
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedCurrencyCode by remember(currencyType) {
        mutableStateOf(currencyType.code)
    }

    AlertDialog(
        containerColor = surfaceColor,
        title = {
            Text(
                text = "Select a Currency",
                color = textColor
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(100.dp)),
                    value = searchQuery,
                    onValueChange = { query ->
                        searchQuery = query.uppercase()

                        val queryList = if (query.isNotEmpty()) {
                            currenciesToShow.filter {
                                it.code.contains(searchQuery)
                            }
                        } else currenciesList

                        currenciesToShow.apply {
                            clear()
                            addAll(queryList)
                        }

                    },
                    placeholder = {
                        Text(
                            text = "Search here",
                            color = textColor.copy(alpha = 0.38f),
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        )
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = textColor,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = textColor.copy(alpha = 0.1f),
                        unfocusedContainerColor = textColor.copy(alpha = 0.1f),
                        disabledContainerColor = textColor.copy(alpha = 0.1f),
                        errorContainerColor = textColor.copy(alpha = 0.1f),
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = textColor,
                    )
                )
                AnimatedContent(
                    targetState = currenciesToShow
                ) { list ->
                    LazyColumn(
                        modifier = Modifier
                            .height(250.dp)
                    ) {
                        items(list) { currency ->
                            val code = CurrencyCode.valueOf(currency.code)
                            val isSelected = selectedCurrencyCode.name == currency.code

                            val saturation = remember { Animatable(if (isSelected) 1f else 0f) }

                            LaunchedEffect(isSelected) {
                                saturation.animateTo(if (isSelected) 1f else 0f)
                            }

                            val flagColorMatrix = remember(saturation.value) {
                                ColorMatrix().apply {
                                    setToSaturation(saturation.value)
                                }
                            }

                            val textAlpha by animateFloatAsState(
                                targetValue = if (isSelected) 1f else 0.5f,
                                animationSpec = tween(durationMillis = 300)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(size = 8.dp))
                                    .clickable { selectedCurrencyCode = code }
                                    .padding(all = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CurrencyFlag(
                                    currencyCode = code,
                                    flagColorMatrix = flagColorMatrix,
                                    textAlpha = textAlpha,
                                )
                                CurrencyCodeSelector(
                                    isSelected = isSelected
                                )
                            }
                        }
                    }
                }
            }
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.outline
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmSelected(selectedCurrencyCode)
            }) {
                Text(
                    text = "Confirm",
                    color = primaryColor
                )
            }
        }
    )
}

@Composable
fun CurrencyItem() {
    
}

@Composable
private fun CurrencyCodeSelector(isSelected: Boolean = false) {
    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) primaryColor else textColor.copy(alpha = 0.1f),
        animationSpec = tween(durationMillis = 300)
    )
    Box(
        modifier = Modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(animatedColor),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                modifier = Modifier.size(12.dp),
                imageVector = Icons.Default.Check,
                contentDescription = "Checkmark icon",
                tint = surfaceColor
            )
        }
    }
}