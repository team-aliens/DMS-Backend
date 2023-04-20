package team.aliens.dms.domain.student.service

import java.time.LocalDateTime
import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.CommandStudentPort

@Service
class CommandStudentServiceImpl(
    private val commandStudentPort: CommandStudentPort
) : CommandStudentService {

    override fun saveStudent(student: Student) =
        commandStudentPort.saveStudent(student)

    override fun saveAllStudent(students: List<Student>) =
        commandStudentPort.saveAllStudent(students)

    override fun deleteStudent(student: Student) {
        commandStudentPort.saveStudent(
            student.copy(deletedAt = LocalDateTime.now())
        )
    }
}
