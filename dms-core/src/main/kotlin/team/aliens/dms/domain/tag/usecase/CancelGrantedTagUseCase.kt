package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.tag.exception.TagNotFoundException
import team.aliens.dms.domain.tag.spi.CommandTagPort
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class CancelGrantedTagUseCase(
    private val userService: UserService,
    private val studentService: StudentService,
    private val queryTagPort: QueryTagPort,
    private val commandTagPort: CommandTagPort
) {

    fun execute(studentId: UUID, tagId: UUID) {

        val user = userService.getCurrentUser()
        val tag = queryTagPort.queryTagById(tagId) ?: throw TagNotFoundException

        validateSameSchool(user.schoolId, tag.schoolId)

        val student = studentService.getStudentById(studentId)

        commandTagPort.deleteStudentTagById(
            studentId = student.id,
            tagId = tag.id
        )
    }
}
