package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.spi.SendEmailPort
import team.aliens.dms.domain.school.exception.AnswerMismatchException
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.user.service.GetUserService
import java.util.UUID

@ReadOnlyUseCase
class FindManagerAccountIdUseCase(
    private val querySchoolPort: QuerySchoolPort,
    private val getUserService: GetUserService,
    private val sendEmailPort: SendEmailPort
) {

    fun execute(schoolId: UUID, answer: String): String {
        val school = querySchoolPort.querySchoolById(schoolId) ?: throw SchoolNotFoundException

        if (school.answer != answer) {
            throw AnswerMismatchException
        }

        val user = getUserService.queryUserBySchoolIdAndAuthority(schoolId, Authority.MANAGER)

        sendEmailPort.sendAccountId(user.email, user.accountId)
        return StringUtil.coveredEmail(user.email)
    }
}
