package team.aliens.dms.persistence.vote

import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.VotingTopicPort
import team.aliens.dms.persistence.vote.entity.QVoteJpaEntity.voteJpaEntity
import team.aliens.dms.persistence.vote.entity.QVotingTopicJpaEntity.votingTopicJpaEntity
import team.aliens.dms.persistence.vote.mapper.VotingTopicMapper
import team.aliens.dms.persistence.vote.repository.VotingTopicJpaRepository
import team.aliens.dms.persistence.vote.repository.vo.QQueryVotingTopicResultVO
import team.aliens.dms.persistence.vote.repository.vo.QueryVotingTopicResultVO
import java.util.UUID

@Component
class VotingTopicPersistenceAdapter(
    private val votingTopicMapper: VotingTopicMapper,
    private val votingTopicJpaRepository: VotingTopicJpaRepository,
    private val queryFactory: JPAQueryFactory
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

    override fun queryAllVotingTopic(userId: UUID): List<QueryVotingTopicResultVO> {
        return queryFactory
            .select(
                QQueryVotingTopicResultVO(
                    votingTopicJpaEntity.id,
                    votingTopicJpaEntity.topicName,
                    votingTopicJpaEntity.description,
                    votingTopicJpaEntity.startTime,
                    votingTopicJpaEntity.endTime,
                    votingTopicJpaEntity.voteType,
                    JPAExpressions.selectOne()
                        .from(voteJpaEntity)
                        .where(
                            voteJpaEntity.votingTopic.id.eq(votingTopicJpaEntity.id)
                                .and(voteJpaEntity.student.id.eq(userId))
                        )
                        .exists()
                )
            )
            .from(votingTopicJpaEntity)
            .fetch()
    }
}
