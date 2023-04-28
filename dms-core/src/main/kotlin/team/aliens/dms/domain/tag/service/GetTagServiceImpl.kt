package team.aliens.dms.domain.tag.service

import java.util.UUID
import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.tag.exception.TagNotFoundException
import team.aliens.dms.domain.tag.model.Tag
import team.aliens.dms.domain.tag.spi.QueryTagPort

@Service
class GetTagServiceImpl(
    private val queryTagPort: QueryTagPort
) : GetTagService {

    override fun getTagById(tagId: UUID) =
        queryTagPort.queryTagById(tagId) ?: throw TagNotFoundException

    override fun getTagsBySchoolId(schoolId: UUID): List<Tag> {
        return queryTagPort.queryTagsBySchoolId(schoolId)
    }
}
