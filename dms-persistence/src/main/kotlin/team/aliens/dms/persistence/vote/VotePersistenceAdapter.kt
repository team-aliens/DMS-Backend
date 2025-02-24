package team.aliens.dms.persistence.vote

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.VotePort
import team.aliens.dms.persistence.vote.mapper.VotingTopicMapper
import team.aliens.dms.persistence.vote.repository.VotingTopicJpaRepository
import java.util.UUID

@Component
class VotePersistenceAdapter(
    private val votingTopicJpaRepository: VotingTopicJpaRepository,
    private val votingTopicMapper: VotingTopicMapper,
) : VotePort {

    override fun saveVotingTopic(votingTopic: VotingTopic) = votingTopicMapper.toDomain(
        votingTopicJpaRepository.save(
            votingTopicMapper.toEntity(votingTopic)
        )
    )!!

    override fun deleteVotingTopicById(votingTopicId: UUID) {
        votingTopicJpaRepository.deleteById(votingTopicId)
    }

    override fun queryVotingTopicById(votingTopicId: UUID) = votingTopicMapper.toDomain(
        votingTopicJpaRepository.findByIdOrNull(votingTopicId)
    )

    override fun queryAllVotingTopic() = votingTopicJpaRepository.findAll().map {
        votingTopicMapper.toDomain(it)!!
    }
}
