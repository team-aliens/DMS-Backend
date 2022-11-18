package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.manager.spi.ManagerQuerySchoolPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.school.exception.AnswerMismatchException
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.user.exception.InvalidRoleException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.service.CheckUserAuthority
import java.util.UUID

@ReadOnlyUseCase
class FindManagerAccountIdUseCase(
    private val querySchoolPort: ManagerQuerySchoolPort,
    private val queryUserPort: ManagerQueryUserPort,
    private val checkUserAuthority: CheckUserAuthority
) {

    fun execute(schoolId: UUID, answer: String): String {
        val school = querySchoolPort.querySchoolById(schoolId) ?: throw SchoolNotFoundException

        if (school.answer != answer) {
            throw AnswerMismatchException
        }

        val user = queryUserPort.queryUserBySchoolId(schoolId) ?: throw UserNotFoundException

        if (checkUserAuthority.execute(user.id) != Authority.MANAGER) {
            throw InvalidRoleException
        }

        return StringUtil.coveredEmail(user.email)
    }
}