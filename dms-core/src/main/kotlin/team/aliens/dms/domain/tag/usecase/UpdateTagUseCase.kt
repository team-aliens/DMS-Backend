package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.tag.service.TagService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class UpdateTagUseCase(
    private val userService: UserService,
    private val tagService: TagService
) {

    fun execute(tagId: UUID, newName: String, newColor: String) {

        val user = userService.getCurrentUser()
        val tag = tagService.getTagById(tagId)
        validateSameSchool(tag.schoolId, user.schoolId)

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
