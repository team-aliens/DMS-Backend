package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort

@UseCase
class UpdateStudentProfileUseCase(
    private val securityPort: StudentSecurityPort,
    private val queryStudentPort: QueryStudentPort,
    private val commandStudentPort: CommandStudentPort
) {

    fun execute(profileImageUrl: String) {
        val currentUserId = securityPort.getCurrentUserId()
        val student = queryStudentPort.queryStudentByUserId(currentUserId) ?: throw StudentNotFoundException

        commandStudentPort.saveStudent(
            student.copy(profileImageUrl = profileImageUrl)
        )
    }
}
