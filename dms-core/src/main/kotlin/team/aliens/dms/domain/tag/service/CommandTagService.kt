package team.aliens.dms.domain.tag.service

import team.aliens.dms.domain.tag.model.StudentTag
import team.aliens.dms.domain.tag.model.Tag
import java.util.UUID

interface CommandTagService {

    fun deleteAllStudentTagsByStudentIdIn(studentIds: List<UUID>)

    fun deleteStudentTagById(studentId: UUID, tagId: UUID)

    fun deleteStudentTagAndTagById(tagId: UUID)

    fun saveTag(tag: Tag): Tag

    fun saveStudentTag(studentTag: StudentTag): StudentTag

    fun saveAllStudentTags(studentTags: List<StudentTag>)
}
