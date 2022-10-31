package team.aliens.dms.persistence.point

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.spi.PointHistoryPort
import team.aliens.dms.persistence.point.mapper.PointHistoryMapper
import team.aliens.dms.persistence.point.repository.PointHistoryJpaRepository
import java.util.*

import team.aliens.dms.persistence.point.entity.QPointHistoryJpaEntity.pointHistoryJpaEntity
import team.aliens.dms.persistence.point.entity.QPointOptionJpaEntity.pointOptionJpaEntity

@Component
class PointHistoryPersistenceAdapter(
    private val pointHistoryMapper: PointHistoryMapper,
    private val pointHistoryRepository: PointHistoryJpaRepository,
    private val queryFactory: JPAQueryFactory
) : PointHistoryPort {

    override fun queryPointScore(studentId: UUID, isBonus: Boolean): Int {
        return queryFactory
            .select(pointOptionJpaEntity.score.sum())
            .from(pointHistoryJpaEntity)
            .join(pointHistoryJpaEntity.pointOption, pointOptionJpaEntity)
            .where(
                pointHistoryJpaEntity.student.userId.eq(studentId),
                bonusOrMinus(isBonus)
            )
            .fetchOne()!!
    }

    private fun bonusOrMinus(isBonus: Boolean) =
        if (isBonus) pointOptionJpaEntity.score.gt(0) else pointOptionJpaEntity.score.lt(0)

}