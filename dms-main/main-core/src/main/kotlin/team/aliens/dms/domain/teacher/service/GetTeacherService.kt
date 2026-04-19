package team.aliens.dms.domain.teacher.service

import team.aliens.dms.domain.teacher.model.Teacher
import java.util.UUID

interface GetTeacherService {
    fun getTeacherById(id: UUID): Teacher
}
