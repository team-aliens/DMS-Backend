package team.aliens.dms.persistence.point

import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.dto.QueryAllPointHistoryResponse
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PointPort
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.persistence.point.entity.QPointHistoryJpaEntity.pointHistoryJpaEntity
import team.aliens.dms.persistence.point.entity.QPointOptionJpaEntity.pointOptionJpaEntity
import team.aliens.dms.persistence.point.mapper.PointHistoryMapper
import team.aliens.dms.persistence.point.repository.PointHistoryJpaRepository
import team.aliens.dms.persistence.point.repository.vo.QQueryAllPointHistoryVO
import team.aliens.dms.persistence.point.repository.vo.QQueryPointHistoryVO
import team.aliens.dms.persistence.point.repository.vo.QueryAllPointHistoryVO
import java.util.UUID

@Component
class PointPersistenceAdapter(
    private val pointHistoryMapper: PointHistoryMapper,
    private val pointHistoryRepository: PointHistoryJpaRepository,
    private val queryFactory: JPAQueryFactory
) : PointPort {

    override fun queryBonusAndMinusTotalPointByStudent(student: Student): Pair<Int, Int> {
        val lastHistory = queryFactory
            .selectFrom(pointHistoryJpaEntity)
            .orderBy(pointHistoryJpaEntity.createdAt.desc())
            .limit(1)
            .fetchOne()

        val bonusTotal = lastHistory?.bonusTotal ?: 0
        val minusTotal = lastHistory?.bonusTotal ?: 0

        return Pair(bonusTotal, minusTotal)
    }

    override fun queryGrantedPointHistoryByStudentAndType(
        student: Student,
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
            .where(
                pointHistoryJpaEntity.isCancel.eq(false),
                pointHistoryJpaEntity.gcn.eq(student.gcn),
                pointHistoryJpaEntity.name.eq(student.name),
                pointOptionJpaEntity.type.eq(type)
            )
            .orderBy(pointHistoryJpaEntity.createdAt.desc())
            .fetch()
            .map {
                QueryPointHistoryResponse.Point(
                    pointHistoryId = it.pointId,
                    date = it.date.toLocalDate(),
                    type = it.type,
                    name = it.name,
                    score = it.score
                )
            }
    }

    override fun queryGrantedPointHistoryByStudent(student: Student): List<QueryPointHistoryResponse.Point> {
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
            .where(
                pointHistoryJpaEntity.isCancel.eq(false),
                pointHistoryJpaEntity.gcn.eq(student.gcn),
                pointHistoryJpaEntity.name.eq(student.name)
            )
            .orderBy(pointHistoryJpaEntity.createdAt.desc())
            .fetch()
            .map {
                QueryPointHistoryResponse.Point(
                    pointHistoryId = it.pointId,
                    date = it.date.toLocalDate(),
                    type = it.type,
                    name = it.name,
                    score = it.score
                )
            }
    }
}