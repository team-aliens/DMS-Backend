package team.aliens.dms.domain.vote.dto.response

class VotingOptionsResponse(
    val votingOptions: List<VotingOption>
)
{
    companion object {
        fun of(
                votingOptions: List<VotingOption>
        ) = VotingOptionsResponse(
                votingOptions = votingOptions
        )
    }
}
