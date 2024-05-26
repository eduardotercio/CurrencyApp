package data.model

import domain.model.Currency
import domain.model.MetaData
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val meta: MetaData,
    val data: Map<String, Currency>
)

