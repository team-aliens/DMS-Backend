package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.tag.exception.TagNotFoundException
import team.aliens.dms.domain.tag.spi.CommandStudentTagPort
import team.aliens.dms.domain.tag.spi.CommandTagPort
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class RemoveTagUseCase(
    private val userService: UserService,
    private val queryTagPort: QueryTagPort,
    private val commandTagPort: CommandTagPort,
    private val commandStudentTagPort: CommandStudentTagPort
) {

    fun execute(tagId: UUID) {

        val user = userService.getCurrentUser()

        val tag = queryTagPort.queryTagById(tagId) ?: throw TagNotFoundException
        validateSameSchool(user.schoolId, tag.schoolId)

        commandStudentTagPort.deleteStudentTagByTagId(tag.id)
        commandTagPort.deleteTagById(tag.id)
    }
}
