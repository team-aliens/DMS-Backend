package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.tag.exception.TagAlreadyExistsException
import team.aliens.dms.domain.tag.model.Tag
import team.aliens.dms.domain.tag.spi.CommandTagPort
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.user.service.GetUserService
import java.util.UUID

@UseCase
class CreateTagUseCase(
    private val getUserService: GetUserService,
    private val commandTagPort: CommandTagPort,
    private val queryTagPort: QueryTagPort
) {

    fun execute(name: String, color: String): UUID {

        val user = getUserService.getCurrentUser()

        if (queryTagPort.existsByNameAndSchoolId(name, user.schoolId)) {
            throw TagAlreadyExistsException
        }

        val tag = Tag(
            name = name,
            color = color,
            schoolId = user.schoolId
        )

        val savedTag = commandTagPort.saveTag(tag)

        return savedTag.id
    }
}
