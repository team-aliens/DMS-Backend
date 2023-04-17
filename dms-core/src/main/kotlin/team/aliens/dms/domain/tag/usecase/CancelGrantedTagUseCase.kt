package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.tag.exception.TagNotFoundException
import team.aliens.dms.domain.tag.spi.CommandTagPort
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class CancelGrantedTagUseCase(
    private val userService: UserService,
    private val queryTagPort: QueryTagPort,
    private val queryStudentPort: QueryStudentPort,
    private val commandTagPort: CommandTagPort
) {

    fun execute(studentId: UUID, tagId: UUID) {

        val user = userService.getCurrentUser()
        val tag = queryTagPort.queryTagById(tagId) ?: throw TagNotFoundException

        validateSameSchool(user.schoolId, tag.schoolId)

        val student = queryStudentPort.queryStudentById(studentId) ?: throw StudentNotFoundException

        commandTagPort.deleteStudentTagById(
            studentId = student.id,
            tagId = tag.id
        )
    }
}
