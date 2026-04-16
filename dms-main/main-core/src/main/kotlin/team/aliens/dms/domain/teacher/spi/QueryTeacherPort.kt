package team.aliens.dms.domain.teacher.spi

import team.aliens.dms.domain.teacher.model.Teacher
import java.util.UUID

interface QueryTeacherPort {
    fun getTeacherById(id: UUID): Teacher?
}
