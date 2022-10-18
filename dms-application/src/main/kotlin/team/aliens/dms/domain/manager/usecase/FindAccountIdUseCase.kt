package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerQuerySchoolPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.school.exception.AnswerNotMatchedException
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.global.annotation.ReadOnlyUseCase
import team.aliens.dms.global.spi.CoveredEmailPort
import java.util.*

@ReadOnlyUseCase
class FindAccountIdUseCase(
    private val managerQuerySchoolPort: ManagerQuerySchoolPort,
    private val managerQueryUserPort: ManagerQueryUserPort,
    private val coveredEmailPort: CoveredEmailPort
) {

    fun execute(schoolId: UUID, answer: String): String {
        val school = managerQuerySchoolPort.querySchoolById(schoolId) ?: throw SchoolNotFoundException

        if (school.answer != answer) {
            throw AnswerNotMatchedException
        }

        val user = managerQueryUserPort.queryUserBySchoolId(schoolId) ?: throw ManagerNotFoundException

        return coveredEmailPort.coveredEmail(user.email)
    }

}