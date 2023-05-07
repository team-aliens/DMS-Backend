package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.spi.SendEmailPort
import team.aliens.dms.domain.manager.dto.ManagerEmailResponse
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@ReadOnlyUseCase
class FindManagerAccountIdUseCase(
    private val schoolService: SchoolService,
    private val userService: UserService,
    private val sendEmailPort: SendEmailPort
) {

    fun execute(schoolId: UUID, answer: String): ManagerEmailResponse {
        schoolService.getSchoolById(schoolId)
            .apply { checkAnswer(answer) }

        val user = userService.queryUserBySchoolIdAndAuthority(schoolId, Authority.MANAGER)

        sendEmailPort.sendAccountId(user.email, user.accountId)

        return ManagerEmailResponse(StringUtil.coveredEmail(user.email))
    }
}
