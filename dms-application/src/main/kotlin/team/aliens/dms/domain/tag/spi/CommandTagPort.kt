package team.aliens.dms.domain.tag.spi

import java.util.UUID

interface CommandTagPort {

    fun deleteStudentTagById(studentId: UUID, tagId: UUID)
}
