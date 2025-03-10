package team.aliens.dms.domain.vote.dto.response

class VotingOptionResponses(
    val votingOptions: List<VotingOption>
)
{
    companion object {
        fun of(
                votingOptions: List<VotingOption>
        ) = VotingOptionResponses(
                votingOptions = votingOptions
        )
    }
}