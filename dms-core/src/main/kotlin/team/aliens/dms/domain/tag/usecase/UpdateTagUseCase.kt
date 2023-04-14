package team.aliens.dms.domain.tag.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.tag.exception.TagAlreadyExistsException
import team.aliens.dms.domain.tag.exception.TagNotFoundException
import team.aliens.dms.domain.tag.spi.CommandTagPort
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.user.service.GetUserService

@UseCase
class UpdateTagUseCase(
    private val getUserService: GetUserService,
    private val commandTagPort: CommandTagPort,
    private val queryTagPort: QueryTagPort
) {

    fun execute(tagId: UUID, newName: String, newColor: String) {

        val user = getUserService.getCurrentUser()
        val tag = queryTagPort.queryTagById(tagId) ?: throw TagNotFoundException
        validateSameSchool(tag.schoolId, user.schoolId)

        if (newName != tag.name && queryTagPort.existsByNameAndSchoolId(newName, user.schoolId)) {
            throw TagAlreadyExistsException
        }

        commandTagPort.saveTag(
            tag.copy(
                name = newName,
                color = newColor
            )
        )
    }
}
