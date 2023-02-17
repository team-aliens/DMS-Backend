package team.aliens.dms.persistence.point

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PointPort
import team.aliens.dms.persistence.point.entity.QPointHistoryJpaEntity.pointHistoryJpaEntity
import team.aliens.dms.persistence.point.mapper.PointHistoryMapper
import team.aliens.dms.persistence.point.repository.PointHistoryJpaRepository
import team.aliens.dms.persistence.point.repository.vo.QQueryPointHistoryVO

@Component
class PointPersistenceAdapter(
    private val pointHistoryMapper: PointHistoryMapper,
    private val pointHistoryRepository: PointHistoryJpaRepository,
    private val queryFactory: JPAQueryFactory
) : PointPort {

    override fun queryBonusAndMinusTotalPointByStudentGcnAndName(
        gcn: String,
        studentName: String
    ): Pair<Int, Int> {
        val lastHistory = queryFactory
            .selectFrom(pointHistoryJpaEntity)
            .where(
                pointHistoryJpaEntity.studentGcn.eq(gcn),
                pointHistoryJpaEntity.studentName.eq(studentName)
            )
            .orderBy(pointHistoryJpaEntity.createdAt.desc())
            .fetchFirst()

        val bonusTotal = lastHistory?.bonusTotal ?: 0
        val minusTotal = lastHistory?.bonusTotal ?: 0

        return Pair(bonusTotal, minusTotal)
    }

    override fun queryPointHistoryByStudentGcnAndNameAndType(
        gcn: String,
        studentName: String,
        type: PointType?,
        isCancel: Boolean?
    ): List<QueryPointHistoryResponse.Point> {
        return queryFactory
            .select(
                QQueryPointHistoryVO(
                    pointHistoryJpaEntity.createdAt!!,
                    pointHistoryJpaEntity.pointType,
                    pointHistoryJpaEntity.pointName,
                    pointHistoryJpaEntity.pointScore
                )
            )
            .from(pointHistoryJpaEntity)
            .where(
                pointHistoryJpaEntity.studentGcn.eq(gcn),
                pointHistoryJpaEntity.studentName.eq(studentName),
                type?.let {pointHistoryJpaEntity.pointType.eq(it) },
                isCancel?.let { pointHistoryJpaEntity.isCancel.eq(it) }
            )
            .orderBy(pointHistoryJpaEntity.createdAt.desc())
            .fetch()
            .map {
                QueryPointHistoryResponse.Point(
                    date = it.date.toLocalDate(),
                    type = it.pointType,
                    name = it.pointName,
                    score = it.pointScore
                )
            }
    }
}
