package team.aliens.dms.domain.tag.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.tag.service.TagService

@UseCase
class RemoveTagUseCase(
    private val tagService: TagService
) {

    fun execute(tagId: UUID) {
        val tag = tagService.getTagById(tagId)
        tagService.deleteStudentTagAndTagById(tag.id)
    }
}
