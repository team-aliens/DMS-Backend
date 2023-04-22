package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.dto.UpdateQuestionRequest
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class UpdateQuestionUseCase(
    private val schoolService: SchoolService,
    private val userService: UserService,
) {

    fun execute(request: UpdateQuestionRequest) {

        val user = userService.getCurrentUser()

        val school = schoolService.getSchoolById(user.schoolId)

        schoolService.saveSchool(
            school.copy(
                question = request.question,
                answer = request.answer
            )
        )
    }
}
