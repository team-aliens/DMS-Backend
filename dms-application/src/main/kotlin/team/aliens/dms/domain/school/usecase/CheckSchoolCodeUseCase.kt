package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.exception.SchoolCodeMismatchException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import java.util.UUID

@ReadOnlyUseCase
class CheckSchoolCodeUseCase(
    private val querySchoolPort: QuerySchoolPort
) {

    fun execute(schoolCode: String): UUID {
        val school = querySchoolPort.querySchoolByCode(schoolCode) ?: throw SchoolCodeMismatchException

        return school.id
    }
}
