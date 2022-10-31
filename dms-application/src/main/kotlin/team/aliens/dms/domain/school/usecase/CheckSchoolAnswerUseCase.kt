package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.exception.AnswerMismatchException
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import java.util.UUID

@ReadOnlyUseCase
class CheckSchoolAnswerUseCase(
    private val querySchoolPort: QuerySchoolPort
) {

    fun execute(schoolId: UUID, answer: String) {
        val school = querySchoolPort.querySchoolById(schoolId) ?: throw SchoolNotFoundException

        if (school.answer != answer) {
            throw AnswerMismatchException
        }
    }
}