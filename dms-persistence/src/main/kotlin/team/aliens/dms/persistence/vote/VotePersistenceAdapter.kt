package team.aliens.dms.persistence.vote

import com.querydsl.core.types.Expression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.NumberTemplate
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.spi.VotePort
import team.aliens.dms.domain.vote.spi.vo.OptionVotingResultVO
import team.aliens.dms.domain.vote.spi.vo.StudentVotingResultVO
import team.aliens.dms.persistence.point.entity.QPointHistoryJpaEntity
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import team.aliens.dms.persistence.vote.entity.QVoteJpaEntity.voteJpaEntity
import team.aliens.dms.persistence.vote.entity.QVotingOptionJpaEntity.votingOptionJpaEntity
import team.aliens.dms.persistence.vote.entity.QVotingTopicJpaEntity.votingTopicJpaEntity
import team.aliens.dms.persistence.vote.mapper.VoteMapper
import team.aliens.dms.persistence.vote.repository.VoteJpaRepository
import team.aliens.dms.persistence.vote.repository.vo.QQueryOptionVotingResultVO
import team.aliens.dms.persistence.vote.repository.vo.QQueryStudentVotingResultVO
import team.aliens.dms.persistence.vote.repository.vo.QueryStudentVotingResultVO
import java.util.UUID

@Component
class VotePersistenceAdapter(
    private val voteMapper: VoteMapper,
    private val voteJpaRepository: VoteJpaRepository,
    private val queryFactory: JPAQueryFactory
) : VotePort {

    override fun saveVote(vote: Vote): Vote = voteMapper.toDomain(
        voteJpaRepository.save(voteMapper.toEntity(vote))
    )!!

    override fun deleteVoteById(voteId: UUID) = voteJpaRepository.deleteById(voteId)

    override fun queryVoteById(voteId: UUID): Vote? = voteMapper.toDomain(
        voteJpaRepository.findByIdOrNull(voteId)
    )

    override fun queryStudentVotingByVotingTopicIdAndGrade(votingTopicId: UUID, grade: Int): List<StudentVotingResultVO> {
        return queryFactory
            .select(
                QQueryStudentVotingResultVO(
                    studentJpaEntity.id,
                    studentJpaEntity.name,
                    voteJpaEntity.id.count().intValue()
                )
            )
            .from(voteJpaEntity)
            .join(voteJpaEntity.selectedStudent, studentJpaEntity)
            .join(voteJpaEntity.votingTopic, votingTopicJpaEntity)
            .where(
                votingTopicJpaEntity.id.eq(votingTopicId),
                studentJpaEntity.grade.eq(grade)
            )
            .groupBy(studentJpaEntity.id, studentJpaEntity.name)
            .orderBy(voteJpaEntity.id.count().intValue().desc())
            .fetch()
    }

    override fun queryOptionVotingByVotingTopicId(votingTopicId: UUID): List<OptionVotingResultVO> {
        return queryFactory
            .select(
                QQueryOptionVotingResultVO(
                    votingOptionJpaEntity.id,
                    votingOptionJpaEntity.optionName,
                    voteJpaEntity.id.count().intValue().coalesce(0)
                )
            )
            .from(votingOptionJpaEntity)
            .leftJoin(voteJpaEntity).on(voteJpaEntity.selectedOption.eq(votingOptionJpaEntity))
            .where(votingOptionJpaEntity.votingTopic.id.eq(votingTopicId))
            .groupBy(votingOptionJpaEntity.id, votingOptionJpaEntity.optionName)
            .orderBy(voteJpaEntity.id.count().intValue().desc())
            .fetch()
    }

    override fun queryVoteByStudentId(studentId: UUID): List<Vote> =
        voteJpaRepository.findByStudentId(studentId).map { entity ->
            voteMapper.toDomain(entity)!!
        }

    override fun queryModelStudentVotingByVotingTopicIdAndGrade(
        votingTopicId: UUID,
        grade: Int
    ): List<StudentVotingResultVO> {
        val subPointHistoryJpaEntity = QPointHistoryJpaEntity("subPointHistory")

        val selectedStudentGcnExpr =
            voteJpaEntity.selectedStudent.grade.multiply(1000)
                .add(voteJpaEntity.selectedStudent.classRoom.multiply(100))
                .add(voteJpaEntity.selectedStudent.number).stringValue()

        val bonusTotalSubQuery: Expression<Int> = JPAExpressions
            .select(subPointHistoryJpaEntity.bonusTotal)
            .from(subPointHistoryJpaEntity)
            .where(
                subPointHistoryJpaEntity.studentGcn.eq(selectedStudentGcnExpr),
                subPointHistoryJpaEntity.createdAt.eq(
                    JPAExpressions.select(subPointHistoryJpaEntity.createdAt.max())
                        .from(subPointHistoryJpaEntity)
                        .where(
                            subPointHistoryJpaEntity.studentGcn.eq(selectedStudentGcnExpr)
                        )
                )
            )

        val bonusTotalOrderExpr: NumberTemplate<Int> = Expressions.numberTemplate(
            Int::class.java,
            "({0})",
            bonusTotalSubQuery
        )

        val results = queryFactory
            .select(
                studentJpaEntity.id,
                studentJpaEntity.name,
                voteJpaEntity.id.count().intValue().coalesce(0),
                bonusTotalOrderExpr
            )
            .from(voteJpaEntity)
            .join(voteJpaEntity.selectedStudent, studentJpaEntity)
            .join(voteJpaEntity.votingTopic, votingTopicJpaEntity)
            .where(
                votingTopicJpaEntity.id.eq(votingTopicId),
                studentJpaEntity.grade.eq(grade)
            )
            .groupBy(studentJpaEntity.id, studentJpaEntity.name)
            .orderBy(
                voteJpaEntity.id.count().intValue().desc(),
                bonusTotalOrderExpr.intValue().asc()
            )
            .fetch()

        return results.map {
            QueryStudentVotingResultVO(
                id = it.get(studentJpaEntity.id)!!,
                name = it.get(studentJpaEntity.name)!!,
                votes = it.get(voteJpaEntity.id.count().intValue().coalesce(0))!!
            )
        }
    }
}
