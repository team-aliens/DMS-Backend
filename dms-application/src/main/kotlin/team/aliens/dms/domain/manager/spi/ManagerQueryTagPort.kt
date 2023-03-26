package team.aliens.dms.domain.manager.spi

import team.aliens.dms.domain.tag.model.Tag
import java.util.UUID

interface ManagerQueryTagPort {

    fun queryTagsByStudentId(studentId: UUID): List<Tag>
}