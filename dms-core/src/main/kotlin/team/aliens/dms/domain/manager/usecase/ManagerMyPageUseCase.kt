package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.manager.dto.ManagerDetailsResponse
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class ManagerMyPageUseCase(
    private val userService: UserService,
    private val schoolService: SchoolService
) {

    fun execute(): ManagerDetailsResponse {

        val user = userService.getCurrentUser()

        val school = schoolService.getSchoolById(user.schoolId)

        return ManagerDetailsResponse.of(school)
    }
}
