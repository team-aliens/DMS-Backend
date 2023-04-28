package team.aliens.dms.domain.tag.service

import java.util.UUID
import team.aliens.dms.domain.tag.model.Tag

interface GetTagService {

    fun getTagById(tagId: UUID): Tag

    fun getTagsBySchoolId(schoolId: UUID): List<Tag>
}
