package team.aliens.dms.domain.tag.service

import team.aliens.dms.domain.tag.model.StudentTag
import team.aliens.dms.domain.tag.model.Tag
import java.util.UUID

interface GetTagService {

    fun getStudentTagsByStudentId(studentId: UUID): List<StudentTag>

    fun getTagByName(name: String): Tag

    fun getTagById(tagId: UUID): Tag

    fun getTagsBySchoolId(schoolId: UUID): List<Tag>
}
