package team.aliens.dms.domain.tag.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.tag.exception.TagNotFoundException
import team.aliens.dms.domain.tag.model.Tag
import team.aliens.dms.domain.tag.spi.QueryTagPort
import java.util.UUID

@Service
class GetTagServiceImpl(
    private val queryTagPort: QueryTagPort
) : GetTagService {

    override fun getTagById(tagId: UUID): Tag {
        return queryTagPort.queryTagById(tagId)
            ?: throw TagNotFoundException
    }

    override fun getTagsBySchoolId(schoolId: UUID): List<Tag> {
        return queryTagPort.queryTagsBySchoolId(schoolId)
    }
}
