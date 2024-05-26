package data.mapper

import data.model.ApiResponse
import domain.mapper.ServiceResponseMapper
import domain.model.DataResponse

class ServiceResponseMapperImpl : ServiceResponseMapper {
    override fun mapApiToData(apiResponse: ApiResponse): DataResponse {
        return DataResponse(
            meta = apiResponse.meta,
            data = apiResponse.data.values.toList()
        )
    }
}