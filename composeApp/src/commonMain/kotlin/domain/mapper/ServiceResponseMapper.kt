package domain.mapper

import data.model.ApiResponse
import domain.model.DataResponse

interface ServiceResponseMapper {

    fun mapApiToData(apiResponse: ApiResponse): DataResponse
}