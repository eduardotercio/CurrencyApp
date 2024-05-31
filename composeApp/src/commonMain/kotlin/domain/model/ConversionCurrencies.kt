package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ConversionCurrencies(
    val source: Currency = Currency("BRL", 3.7),
    val target: Currency = Currency("USD", 5.4)
)