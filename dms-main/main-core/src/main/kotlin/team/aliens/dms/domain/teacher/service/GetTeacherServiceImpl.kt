package team.aliens.dms.domain.teacher.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.teacher.spi.QueryTeacherPort
import java.util.UUID

@Service
class GetTeacherServiceImpl(
    private val queryTeacherPort: QueryTeacherPort
) : GetTeacherService {

    override fun getTeacherRoleById(id: UUID) = queryTeacherPort.getTeacherRoleById(id)
}
