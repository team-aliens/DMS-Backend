package team.aliens.dms.domain.vote.dto.response

data class VotesResponse(
    val votes: List<VoteResponse>
) {
    companion object {
        fun of(
            votes: List<VoteResponse>
        ) = VotesResponse(
            votes = votes
        )
    }
}
