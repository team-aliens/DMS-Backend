package team.aliens.dms.domain.vote.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class VotingOption(
    val id: UUID = UUID(0, 0),

    val votingTopicId: UUID = UUID(0, 0),

    val optionName: String
)
