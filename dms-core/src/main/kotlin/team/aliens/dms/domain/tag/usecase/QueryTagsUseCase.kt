package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.tag.dto.QueryTagsResponse
import team.aliens.dms.domain.tag.dto.TagResponse
import team.aliens.dms.domain.tag.service.TagService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryTagsUseCase(
    private val tagService: TagService,
    private val userService: UserService
) {

    fun execute(): QueryTagsResponse {

        val user = userService.getCurrentUser()
        val tags = tagService.getTagsBySchoolId(user.schoolId)
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
