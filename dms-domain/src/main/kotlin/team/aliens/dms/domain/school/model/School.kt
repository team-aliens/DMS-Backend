package team.aliens.dms.domain.school.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.LocalDate
import java.util.UUID

@Aggregate
data class School(

    val id: UUID = UUID(0, 0),

    val name: String,

    val code: String,

    val question: String,

    val answer: String,

    val address: String,

    val contractStartedAt: LocalDate,

    val contractEndedAt: LocalDate?

)