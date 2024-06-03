package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ConversionCurrencies(
    val source: Currency = Currency().apply {
        code = "BRL"
        value = 5.2
    },
    val target: Currency = Currency().apply {
        code = "USD"
        value = 1.0
    }
)