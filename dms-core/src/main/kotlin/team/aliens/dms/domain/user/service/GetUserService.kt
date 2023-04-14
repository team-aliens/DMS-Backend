package team.aliens.dms.domain.user.service

import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.user.model.User
import java.util.UUID

interface GetUserService {
    fun queryUserById(userId: UUID): User
    fun getUserAuthority(userId: UUID): Authority
    fun getCurrentUser(): User
    fun getCurrentStudent(): Student
}
