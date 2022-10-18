package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.domain.manager.exception.AnswerNotMatchedException
import team.aliens.dms.domain.manager.spi.ManagerQuerySchoolPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.global.annotation.UseCase
import team.aliens.dms.global.spi.CoveredEmailPort
import java.util.UUID

@UseCase
class FindAccountIdUseCase(
    val managerQuerySchoolPort: ManagerQuerySchoolPort,
    val managerQueryUserPort: ManagerQueryUserPort,
    val managerSecurityPort: ManagerSecurityPort,
    val coveredEmailPort: CoveredEmailPort
) {
    fun execute(schoolId: UUID, answer: String): String {
        val school =
            managerQuerySchoolPort.querySchoolById(schoolId) ?: throw SchoolNotFoundException

        if (school.answer != answer) {
            throw AnswerNotMatchedException
        }

        val user = managerQueryUserPort.queryUserById(managerSecurityPort.getCurrentUserId())
            ?: throw UserNotFoundException

        return coveredEmailPort.coveredEmail(user.email)
    }
}