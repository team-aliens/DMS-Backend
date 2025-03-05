package team.aliens.dms.domain.vote.spi.vo

import java.util.UUID

open class StudentVotingResultVO(
    val id: UUID,
    val name: String,
    val votes: Int
)
