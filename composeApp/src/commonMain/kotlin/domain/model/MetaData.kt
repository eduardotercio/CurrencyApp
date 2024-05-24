package domain.model

import kotlinx.serialization.SerialName

data class MetaData(
    @SerialName("last_updated_at")
    val lastUpdate: String
)
