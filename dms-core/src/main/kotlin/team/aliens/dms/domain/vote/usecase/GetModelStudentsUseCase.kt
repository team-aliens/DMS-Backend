package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.vote.dto.response.ModelStudentListResponse
import team.aliens.dms.domain.vote.service.ModelStudentListService
import java.time.LocalDate

@UseCase
class GetModelStudentsUseCase(
    private val modelStudentListService: ModelStudentListService
) {
    fun execute(requestDate: LocalDate): List<ModelStudentListResponse> {
        return modelStudentListService.getModelStudentList(requestDate)
    }
}
