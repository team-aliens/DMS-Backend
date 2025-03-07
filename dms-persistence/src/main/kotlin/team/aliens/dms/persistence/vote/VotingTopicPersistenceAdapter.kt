package team.aliens.dms.persistence.vote

import org.springframework.data.repository.findByIdOrNull
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.VotingTopicPort
import team.aliens.dms.persistence.vote.mapper.VotingTopicMapper
import team.aliens.dms.persistence.vote.repository.VotingTopicJpaRepository
import java.util.UUID

class VotingTopicPersistenceAdapter(
    private val votingTopicMapper: VotingTopicMapper,
    private val votingTopicJpaRepository: VotingTopicJpaRepository
) : VotingTopicPort {

    override fun queryVotingTopicById(votingTopicId: UUID) = votingTopicMapper.toDomain(
        votingTopicJpaRepository.findByIdOrNull(votingTopicId)
    )

    override fun existVotingTopicByName(votingTopicName: String): Boolean =
        votingTopicJpaRepository.existsByTopicName(votingTopicName)

    override fun existVotingTopicById(votingTopicId: UUID): Boolean =
        votingTopicJpaRepository.existsById(votingTopicId)

    override fun saveVotingTopic(votingTopic: VotingTopic) = votingTopicMapper.toDomain(
        votingTopicJpaRepository.save(
            votingTopicMapper.toEntity(votingTopic)
        )
    )!!

    override fun deleteVotingTopicById(votingTopicId: UUID) {
        votingTopicJpaRepository.deleteById(votingTopicId)
    }

    override fun queryAllVotingTopic() = votingTopicJpaRepository.findAll().map {
        votingTopicMapper.toDomain(it)!!
    }
}
