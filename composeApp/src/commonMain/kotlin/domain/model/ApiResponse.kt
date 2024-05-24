package domain.model

data class ApiResponse(
    val meta: MetaData,
    val data: Map<String, Currency>
)

