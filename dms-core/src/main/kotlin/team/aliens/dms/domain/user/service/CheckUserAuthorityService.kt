package team.aliens.dms.domain.user.service

import team.aliens.dms.common.annotation.DomainService
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.user.spi.UserQueryStudentPort
import java.util.UUID

@DomainService
class CheckUserAuthorityService(
    private val queryStudentPort: UserQueryStudentPort
) : CheckUserAuthority {

    override fun execute(userId: UUID) = when (queryStudentPort.queryStudentByUserId(userId)) {
        is Student -> Authority.STUDENT
        else -> Authority.MANAGER
    }
}
