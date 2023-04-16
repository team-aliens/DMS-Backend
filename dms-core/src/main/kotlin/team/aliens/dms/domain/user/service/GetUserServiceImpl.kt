package team.aliens.dms.domain.user.service

import java.util.UUID
import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.user.exception.UserAccountIdExistsException
import team.aliens.dms.domain.user.exception.UserEmailExistsException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.spi.QueryUserPort

@Service
class GetUserServiceImpl(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val queryStudentPort: QueryStudentPort
) : GetUserService {

    override fun queryUserById(userId: UUID) =
        queryUserPort.queryUserById(userId) ?: throw UserNotFoundException

    override fun queryUserByAccountId(accountId: String) =
        queryUserPort.queryUserByAccountId(accountId) ?: throw UserNotFoundException

    override fun queryUserBySchoolIdAndAuthority(schoolId: UUID, authority: Authority) =
        queryUserPort.queryUserBySchoolIdAndAuthority(schoolId, authority) ?: throw UserNotFoundException

    override fun checkUserNotExistsByEmail(email: String) {
        if (queryUserPort.existsUserByEmail(email)) {
            throw UserEmailExistsException
        }
    }

    override fun checkExistsByEmail(email: String) {
        if (!queryUserPort.existsUserByEmail(email)) {
            throw UserNotFoundException
        }
    }

    override fun checkUserNotExistsByAccountId(accountId: String) {
        if (queryUserPort.existsUserByAccountId(accountId)) {
            throw UserAccountIdExistsException
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
