package team.aliens.dms.domain.teacher.service

import team.aliens.dms.domain.teacher.model.Role
import java.util.UUID

interface GetTeacherService {

    fun getTeacherRoleById(id: UUID): Role
}
