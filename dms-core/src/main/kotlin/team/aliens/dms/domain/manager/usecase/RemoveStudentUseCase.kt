package team.aliens.dms.domain.manager.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.spi.CommandRemainStatusPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.user.service.UserService

@UseCase
class RemoveStudentUseCase(
    private val userService: UserService,
    private val studentService: StudentService,
    private val commandRemainStatusPort: CommandRemainStatusPort,
    private val commandStudyRoomPort: CommandStudyRoomPort
) {

    fun execute(studentId: UUID) {

        val user = userService.getCurrentUser()
        val student = studentService.queryStudentById(studentId)
        validateSameSchool(student.schoolId, user.schoolId)

        commandRemainStatusPort.deleteByStudentId(studentId)
        commandStudyRoomPort.deleteSeatApplicationByStudentId(studentId)

        studentService.deleteStudent(student)
        student.userId?.let { userService.deleteUserById(it) }
    }
}
