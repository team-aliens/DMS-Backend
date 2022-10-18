package team.aliens.dms.domain.school.usecase

import team.aliens.dms.domain.school.dto.SchoolsResponse
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.global.annotation.ReadOnlyUseCase

@ReadOnlyUseCase
class QuerySchoolsUseCase(
    private val querySchoolPort: QuerySchoolPort
) {

    fun execute(): SchoolsResponse {
        val schools = querySchoolPort.queryAllSchool()

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