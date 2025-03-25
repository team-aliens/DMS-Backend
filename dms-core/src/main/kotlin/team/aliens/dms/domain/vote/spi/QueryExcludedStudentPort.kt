package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.ExcludedStudent
import java.util.UUID

interface QueryExcludedStudentPort {

    fun existExcludedStudentById(excludedStudentId: UUID): Boolean

    fun queryAllExcludedStudents(): List<ExcludedStudent>
}
