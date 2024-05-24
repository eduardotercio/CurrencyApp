package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val meta: MetaData,
    val data: Map<String, Currency>
)

