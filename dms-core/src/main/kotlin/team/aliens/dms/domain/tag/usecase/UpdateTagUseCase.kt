package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.tag.exception.TagAlreadyExistsException
import team.aliens.dms.domain.tag.exception.TagNotFoundException
import team.aliens.dms.domain.tag.spi.CommandTagPort
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.util.UUID

@UseCase
class UpdateTagUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val commandTagPort: CommandTagPort,
    private val queryTagPort: QueryTagPort
) {

    fun execute(tagId: UUID, newName: String, newColor: String) {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val tag = queryTagPort.queryTagById(tagId) ?: throw TagNotFoundException
        validateSameSchool(tag.schoolId, manager.schoolId)

        if (newName != tag.name && queryTagPort.existsByNameAndSchoolId(newName, manager.schoolId)) {
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
