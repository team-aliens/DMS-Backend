package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.manager.dto.ManagerMyPageResponse
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class ManagerMyPageUseCase(
    private val userService: UserService,
    private val querySchoolPort: QuerySchoolPort
) {

    fun execute(): ManagerMyPageResponse {

        val user = userService.getCurrentUser()

        val school = querySchoolPort.querySchoolById(user.schoolId) ?: throw SchoolNotFoundException

        return ManagerMyPageResponse(
            schoolId = school.id,
            schoolName = school.name,
            code = school.code,
            question = school.question,
            answer = school.answer
        )
    }
}
