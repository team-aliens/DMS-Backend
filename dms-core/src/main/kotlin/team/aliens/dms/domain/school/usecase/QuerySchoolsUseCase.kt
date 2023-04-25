package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.dto.SchoolsResponse
import team.aliens.dms.domain.school.service.SchoolService

@ReadOnlyUseCase
class QuerySchoolsUseCase(
    private val schoolService: SchoolService
) {

    fun execute(): SchoolsResponse {
        val schools = schoolService.getAllSchools()

        val result = schools.map {
            SchoolsResponse.SchoolElement(
                id = it.id,
                name = it.name,
                address = it.address,
            )
        }

        return SchoolsResponse(result)
    }
}
