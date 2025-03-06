package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.Vote
import java.util.UUID

interface CommandVotePort {

    fun saveVote(vote: Vote): Vote

    fun deleteVoteById(voteId: UUID)
}
