package team.aliens.dms.domain.tag.service

import team.aliens.dms.domain.tag.model.Tag
import java.util.UUID

interface GetTagService {

    fun getTagById(tagId: UUID): Tag

    fun getTagsBySchoolId(schoolId: UUID): List<Tag>
}
