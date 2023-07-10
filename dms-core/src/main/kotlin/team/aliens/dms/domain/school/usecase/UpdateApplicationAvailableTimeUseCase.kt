package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.dto.UpdateApplicationAvailableTimeRequest
import team.aliens.dms.domain.school.model.ApplicationAvailableTime
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class UpdateApplicationAvailableTimeUseCase(
    private val userService: UserService,
    private val schoolService: SchoolService
) {

    fun execute(request: UpdateApplicationAvailableTimeRequest) {
        val user = userService.getCurrentUser()

        schoolService.saveApplicationAvailableTime(
            ApplicationAvailableTime(
                 schoolId = user.schoolId,
                 startDayOfWeek = request.startDayOfWeek,
                 startTime = request.startTime,
                 endDayOfWeek = request.endDayOfWeek,
                 endTime = request.endTime,
                 type = request.type
            )
        )
    }
}