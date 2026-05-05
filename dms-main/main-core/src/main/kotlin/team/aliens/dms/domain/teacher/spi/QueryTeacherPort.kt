package team.aliens.dms.domain.teacher.spi

import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.teacher.model.Teacher
import team.aliens.dms.domain.teacher.spi.vo.TeacherVO
import java.util.UUID

interface QueryTeacherPort {
    fun getTeacherById(id: UUID): Teacher?

    fun getAllTeachersBySchoolIdAndAuthority(schoolId: UUID, authority: Authority): List<TeacherVO>
}
