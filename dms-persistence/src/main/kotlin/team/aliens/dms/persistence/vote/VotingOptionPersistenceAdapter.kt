package team.aliens.dms.persistence.vote

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.spi.VotingOptionPort
import team.aliens.dms.persistence.vote.entity.QVotingOptionJpaEntity
import team.aliens.dms.persistence.vote.mapper.VotingOptionMapper
import team.aliens.dms.persistence.vote.repository.VotingOptionJpaRepository
import java.util.UUID

class VotingOptionPersistenceAdapter(
    private val votingOptionMapper: VotingOptionMapper,
    private val votingOptionJpaRepository: VotingOptionJpaRepository,
    private val queryFactory: JPAQueryFactory
) : VotingOptionPort {

    override fun queryVotingOptionById(votingOptionId: UUID): VotingOption? = votingOptionMapper.toDomain(
        votingOptionJpaRepository.findByIdOrNull(votingOptionId)
    )

    override fun queryVotingOptionsByVotingTopicId(votingTopicId: UUID): List<VotingOption> {
        return queryFactory
            .selectFrom(QVotingOptionJpaEntity.votingOptionJpaEntity)
            .where(QVotingOptionJpaEntity.votingOptionJpaEntity.votingTopic.id.eq(votingTopicId))
            .fetch()
            .map { entity ->
                VotingOption(
                    id = entity.id!!,
                    votingTopicId = entity.votingTopic!!.id!!,
                    optionName = entity.optionName
                )
            }
    }

    override fun existVotingOptionById(votingOptionId: UUID): Boolean =
        votingOptionJpaRepository.existsById(votingOptionId)

    override fun saveVotingOption(votingOption: VotingOption): VotingOption = votingOptionMapper.toDomain(
        votingOptionJpaRepository.save(votingOptionMapper.toEntity(votingOption))
    )!!

    override fun deleteVotingOptionByVotingOptionId(votingOptionId: UUID) = votingOptionJpaRepository.deleteById(votingOptionId)
}
