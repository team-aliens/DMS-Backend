package team.aliens.dms.persistence.point

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PointPort
import team.aliens.dms.domain.student.model.Student
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

    override fun queryPointHistoryByStudentAndType(
        student: Student,
        type: PointType
    ): List<QueryPointHistoryResponse.Point> {
        return queryFactory
            .select(
                QQueryPointHistoryVO(
                    pointHistoryJpaEntity.id,
                    pointHistoryJpaEntity.createdAt!!,
                    pointHistoryJpaEntity.type,
                    pointHistoryJpaEntity.studentName,
                    pointHistoryJpaEntity.score
                )
            )
            .from(pointHistoryJpaEntity)
            .where(
                pointHistoryJpaEntity.gcn.eq(student.gcn),
                pointHistoryJpaEntity.studentName.eq(student.name),
                pointHistoryJpaEntity.type.eq(type)
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

    override fun queryAllPointHistoryByStudent(student: Student): List<QueryPointHistoryResponse.Point> {
        return queryFactory
            .select(
                QQueryPointHistoryVO(
                    pointHistoryJpaEntity.id,
                    pointHistoryJpaEntity.createdAt!!,
                    pointHistoryJpaEntity.type,
                    pointHistoryJpaEntity.studentName,
                    pointHistoryJpaEntity.score
                )
            )
            .from(pointHistoryJpaEntity)
            .where(
                pointHistoryJpaEntity.gcn.eq(student.gcn),
                pointHistoryJpaEntity.studentName.eq(student.name)
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

    override fun queryTotalBonusPoint(studentId: UUID): Int {
        return queryFactory
            .select(pointOptionJpaEntity.score.sum())
            .from(pointHistoryJpaEntity)
            .where(
                // TODO: tbl_point_total 추가시 대체
                //pointHistoryJpaEntity.student.id.eq(studentId),
                pointOptionJpaEntity.type.eq(PointType.BONUS)
            )
            .fetchOne() ?: 0
    }

    override fun queryTotalMinusPoint(studentId: UUID): Int {
        return queryFactory
            .select(pointOptionJpaEntity.score.sum())
            .from(pointHistoryJpaEntity)
            .where(
                //pointHistoryJpaEntity.student.id.eq(studentId),
                pointOptionJpaEntity.type.eq(PointType.MINUS)
            )
            .fetchOne() ?: 0
    }

}