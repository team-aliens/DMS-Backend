package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.spi.SendEmailPort
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerQuerySchoolPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.school.exception.AnswerMismatchException
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import java.util.UUID

@ReadOnlyUseCase
class FindManagerAccountIdUseCase(
    private val querySchoolPort: ManagerQuerySchoolPort,
    private val queryUserPort: ManagerQueryUserPort,
    private val sendEmailPort: SendEmailPort
) {

    fun execute(schoolId: UUID, answer: String): String {
        val school = querySchoolPort.querySchoolById(schoolId) ?: throw SchoolNotFoundException

        if (school.answer != answer) {
            throw AnswerMismatchException
        }

        val manager = queryUserPort.queryUserBySchoolIdAndAuthority(schoolId, Authority.MANAGER)
            ?: throw ManagerNotFoundException

        sendEmailPort.sendAccountId(manager.email, manager.accountId)

        return StringUtil.coveredEmail(manager.email)
    }
}