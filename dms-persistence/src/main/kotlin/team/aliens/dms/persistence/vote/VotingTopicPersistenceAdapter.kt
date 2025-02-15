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

    override fun queryStartTimeById(id: UUID): LocalDateTime? {
        return votingTopicJpaRepository.findStartTimeById(id)
    }

    override fun queryEndTimeById(id: UUID): LocalDateTime? {
        return votingTopicJpaRepository.findEndTimeById(id)
    }

    override fun saveVotingTopic(votingTopic: VotingTopic): UUID {
        return votingTopicJpaRepository.save(
            votingTopicMapper.toEntity(
                votingTopic
            )
        ).id!!
    }

    override fun deleteVotingTopicById(id: UUID) {
        votingTopicJpaRepository.deleteById(id)
    }

    override fun queryVotingTopicById(id: UUID) = votingTopicMapper.toDomain(
        votingTopicJpaRepository.findByIdOrNull(id)
    )

    override fun queryAllVotingTopic(): List<VotingTopic?> {
        return votingTopicJpaRepository.findAll().map {
            votingTopicMapper.toDomain(it)
        }
    }
}
