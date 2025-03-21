package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.ExcludedStudent

interface QueryExcludedStudentPort {

    fun queryAllExcludedStudents(): List<ExcludedStudent>
}
