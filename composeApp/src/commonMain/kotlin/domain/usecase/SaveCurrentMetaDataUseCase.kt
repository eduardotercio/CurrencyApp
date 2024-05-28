package domain.usecase

interface SaveCurrentMetaDataUseCase {

    suspend operator fun invoke(lastTimeUpdated: String)
}