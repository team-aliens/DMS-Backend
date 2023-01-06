package team.aliens.dms.domain.file.usecase

import java.io.File
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.file.spi.ParseFilePort

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