package team.aliens.dms.domain.file.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.file.spi.ParseFilePort
import team.aliens.dms.domain.student.spi.CommandStudentPort
import java.io.File

@UseCase
class ImportVerifiedStudentUseCase(
    private val parseFilePort: ParseFilePort,
    private val commandStudentPort: CommandStudentPort
) {

    fun execute(file: File) {
        val verifiedStudents = parseFilePort.transferToVerifiedStudent(file)

        commandStudentPort.saveAllVerifiedStudent(verifiedStudents)
    }
}
