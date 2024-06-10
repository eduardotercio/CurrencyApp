package domain.model

sealed class CurrencyType(
    val currencyCode: CurrencyCode?,
    val defaultCode: String,
    val key: String,
) {
    data class SourceCurrency(val source: CurrencyCode? = null) :
        CurrencyType(
            currencyCode = source,
            defaultCode = SOURCE_CODE_VALUE_DEFAULT,
            key = SOURCE_CURRENCY_KEY
        )

    data class TargetCurrency(val target: CurrencyCode? = null) :
        CurrencyType(
            currencyCode = target,
            defaultCode = TARGET_CODE_VALUE_DEFAULT,
            key = TARGET_CURRENCY_KEY
        )

    fun isSource() = this is SourceCurrency

    fun isTarget() = this is TargetCurrency

    private companion object {
        const val SOURCE_CURRENCY_KEY = "source"
        const val TARGET_CURRENCY_KEY = "target"

        const val SOURCE_CODE_VALUE_DEFAULT = "BRL"
        const val TARGET_CODE_VALUE_DEFAULT = "USD"
    }
}