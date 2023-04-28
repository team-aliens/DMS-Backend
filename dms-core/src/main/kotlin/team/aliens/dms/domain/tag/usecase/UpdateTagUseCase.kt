package team.aliens.dms.domain.tag.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.tag.service.TagService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class UpdateTagUseCase(
    private val userService: UserService,
    private val tagService: TagService
) {

    fun execute(tagId: UUID, newName: String, newColor: String) {

        val user = userService.getCurrentUser()
        val tag = tagService.getTagById(tagId)

        if (newName != tag.name) {
            tagService.checkTagExistsByNameAndSchoolId(newName, user.schoolId)
        }

        tagService.saveTag(
            tag.copy(
                name = newName,
                color = newColor
            )
        )
    }
}
