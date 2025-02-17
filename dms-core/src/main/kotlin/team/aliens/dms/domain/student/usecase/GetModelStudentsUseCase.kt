package team.aliens.dms.domain.student.usecase

import org.springframework.beans.factory.annotation.Qualifier
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.service.GetStudentService
import team.aliens.dms.domain.vote.dto.response.ModelStudentResponse
import java.time.LocalDate

@ReadOnlyUseCase
class GetModelStudentsUseCase(
    @Qualifier("getStudentServiceImpl") private val getStudentService: GetStudentService
) {
    fun execute(date: LocalDate): List<ModelStudentResponse> {
        return getStudentService.getModelStudentList(date)
    }

}
