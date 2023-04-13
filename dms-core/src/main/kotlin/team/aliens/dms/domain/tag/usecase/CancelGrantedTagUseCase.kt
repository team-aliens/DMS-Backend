package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.tag.exception.TagNotFoundException
import team.aliens.dms.domain.tag.spi.CommandTagPort
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.util.UUID

@UseCase
class CancelGrantedTagUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val queryTagPort: QueryTagPort,
    private val queryStudentPort: QueryStudentPort,
    private val commandTagPort: CommandTagPort
) {

    fun execute(studentId: UUID, tagId: UUID) {
        val managerId = securityPort.getCurrentUserId()
        val manager = queryUserPort.queryUserById(managerId) ?: throw UserNotFoundException
        val tag = queryTagPort.queryTagById(tagId) ?: throw TagNotFoundException

        validateSameSchool(manager.schoolId, tag.schoolId)

        val student = queryStudentPort.queryStudentById(studentId) ?: throw StudentNotFoundException

        commandTagPort.deleteStudentTagById(
            studentId = student.id,
            tagId = tag.id
        )
    }
}
