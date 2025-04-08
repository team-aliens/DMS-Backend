package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.ExcludedStudent
import java.util.UUID

interface CommandExcludedStudentPort {

    fun saveExcludedStudent(excludedStudent: ExcludedStudent): ExcludedStudent

    fun deleteExcludedStudentById(excludedStudentId: UUID)

    fun clearExcludedStudents()
}
