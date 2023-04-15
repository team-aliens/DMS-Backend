package team.aliens.dms.domain.manager.usecase

import java.time.LocalDateTime
import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.spi.CommandRemainStatusPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.user.service.CommandUserService
import team.aliens.dms.domain.user.service.GetUserService

@UseCase
class RemoveStudentUseCase(
    private val getUserService: GetUserService,
    private val commandUserService: CommandUserService,
    private val queryStudentPort: QueryStudentPort,
    private val commandRemainStatusPort: CommandRemainStatusPort,
    private val commandStudyRoomPort: CommandStudyRoomPort,
    private val commandStudentPort: CommandStudentPort
) {

    fun execute(studentId: UUID) {

        val user = getUserService.getCurrentUser()
        val student = queryStudentPort.queryStudentById(studentId) ?: throw StudentNotFoundException
        validateSameSchool(student.schoolId, user.schoolId)

        commandRemainStatusPort.deleteByStudentId(studentId)
        commandStudyRoomPort.deleteSeatApplicationByStudentId(studentId)

        commandStudentPort.saveStudent(
            student.copy(deletedAt = LocalDateTime.now())
        )

        student.userId?.let {
            commandUserService.deleteUserById(it)
        }
    }
}
