package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.service.SchoolService
import java.util.UUID

@ReadOnlyUseCase
class CheckSchoolAnswerUseCase(
    private val schoolService: SchoolService
) {

    fun execute(schoolId: UUID, answer: String) {
        schoolService.getSchoolById(schoolId)
            .apply { checkAnswer(answer) }
    }
}
