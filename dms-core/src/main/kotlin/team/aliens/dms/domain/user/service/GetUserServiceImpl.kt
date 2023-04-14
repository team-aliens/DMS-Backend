package team.aliens.dms.domain.user.service

import java.util.UUID
import team.aliens.dms.common.annotation.DomainService
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.spi.QueryUserPort

@DomainService
class GetUserServiceImpl(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val queryStudentPort: QueryStudentPort
) : GetUserService {

    override fun getUserAuthority(userId: UUID) = when (queryStudentPort.queryStudentById(userId)) {
        is Student -> Authority.STUDENT
        else -> Authority.MANAGER
    }

    override fun getCurrentUser(): User {
        val currentUserId = securityPort.getCurrentUserId()
        return queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException
    }

    override fun getCurrentStudent(): Student {
        val currentUserId = securityPort.getCurrentUserId()
        return queryStudentPort.queryStudentById(currentUserId) ?: throw UserNotFoundException
    }
}
