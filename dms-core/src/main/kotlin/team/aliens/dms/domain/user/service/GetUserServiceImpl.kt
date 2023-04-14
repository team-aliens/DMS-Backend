package team.aliens.dms.domain.user.service

import java.util.UUID
import team.aliens.dms.common.annotation.DomainService
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.user.exception.InvalidRoleException
import team.aliens.dms.domain.user.exception.UserAccountIdExistsException
import team.aliens.dms.domain.user.exception.UserEmailExistsException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.spi.QueryUserPort

@DomainService
class GetUserServiceImpl(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val queryStudentPort: QueryStudentPort
) : GetUserService {

    override fun queryUserById(userId: UUID) =
        queryUserPort.queryUserById(userId) ?: throw UserNotFoundException

    override fun queryUserByEmail(email: String): User {
        return queryUserPort.queryUserByEmail(email) ?: throw UserNotFoundException
    }

    override fun queryUserByAccountId(accountId: String): User {
        return queryUserPort.queryUserByAccountId(accountId) ?: throw UserNotFoundException
    }

    override fun queryUserBySchoolIdAndAuthority(schoolId: UUID, authority: Authority): User {
        return queryUserPort.queryUserBySchoolIdAndAuthority(schoolId, authority) ?: throw UserNotFoundException
    }

    override fun checkUserNotExistsByEmail(email: String) {
        if (queryUserPort.existsUserByEmail(email)) {
            throw UserEmailExistsException
        }
    }

    override fun checkUserNotExistsByAccountId(accountId: String) {
        if (queryUserPort.existsUserByAccountId(accountId)) {
            throw UserAccountIdExistsException
        }
    }

    override fun checkUserAuthority(userId: UUID, expectedAuthority: Authority) {
        if (getUserAuthority(userId) != expectedAuthority) {
            throw InvalidRoleException
        }
    }

    override fun getUserAuthority(userId: UUID) = when (queryStudentPort.queryStudentById(userId)) {
        is Student -> Authority.STUDENT
        else -> Authority.MANAGER
    }

    override fun getCurrentUser(): User {
        val currentUserId = securityPort.getCurrentUserId()
        return queryUserById(currentUserId)
    }

    override fun getCurrentStudent(): Student {
        val currentUserId = securityPort.getCurrentUserId()
        return queryStudentPort.queryStudentById(currentUserId) ?: throw UserNotFoundException
    }
}
