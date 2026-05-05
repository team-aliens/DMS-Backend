package team.aliens.dms.domain.teacher.service

import team.aliens.dms.domain.teacher.model.Teacher
import team.aliens.dms.domain.teacher.spi.vo.TeacherVO
import java.util.UUID

interface GetTeacherService {
    fun getTeacherById(id: UUID): Teacher

    fun getGeneralTeachersBySchoolId(schoolId: UUID): List<TeacherVO>
}
