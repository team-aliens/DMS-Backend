package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.tag.exception.TagNotFoundException
import team.aliens.dms.domain.tag.spi.CommandTagPort
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.tag.spi.TagQueryUserPort
import team.aliens.dms.domain.tag.spi.TagSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.util.UUID
import team.aliens.dms.domain.tag.spi.CommandStudentTagPort

@UseCase
class RemoveTagUseCase(
    private val securityPort: TagSecurityPort,
    private val queryUserPort: TagQueryUserPort,
    private val queryTagPort: QueryTagPort,
    private val commandTagPort: CommandTagPort,
    private val commandStudentTagPort: CommandStudentTagPort
) {

    fun execute(tagId: UUID) {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val tag = queryTagPort.queryTagById(tagId) ?: throw TagNotFoundException
        validateSameSchool(manager.schoolId, tag.schoolId)

        commandStudentTagPort.deleteStudentTagByTagId(tag.id)
        commandTagPort.deleteTagById(tag.id)
    }
}
