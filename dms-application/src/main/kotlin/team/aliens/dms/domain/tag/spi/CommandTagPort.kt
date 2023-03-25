package team.aliens.dms.domain.tag.spi

import team.aliens.dms.domain.tag.model.StudentTag
import team.aliens.dms.domain.tag.model.Tag
import java.util.UUID

interface CommandTagPort {

    fun saveAllStudentTags(studentTags: List<StudentTag>)
    
    fun saveTag(tag: Tag): Tag

    fun deleteStudentTagById(studentId: UUID, tagId: UUID)
}

    fun deleteTagById(tagId: UUID)