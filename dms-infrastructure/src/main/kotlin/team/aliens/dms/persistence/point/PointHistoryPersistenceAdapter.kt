package team.aliens.dms.persistence.point

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PointHistoryPort
import team.aliens.dms.persistence.point.mapper.PointHistoryMapper
import team.aliens.dms.persistence.point.repository.PointHistoryJpaRepository

import team.aliens.dms.persistence.point.entity.QPointHistoryJpaEntity.pointHistoryJpaEntity
import team.aliens.dms.persistence.point.entity.QPointOptionJpaEntity.pointOptionJpaEntity
import java.util.UUID

@Component
class PointHistoryPersistenceAdapter(
    private val pointHistoryMapper: PointHistoryMapper,
    private val pointHistoryRepository: PointHistoryJpaRepository,
    private val queryFactory: JPAQueryFactory
) : PointHistoryPort {

    override fun queryTotalBonusPoint(studentId: UUID): Int {
        return queryFactory
            .select(pointOptionJpaEntity.score.sum())
            .from(pointHistoryJpaEntity)
            .join(pointHistoryJpaEntity.pointOption, pointOptionJpaEntity)
            .where(
                pointHistoryJpaEntity.student.userId.eq(studentId),
                pointOptionJpaEntity.type.eq(PointType.BONUS)
            )
            .fetchOne()!!
    }

    override fun queryTotalMinusPoint(studentId: UUID): Int {
        return queryFactory
            .select(pointOptionJpaEntity.score.sum())
            .from(pointHistoryJpaEntity)
            .join(pointHistoryJpaEntity.pointOption, pointOptionJpaEntity)
            .where(
                pointHistoryJpaEntity.student.userId.eq(studentId),
                pointOptionJpaEntity.type.eq(PointType.MINUS)
            )
            .fetchOne()!!
    }
}