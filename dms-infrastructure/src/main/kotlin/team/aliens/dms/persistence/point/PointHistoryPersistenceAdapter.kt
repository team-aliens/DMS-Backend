package team.aliens.dms.persistence.point

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.dto.QueryAllPointHistoryResponse
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PointHistoryPort
import team.aliens.dms.persistence.point.entity.QPointHistoryJpaEntity.pointHistoryJpaEntity
import team.aliens.dms.persistence.point.mapper.PointHistoryMapper
import team.aliens.dms.persistence.point.repository.PointHistoryJpaRepository
import team.aliens.dms.persistence.point.repository.vo.QQueryAllPointHistoryVO
import team.aliens.dms.persistence.point.repository.vo.QQueryPointHistoryVO
import java.time.LocalDateTime
import java.util.UUID

@Component
class PointHistoryPersistenceAdapter(
    private val pointHistoryMapper: PointHistoryMapper,
    private val pointHistoryRepository: PointHistoryJpaRepository,
    private val queryFactory: JPAQueryFactory
) : PointHistoryPort {

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
        val minusTotal = lastHistory?.minusTotal ?: 0

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
                    pointHistoryJpaEntity.createdAt,
                    pointHistoryJpaEntity.pointType,
                    pointHistoryJpaEntity.pointName,
                    pointHistoryJpaEntity.pointScore
                )
            )
            .from(pointHistoryJpaEntity)
            .where(
                pointHistoryJpaEntity.studentGcn.eq(gcn),
                pointHistoryJpaEntity.studentName.eq(studentName),
                type?.let { pointHistoryJpaEntity.pointType.eq(it) },
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

    override fun queryPointHistoryBySchoolIdAndType(
        schoolId: UUID,
        type: PointType?,
        isCancel: Boolean?,
        pageData: PageData
    ): List<QueryAllPointHistoryResponse.PointHistory> {
        return queryFactory
            .select(
                QQueryAllPointHistoryVO(
                    pointHistoryJpaEntity.id,
                    pointHistoryJpaEntity.studentName,
                    pointHistoryJpaEntity.studentGcn,
                    pointHistoryJpaEntity.createdAt,
                    pointHistoryJpaEntity.pointName,
                    pointHistoryJpaEntity.pointType,
                    pointHistoryJpaEntity.pointScore
                )
            )
            .from(pointHistoryJpaEntity)
            .where(
                pointHistoryJpaEntity.school.id.eq(schoolId),
                type?.let { pointHistoryJpaEntity.pointType.eq(it) },
                isCancel?.let { pointHistoryJpaEntity.isCancel.eq(it) }
            )
            .offset(pageData.offset)
            .limit(pageData.size)
            .orderBy(pointHistoryJpaEntity.createdAt.desc())
            .fetch()
            .map {
                QueryAllPointHistoryResponse.PointHistory(
                    pointHistoryId = it.pointHistoryId,
                    studentName = it.studentName,
                    studentGcn = it.studentGcn,
                    date = it.date.toLocalDate(),
                    pointName = it.pointName,
                    pointType = it.pointType,
                    pointScore = it.pointScore
                )
            }
    }

    override fun queryPointHistoryBySchoolIdAndCreatedAtBetween(
        schoolId: UUID,
        startAt: LocalDateTime?,
        endAt: LocalDateTime?
    ): List<PointHistory> {
        return queryFactory
            .selectFrom(pointHistoryJpaEntity)
            .where(
                pointHistoryJpaEntity.school.id.eq(schoolId),
                startAt?.let { pointHistoryJpaEntity.createdAt.goe(it) },
                endAt?.let { pointHistoryJpaEntity.createdAt.lt(it) }
            )
            .orderBy(pointHistoryJpaEntity.createdAt.desc())
            .fetch()
            .mapNotNull {
                pointHistoryMapper.toDomain(it)
            }
    }

    override fun saveAllPointHistories(pointHistories: List<PointHistory>) {
        pointHistoryRepository.saveAll(
            pointHistories.map {
                pointHistoryMapper.toEntity(it)
            }
        )
    }
}
