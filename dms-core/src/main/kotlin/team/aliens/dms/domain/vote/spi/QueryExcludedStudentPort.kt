package team.aliens.dms.domain.vote.spi

import java.util.UUID

interface QueryExcludedStudentPort {

    fun queryExcludedStudentById(excludedStudentId: UUID) : Boolean
}
