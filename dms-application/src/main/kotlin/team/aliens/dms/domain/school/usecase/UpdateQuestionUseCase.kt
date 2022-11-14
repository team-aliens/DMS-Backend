package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.manager.spi.ManagerQuerySchoolPort
import team.aliens.dms.domain.school.dto.UpdateQuestionRequest
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.spi.CommandSchoolPort
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.school.spi.SchoolQueryUserPort
import team.aliens.dms.domain.school.spi.SchoolSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@UseCase
class UpdateQuestionUseCase(
    private val querySchoolPort: QuerySchoolPort,
    private val commandSchoolPort: CommandSchoolPort,
    private val securityPort: SchoolSecurityPort,
    private val queryUserPort: SchoolQueryUserPort
) {

    fun execute(request: UpdateQuestionRequest) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val school = querySchoolPort.querySchoolById(user.id) ?: throw SchoolNotFoundException

        commandSchoolPort.saveSchool(
            school.copy(
                question = request.question,
                answer = request.answer
            )
        )
    }
}