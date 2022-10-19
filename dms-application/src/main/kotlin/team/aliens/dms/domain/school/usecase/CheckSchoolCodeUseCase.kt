package team.aliens.dms.domain.school.usecase

import team.aliens.dms.domain.school.exception.CodeNotMatchedException
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.global.annotation.ReadOnlyUseCase
import java.util.UUID

@ReadOnlyUseCase
class CheckSchoolCodeUseCase(
    private val querySchoolPort: QuerySchoolPort
) {

    fun execute(schoolId: UUID, schoolCode: String): UUID {
        val school = querySchoolPort.querySchoolById(schoolId) ?: throw SchoolNotFoundException

        if (schoolCode != school.code) {
            throw CodeNotMatchedException
        }

        return schoolId
    }
}