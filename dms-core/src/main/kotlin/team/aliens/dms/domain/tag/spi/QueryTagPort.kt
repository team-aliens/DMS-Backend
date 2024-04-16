package team.aliens.dms.domain.tag.spi

import team.aliens.dms.domain.tag.model.Tag
import java.util.UUID

interface QueryTagPort {

    fun queryAllWarningTags(names: List<String>): List<Tag>

    fun queryTagsBySchoolId(schoolId: UUID): List<Tag>

    fun queryTagById(tagId: UUID): Tag?

    fun queryTagByName(name: String): Tag?

    fun existsByNameAndSchoolId(name: String, schoolId: UUID): Boolean

    fun queryTagsByStudentId(studentId: UUID): List<Tag>
}
