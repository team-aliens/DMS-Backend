package team.aliens.dms.persistence.point

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.JPAExpressions.select
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PointHistoryPort
import team.aliens.dms.domain.point.spi.vo.PointHistoryVO
import team.aliens.dms.domain.point.spi.vo.StudentPointHistoryVO
import team.aliens.dms.domain.point.spi.vo.StudentTotalVO
import team.aliens.dms.persistence.point.entity.QPointHistoryJpaEntity.pointHistoryJpaEntity
import team.aliens.dms.persistence.point.mapper.PointHistoryMapper
import team.aliens.dms.persistence.point.repository.PointHistoryJpaRepository
import team.aliens.dms.persistence.point.repository.vo.QQueryAllPointHistoryVO
import team.aliens.dms.persistence.point.repository.vo.QQueryPointHistoryVO
import team.aliens.dms.persistence.point.repository.vo.QQueryStudentTotalVO
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import java.time.LocalDateTime
import java.util.UUID

@Component
class PointHistoryPersistenceAdapter(
    private val pointHistoryMapper: PointHistoryMapper,
    private val pointHistoryRepository: PointHistoryJpaRepository,
    private val queryFactory: JPAQueryFactory
) : PointHistoryPort {

    override fun savePointHistory(pointHistory: PointHistory) = pointHistoryMapper.toDomain(
        pointHistoryRepository.save(
            pointHistoryMapper.toEntity(pointHistory)
        )
    )!!

    override fun deletePointHistory(pointHistory: PointHistory) {
        pointHistoryRepository.delete(
            pointHistoryMapper.toEntity(pointHistory)
        )
    }

    override fun queryPointHistoryById(pointHistoryId: UUID) = pointHistoryMapper.toDomain(
        pointHistoryRepository.findByIdOrNull(pointHistoryId)
    )

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
        isCancel: Boolean?,
        pageData: PageData
    ): List<PointHistoryVO> {
        return queryFactory
            .select(
                QQueryPointHistoryVO(
                    pointHistoryJpaEntity.id,
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
            .offset(pageData.offset)
            .limit(pageData.size)
            .orderBy(pointHistoryJpaEntity.createdAt.desc())
            .fetch()
    }

    override fun queryPointHistoryBySchoolIdAndType(
        schoolId: UUID,
        type: PointType?,
        studentName: String?,
        studentGcn: String?,
        isCancel: Boolean?,
        pageData: PageData
    ): List<StudentPointHistoryVO> {
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
                studentName?.let { pointHistoryJpaEntity.studentName.eq(it) },
                studentGcn?.let { pointHistoryJpaEntity.studentGcn.eq(it) },
                type?.let { pointHistoryJpaEntity.pointType.eq(it) },
                isCancel?.let { pointHistoryJpaEntity.isCancel.eq(it) }
            )
            .offset(pageData.offset)
            .limit(pageData.size)
            .orderBy(pointHistoryJpaEntity.createdAt.desc())
            .fetch()
    }

    override fun queryPointHistoryBySchoolIdAndCreatedAtBetween(
        schoolId: UUID,
        startAt: LocalDateTime?,
        endAt: LocalDateTime?
    ): List<PointHistory> {
        return queryFactory
            .selectFrom(pointHistoryJpaEntity)
            .where(
                pointHistoryJpaEntity.isCancel.eq(false),
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

    override fun queryPointHistoryByGcnIn(gcns: List<String>) =
        pointHistoryRepository.findByStudentGcnIn(gcns)
            .map { pointHistoryMapper.toDomain(it)!! }

    override fun queryPointTotalsGroupByStudent(): List<StudentTotalVO> {
        return queryFactory
            .select(
                QQueryStudentTotalVO(
                    studentJpaEntity.id,
                    pointHistoryJpaEntity.bonusTotal,
                    pointHistoryJpaEntity.minusTotal
                )
            )
            .from(studentJpaEntity)
            .innerJoin(pointHistoryJpaEntity).on(
                pointHistoryJpaEntity.createdAt.eq(
                    select(pointHistoryJpaEntity.createdAt.max())
                        .from(pointHistoryJpaEntity)
                        .where(
                            eqGcn(),
                            studentJpaEntity.name.eq(pointHistoryJpaEntity.studentName)
                        )
                )
            )
            .fetch()
    }

    private fun eqGcn(): BooleanBuilder {
        val condition = BooleanBuilder()
        val gcn = pointHistoryJpaEntity.studentGcn
        condition
            .and(gcn.substring(0, 1).eq(studentJpaEntity.grade.stringValue()))
            .and(gcn.substring(1, 2).endsWith(studentJpaEntity.classRoom.stringValue()))
            .and(gcn.substring(2).endsWith(studentJpaEntity.number.stringValue()))
        return condition
    }
}
