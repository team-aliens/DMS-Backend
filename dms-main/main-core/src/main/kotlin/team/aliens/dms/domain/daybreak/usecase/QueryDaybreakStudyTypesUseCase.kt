package team.aliens.dms.domain.daybreak.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.daybreak.dto.response.DaybreakStudyTypesResponse
import team.aliens.dms.domain.daybreak.dto.response.DaybreakStudyTypesResponse.DaybreakStudyTypeResponse
import team.aliens.dms.domain.daybreak.service.DaybreakService

@UseCase
class QueryDaybreakStudyTypesUseCase(
    private val daybreakService: DaybreakService,
    private val securityService: SecurityService
) {

    fun execute(): DaybreakStudyTypesResponse {

        val schoolId = securityService.getCurrentSchoolId()

        val typeList = daybreakService.getDaybreakStudyTypesBySchoolId(schoolId)

        return DaybreakStudyTypesResponse(
            types = typeList.map { type ->
                DaybreakStudyTypeResponse(
                    id = type.id,
                    name = type.name
                )
            }
        )
    }
}
