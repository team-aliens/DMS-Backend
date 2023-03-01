package team.aliens.dms.domain.file.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.file.spi.ParseFilePort
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.io.File

@UseCase
class ImportVerifiedStudentUseCase(
    private val parseFilePort: ParseFilePort,
    private val securityPort: StudentSecurityPort,
    private val queryUserPort: StudentQueryUserPort,
    private val querySchoolPort: StudentQuerySchoolPort,
    private val commandStudentPort: CommandStudentPort
) {

    fun execute(file: File) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException
        val school = querySchoolPort.querySchoolById(user.schoolId) ?: throw SchoolNotFoundException

        val verifiedStudents = parseFilePort.transferToVerifiedStudent(file, school.name)

        commandStudentPort.saveAllVerifiedStudent(verifiedStudents)
    }
}
