package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.spi.CommandRemainStatusPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class RemoveStudentUseCase(
    private val userService: UserService,
    private val studentService: StudentService,
    private val commandRemainStatusPort: CommandRemainStatusPort,
    private val commandStudyRoomPort: CommandStudyRoomPort
) {

    fun execute(studentId: UUID) {

        val user = userService.getCurrentUser()
        val student = studentService.getStudentById(studentId, user.schoolId)

        commandRemainStatusPort.deleteByStudentId(studentId)
        commandStudyRoomPort.deleteSeatApplicationByStudentId(studentId)

        studentService.deleteStudent(student)
        student.userId?.let { userService.deleteUserById(it) }
    }
}
