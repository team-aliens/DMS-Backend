package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.VotingOption
import java.util.UUID

interface CommandVotingOptionPort {

    fun saveVotingOption(votingOption: VotingOption): VotingOption

    fun deleteVotingOptionByVotingOptionId(votingOptionId: UUID)
}
