package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ConversionCurrencies(
    val source: Currency = Currency().apply {
        code = "BRL"
        value = 3.7
    },
    val target: Currency = Currency().apply {
        code = "USD"
        value = 5.4
    }
)