package team.aliens.dms.domain.tag.service

import team.aliens.dms.domain.tag.model.Tag
import team.aliens.dms.domain.tag.spi.vo.StudentTagDetailVO
import java.util.UUID

interface GetTagService {

    fun getAllStudentTagDetails(): List<StudentTagDetailVO>

    fun getTagsByTagNameIn(names: List<String>): List<Tag>

    fun getTagById(tagId: UUID): Tag

    fun getTagsBySchoolId(schoolId: UUID): List<Tag>
}
