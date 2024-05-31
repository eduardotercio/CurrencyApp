package data.mapper

import data.model.ApiResponse
import domain.mapper.ServiceResponseMapper
import domain.model.CurrencyCode
import domain.model.DataResponse

class ServiceResponseMapperImpl : ServiceResponseMapper {
    override fun mapApiToData(apiResponse: ApiResponse): DataResponse {
        val currencyCodeNames = CurrencyCode.entries.map { it.name }.toSet()
        val availableCurrencies =
            apiResponse.data.values.filter { currencyCodeNames.contains(it.code) }

        return DataResponse(
            meta = apiResponse.meta,
            data = availableCurrencies
        )
    }
}