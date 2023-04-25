package team.aliens.dms.domain.tag.spi

import team.aliens.dms.domain.tag.model.StudentTag
import java.util.UUID

interface CommandStudentTagPort {

    fun deleteStudentTagById(studentId: UUID, tagId: UUID)

    fun deleteStudentTagByTagId(tagId: UUID)

    fun saveAllStudentTags(studentTags: List<StudentTag>)
}
