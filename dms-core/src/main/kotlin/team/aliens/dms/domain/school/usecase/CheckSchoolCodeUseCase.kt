package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.dto.SchoolIdResponse
import team.aliens.dms.domain.school.service.SchoolService

@ReadOnlyUseCase
class CheckSchoolCodeUseCase(
    private val schoolService: SchoolService
) {

    fun execute(schoolCode: String): SchoolIdResponse {
        val school = schoolService.getSchoolByCode(schoolCode)

        return SchoolIdResponse(schoolId = school.id)
    }
}
