package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.dto.UpdateQuestionRequest
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.spi.CommandSchoolPort
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.user.service.UserService

@UseCase
class UpdateQuestionUseCase(
    private val querySchoolPort: QuerySchoolPort,
    private val commandSchoolPort: CommandSchoolPort,
    private val userService: UserService,
) {

    fun execute(request: UpdateQuestionRequest) {

        val user = userService.getCurrentUser()

        val school = querySchoolPort.querySchoolById(user.schoolId) ?: throw SchoolNotFoundException

        commandSchoolPort.saveSchool(
            school.copy(
                question = request.question,
                answer = request.answer
            )
        )
    }
}
