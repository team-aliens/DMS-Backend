package team.aliens.dms.persistence.vote

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.VotingTopicPort
import team.aliens.dms.persistence.vote.mapper.VotingTopicMapper
import team.aliens.dms.persistence.vote.repository.VotingTopicJpaRepository
import java.time.LocalDateTime
import java.util.UUID


@Component
class VotingTopicPersistenceAdapter(
    private val votingTopicJpaRepository: VotingTopicJpaRepository,
    private val votingTopicMapper: VotingTopicMapper,
) : VotingTopicPort {

    override fun saveVotingTopic(votingTopic: VotingTopic) = votingTopicJpaRepository.save(
        votingTopicMapper.toEntity(
            votingTopic
        )
    ).id!!

    override fun deleteVotingTopicById(votingTopicId: UUID) {
        votingTopicJpaRepository.deleteById(votingTopicId)
    }

    override fun queryVotingTopicById(votingTopicId: UUID) = votingTopicMapper.toDomain(
        votingTopicJpaRepository.findByIdOrNull(votingTopicId)
    )

    override fun queryAllVotingTopic() = votingTopicJpaRepository.findAll().map {
        votingTopicMapper.toDomain(it)
    }
}
