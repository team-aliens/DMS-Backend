package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.service.SchoolService
import java.util.UUID

@ReadOnlyUseCase
class QuerySchoolQuestionUseCase(
    private val schoolService: SchoolService
) {

    fun execute(schoolId: UUID): String {
        val school = schoolService.getSchoolById(schoolId)

        return school.question
    }
}
