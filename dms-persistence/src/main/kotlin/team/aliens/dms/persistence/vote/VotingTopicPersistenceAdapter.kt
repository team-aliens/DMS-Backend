package team.aliens.dms.persistence.vote

import team.aliens.dms.persistence.vote.mapper.VotingTopicMapper
import org.springframework.stereotype.Component
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.VotingTopicPort
import team.aliens.dms.persistence.vote.repository.VotingTopicJpaRepository
import java.time.LocalDateTime
import java.util.*

@Component
class VotingTopicPersistenceAdapter(
    private val votingTopicJpaRepository: VotingTopicJpaRepository,
    private val votingTopicMapper: VotingTopicMapper,

    ) : VotingTopicPort {
    override fun findStartTimeById(id:UUID): LocalDateTime? {
        return votingTopicJpaRepository.findStartTimeById(id)
    }

    override fun findEndTimeById(id:UUID): LocalDateTime? {
        return votingTopicJpaRepository.findEndTimeById(id)
    }

    override fun saveVotingTopic(votingTopic: VotingTopic) {
        votingTopicJpaRepository.save(
            votingTopicMapper.toEntity(
                votingTopic
            )
        )
    }

    override fun deleteVotingTopicById(id: UUID) {
       votingTopicJpaRepository.deleteById(id)
    }

}