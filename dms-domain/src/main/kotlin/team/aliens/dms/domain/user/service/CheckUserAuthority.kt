package team.aliens.dms.domain.user.service

import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.user.spi.UserQueryStudentPort
import team.aliens.dms.common.annotation.DomainService
import java.util.UUID

@DomainService
class CheckUserAuthority(
    private val queryStudentPort: UserQueryStudentPort
) {

    fun execute(userId: UUID) = when (queryStudentPort.queryStudentById(userId)) {
        is Student -> Authority.STUDENT
        else -> Authority.MANAGER
    }
}