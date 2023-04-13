package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.tag.dto.QueryTagsResponse
import team.aliens.dms.domain.tag.dto.TagResponse
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort

@ReadOnlyUseCase
class QueryTagsUseCase(
    private val queryTagPort: QueryTagPort,
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort
) {

    fun execute(): QueryTagsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val tags = queryTagPort.queryTagsBySchoolId(manager.schoolId)
            .map {
                TagResponse(
                    id = it.id,
                    name = it.name,
                    color = it.color
                )
            }

        return QueryTagsResponse(tags)
    }
}
