package team.aliens.dms.persistence.point

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PointPort
import team.aliens.dms.persistence.point.entity.QPointHistoryJpaEntity.pointHistoryJpaEntity
import team.aliens.dms.persistence.point.entity.QPointOptionJpaEntity.pointOptionJpaEntity
import team.aliens.dms.persistence.point.mapper.PointHistoryMapper
import team.aliens.dms.persistence.point.repository.PointHistoryJpaRepository
import team.aliens.dms.persistence.point.repository.vo.QQueryPointHistoryVO
import java.util.UUID

@Component
class PointPersistenceAdapter(
    private val pointHistoryMapper: PointHistoryMapper,
    private val pointHistoryRepository: PointHistoryJpaRepository,
    private val queryFactory: JPAQueryFactory
) : PointPort {

    override fun queryPointHistoryByStudentIdAndType(
        studentId: UUID,
        type: PointType
    ): List<QueryPointHistoryResponse.Point> {
        return queryFactory
            .select(
                QQueryPointHistoryVO(
                    pointOptionJpaEntity.id,
                    pointHistoryJpaEntity.createdAt!!,
                    pointOptionJpaEntity.type,
                    pointOptionJpaEntity.name,
                    pointOptionJpaEntity.score
                )
            )
            .from(pointHistoryJpaEntity)
            .join(pointHistoryJpaEntity.pointOption, pointOptionJpaEntity)
            .where(
                pointHistoryJpaEntity.student.id.eq(studentId),
                pointOptionJpaEntity.type.eq(type)
            )
            .orderBy(pointHistoryJpaEntity.createdAt.desc())
            .fetch()
            .map {
                QueryPointHistoryResponse.Point(
                    pointId = it.pointId,
                    date = it.date.toLocalDate(),
                    type = it.type,
                    name = it.name,
                    score = it.score
                )
            }
    }

    override fun queryAllPointHistoryByStudentId(studentId: UUID): List<QueryPointHistoryResponse.Point> {
        return queryFactory
            .select(
                QQueryPointHistoryVO(
                    pointOptionJpaEntity.id,
                    pointHistoryJpaEntity.createdAt!!,
                    pointOptionJpaEntity.type,
                    pointOptionJpaEntity.name,
                    pointOptionJpaEntity.score
                )
            )
            .from(pointHistoryJpaEntity)
            .join(pointHistoryJpaEntity.pointOption, pointOptionJpaEntity)
            .where(pointHistoryJpaEntity.student.id.eq(studentId))
            .orderBy(pointHistoryJpaEntity.createdAt.desc())
            .fetch()
            .map {
                QueryPointHistoryResponse.Point(
                    pointId = it.pointId,
                    date = it.date.toLocalDate(),
                    type = it.type,
                    name = it.name,
                    score = it.score
                )
            }
    }

    override fun queryTotalBonusPoint(studentId: UUID): Int {
        return queryFactory
            .select(pointOptionJpaEntity.score.sum())
            .from(pointHistoryJpaEntity)
            .join(pointHistoryJpaEntity.pointOption, pointOptionJpaEntity)
            .where(
                pointHistoryJpaEntity.student.id.eq(studentId),
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
                pointHistoryJpaEntity.student.id.eq(studentId),
                pointOptionJpaEntity.type.eq(PointType.MINUS)
            )
            .fetchOne()!!
    }
}