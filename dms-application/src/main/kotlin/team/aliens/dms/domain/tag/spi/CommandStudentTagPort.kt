package team.aliens.dms.domain.tag.spi

import java.util.UUID

interface CommandStudentTagPort {
    fun deleteStudentTagByTagId(tagId: UUID)
}