package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.ExcludedStudent
import java.util.UUID

interface QueryExcludedStudentPort {

    fun queryAllExcludedStudents(): List<ExcludedStudent>

    fun queryExcludedStudentById(excludedStudentId: UUID): ExcludedStudent?
}
