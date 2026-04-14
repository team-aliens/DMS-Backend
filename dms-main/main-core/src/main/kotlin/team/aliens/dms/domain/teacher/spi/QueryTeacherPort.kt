package team.aliens.dms.domain.teacher.spi

import team.aliens.dms.domain.teacher.model.Role
import java.util.UUID

interface QueryTeacherPort {

    fun getTeacherRoleById(id: UUID): Role
}
