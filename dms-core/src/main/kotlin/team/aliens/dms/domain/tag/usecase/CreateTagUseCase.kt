package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.tag.exception.TagAlreadyExistsException
import team.aliens.dms.domain.tag.model.Tag
import team.aliens.dms.domain.tag.spi.CommandTagPort
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.util.UUID

@UseCase
class CreateTagUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val commandTagPort: CommandTagPort,
    private val queryTagPort: QueryTagPort
) {

    fun execute(name: String, color: String): UUID {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        if (queryTagPort.existsByNameAndSchoolId(name, manager.schoolId)) {
            throw TagAlreadyExistsException
        }

        val tag = Tag(
            name = name,
            color = color,
            schoolId = manager.schoolId
        )

        val savedTag = commandTagPort.saveTag(tag)

        return savedTag.id
    }
}
