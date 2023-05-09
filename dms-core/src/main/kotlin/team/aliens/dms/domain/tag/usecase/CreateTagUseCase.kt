package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.tag.dto.TagIdResponse
import team.aliens.dms.domain.tag.model.Tag
import team.aliens.dms.domain.tag.service.TagService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class CreateTagUseCase(
    private val userService: UserService,
    private val tagService: TagService
) {

    fun execute(name: String, color: String): TagIdResponse {

        val user = userService.getCurrentUser()

        tagService.checkTagExistsByNameAndSchoolId(name, user.schoolId)

        val tag = Tag(
            name = name,
            color = color,
            schoolId = user.schoolId
        )

        val savedTag = tagService.saveTag(tag)

        return TagIdResponse(savedTag.id)
    }
}
