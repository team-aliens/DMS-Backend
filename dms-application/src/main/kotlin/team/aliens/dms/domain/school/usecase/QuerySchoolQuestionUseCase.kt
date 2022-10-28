package team.aliens.dms.domain.school.usecase

import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import java.util.*

@ReadOnlyUseCase
class QuerySchoolQuestionUseCase(
    private val querySchoolPort: QuerySchoolPort
) {

    fun execute(schoolId: UUID): String {
        val school = querySchoolPort.querySchoolById(schoolId) ?: throw SchoolNotFoundException

        return school.question
    }

}