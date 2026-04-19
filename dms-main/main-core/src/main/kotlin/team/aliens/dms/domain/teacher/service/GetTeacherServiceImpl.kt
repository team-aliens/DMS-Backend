package team.aliens.dms.domain.teacher.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.teacher.exception.TeacherNotFoundException
import team.aliens.dms.domain.teacher.spi.TeacherPort
import java.util.UUID

@Service
class GetTeacherServiceImpl(
    private val teacherPort: TeacherPort
) : GetTeacherService {

    override fun getTeacherById(id: UUID) =
        teacherPort.getTeacherById(id) ?: throw TeacherNotFoundException
}
