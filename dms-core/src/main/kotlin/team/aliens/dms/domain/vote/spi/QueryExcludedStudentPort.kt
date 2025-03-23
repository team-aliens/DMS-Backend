package team.aliens.dms.domain.vote.spi

import java.util.UUID

interface QueryExcludedStudentPort {

    fun existExcludedStudentById(excludedStudentId: UUID) : Boolean
}
