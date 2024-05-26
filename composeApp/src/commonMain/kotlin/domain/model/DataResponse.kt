package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DataResponse(
    val meta: MetaData,
    val data: List<Currency>
)
