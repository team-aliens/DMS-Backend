package team.aliens.dms.domain.user.service

import team.aliens.dms.common.annotation.DomainService
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.student.model.Student
import java.util.UUID
import team.aliens.dms.domain.student.spi.QueryStudentPort

@DomainService
class CheckUserAuthorityService(
    private val queryStudentPort: QueryStudentPort
) : CheckUserAuthority {

    override fun execute(userId: UUID) = when (queryStudentPort.queryStudentByUserId(userId)) {
        is Student -> Authority.STUDENT
        else -> Authority.MANAGER
    }
}
