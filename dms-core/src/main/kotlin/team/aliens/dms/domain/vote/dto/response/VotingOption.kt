package team.aliens.dms.domain.vote.dto.response

import java.util.UUID

class VotingOption(
    val id: UUID,
    val votingOptionName: String,
) {
    companion object {
        fun of(
            id: UUID,
            votingOptionName: String
        ) = VotingOption(
            id = id,
            votingOptionName = votingOptionName
        )
    }
}
