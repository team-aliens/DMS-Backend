package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.service.SchoolService
import java.util.UUID

@ReadOnlyUseCase
class CheckSchoolCodeUseCase(
    private val schoolService: SchoolService
) {

    fun execute(schoolCode: String): UUID {
        val school = schoolService.getSchoolByCode(schoolCode)

        return school.id
    }
}
