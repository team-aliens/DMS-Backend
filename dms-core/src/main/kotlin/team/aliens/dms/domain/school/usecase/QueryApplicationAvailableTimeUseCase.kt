package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.dto.ApplicationAvailableTimeResponse
import team.aliens.dms.domain.school.model.ApplicationAvailableTimeType
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryApplicationAvailableTimeUseCase(
    private val userService: UserService,
    private val schoolService: SchoolService
) {

    fun execute(type: ApplicationAvailableTimeType): ApplicationAvailableTimeResponse {
        val user = userService.getCurrentUser()

        val applicationAvailableTime = schoolService.getApplicationAvailableTimeBySchoolIdAndType(user.schoolId, type)

        return ApplicationAvailableTimeResponse(
                startDayOfWeek = applicationAvailableTime.startDayOfWeek,
                startTime = applicationAvailableTime.startTime,
                endDayOfWeek = applicationAvailableTime.endDayOfWeek,
                endTime = applicationAvailableTime.endTime
        )
    }
}