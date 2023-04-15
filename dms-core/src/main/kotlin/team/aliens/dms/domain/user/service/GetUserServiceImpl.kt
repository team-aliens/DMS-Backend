package team.aliens.dms.domain.user.service

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
import java.util.UUID

@DomainService
class GetUserServiceImpl(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val queryStudentPort: QueryStudentPort
) : GetUserService {

    override fun queryUserById(userId: UUID) =
        queryUserPort.queryUserById(userId) ?: throw UserNotFoundException

    override fun queryUserByEmail(email: String) =
        queryUserPort.queryUserByEmail(email) ?: throw UserNotFoundException

    override fun queryUserByAccountId(accountId: String) =
        queryUserPort.queryUserByAccountId(accountId) ?: throw UserNotFoundException

    override fun queryUserBySchoolIdAndAuthority(schoolId: UUID, authority: Authority) =
        queryUserPort.queryUserBySchoolIdAndAuthority(schoolId, authority) ?: throw UserNotFoundException

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

    override fun checkUserAuthority(authority: Authority, expectedAuthority: Authority) {
        if (authority != expectedAuthority) {
            throw InvalidRoleException
        }
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
