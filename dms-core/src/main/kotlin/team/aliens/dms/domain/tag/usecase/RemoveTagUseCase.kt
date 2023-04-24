package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.tag.service.TagService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class RemoveTagUseCase(
    private val userService: UserService,
    private val tagService: TagService
) {

    fun execute(tagId: UUID) {

        val user = userService.getCurrentUser()

        val tag = tagService.getTagById(tagId, user.schoolId)

        tagService.deleteStudentTagAndTagById(tag.id)
    }
}
