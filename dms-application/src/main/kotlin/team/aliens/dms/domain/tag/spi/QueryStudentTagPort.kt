package team.aliens.dms.domain.tag.spi

import java.util.UUID

interface QueryStudentTagPort {
    fun existsByTagIdAndStudentIdList(tagId: UUID, studentIdList: List<UUID>): Boolean
}