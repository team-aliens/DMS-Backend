package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.manager.dto.ManagerMyPageResponse
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class ManagerMyPageUseCase(
    private val userService: UserService,
    private val schoolService: SchoolService
) {

    fun execute(): ManagerMyPageResponse {

        val user = userService.getCurrentUser()

        val school = schoolService.getSchoolById(user.schoolId)

        return ManagerMyPageResponse(
            schoolId = school.id,
            schoolName = school.name,
            code = school.code,
            question = school.question,
            answer = school.answer
        )
    }
}
