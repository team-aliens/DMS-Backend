package team.aliens.dms.domain.tag.spi

import java.util.UUID

interface QueryStudentTagPort {
    fun existsByTagIdAndStudentIds(tagId: UUID, studentIds: List<UUID>): Boolean
}