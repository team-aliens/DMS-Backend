package team.aliens.dms.domain.user.service

import java.util.UUID
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.user.model.User

interface GetUserService {

    fun queryUserById(userId: UUID): User

    fun queryUserByEmail(email: String): User

    fun queryUserByAccountId(accountId: String): User

    fun queryUserBySchoolIdAndAuthority(schoolId: UUID, authority: Authority): User

    fun checkUserNotExistsByEmail(email: String)

    fun checkUserNotExistsByAccountId(accountId: String)

    fun checkUserAuthority(userId: UUID, expectedAuthority: Authority)

    fun getUserAuthority(userId: UUID): Authority

    fun getCurrentUser(): User

    fun getCurrentStudent(): Student
}
