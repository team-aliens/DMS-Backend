package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.dto.SchoolQuestionResponse
import team.aliens.dms.domain.school.service.SchoolService
import java.util.UUID

@ReadOnlyUseCase
class QuerySchoolQuestionUseCase(
    private val schoolService: SchoolService
) {

    fun execute(schoolId: UUID): SchoolQuestionResponse {
        val school = schoolService.getSchoolById(schoolId)

        return SchoolQuestionResponse(question = school.question)
    }
}
