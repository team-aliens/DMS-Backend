package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.ExcludedStudent

interface CommandExcludedStudentPort {

    fun saveExcludedStudent(excludedStudent: ExcludedStudent): ExcludedStudent
}
