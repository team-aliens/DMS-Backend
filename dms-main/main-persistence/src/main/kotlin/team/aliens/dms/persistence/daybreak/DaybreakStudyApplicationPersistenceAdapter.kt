package team.aliens.dms.persistence.daybreak

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.spi.DaybreakStudyApplicationPort
import team.aliens.dms.domain.daybreak.spi.vo.GeneralTeacherDaybreakStudyApplicationVO
import team.aliens.dms.domain.daybreak.spi.vo.HeadTeacherDaybreakStudyApplicationVO
import team.aliens.dms.domain.daybreak.spi.vo.ManagerDaybreakStudyApplicationVO
import team.aliens.dms.persistence.daybreak.entity.QDaybreakStudyApplicationJpaEntity.daybreakStudyApplicationJpaEntity
import team.aliens.dms.persistence.daybreak.mapper.DaybreakStudyApplicationMapper
import team.aliens.dms.persistence.daybreak.repository.DaybreakStudyApplicationJpaRepository
import team.aliens.dms.persistence.daybreak.repository.vo.QQueryGeneralTeacherDaybreakStudyApplicationVO
import team.aliens.dms.persistence.daybreak.repository.vo.QQueryHeadTeacherDaybreakStudyApplicationVO
import team.aliens.dms.persistence.daybreak.repository.vo.QQueryManagerDaybreakStudyApplicationVO
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Component
class DaybreakStudyApplicationPersistenceAdapter(
    private val daybreakStudyApplicationRepository: DaybreakStudyApplicationJpaRepository,
    private val daybreakStudyApplicationMapper: DaybreakStudyApplicationMapper,
    private val queryFactory: JPAQueryFactory
) : DaybreakStudyApplicationPort {

    override fun existActiveDaybreakStudyApplicationByStudentId(studentId: UUID): Boolean {
        val fetchOne = queryFactory
            .selectOne()
            .from(daybreakStudyApplicationJpaEntity)
            .where(
                daybreakStudyApplicationJpaEntity.studentJpaEntity.id.eq(studentId)
                    .and(daybreakStudyApplicationJpaEntity.status.ne(Status.EXPIRED))
            )
            .fetchFirst()

        return fetchOne != null
    }

    override fun generalTeacherGetDaybreakStudyApplications(
        typeId: UUID?,
        date: LocalDate,
        pageData: PageData
    ): List<GeneralTeacherDaybreakStudyApplicationVO> {
        return queryFactory
            .select(
                QQueryGeneralTeacherDaybreakStudyApplicationVO(
                    daybreakStudyApplicationJpaEntity.id,
                    daybreakStudyApplicationJpaEntity.daybreakStudyTypeJpaEntity.name,
                    daybreakStudyApplicationJpaEntity.createdAt,
                    daybreakStudyApplicationJpaEntity.startDate,
                    daybreakStudyApplicationJpaEntity.endDate,
                    daybreakStudyApplicationJpaEntity.status,
                    daybreakStudyApplicationJpaEntity.reason,
                    studentJpaEntity.name,
                    studentJpaEntity.grade,
                    studentJpaEntity.classRoom,
                    studentJpaEntity.number
                )
            )
            .from(daybreakStudyApplicationJpaEntity)
            .join(studentJpaEntity).on(daybreakStudyApplicationJpaEntity.studentJpaEntity.id.eq(studentJpaEntity.id))
            .where(
                dateFilter(date),
                typeFilter(typeId)
            )
            .offset(pageData.offset)
            .limit(pageData.size)
            .orderBy(daybreakStudyApplicationJpaEntity.createdAt.desc())
            .fetch()
    }

    override fun headTeacherGetDaybreakStudyApplications(
        typeId: UUID?,
        date: LocalDate,
        status: Status?,
        pageData: PageData
    ): List<HeadTeacherDaybreakStudyApplicationVO> {
        return queryFactory
            .select(
                QQueryHeadTeacherDaybreakStudyApplicationVO(
                    daybreakStudyApplicationJpaEntity.id,
                    daybreakStudyApplicationJpaEntity.daybreakStudyTypeJpaEntity.name,
                    daybreakStudyApplicationJpaEntity.teacherJpaEntity.name,
                    daybreakStudyApplicationJpaEntity.createdAt,
                    daybreakStudyApplicationJpaEntity.startDate,
                    daybreakStudyApplicationJpaEntity.endDate,
                    daybreakStudyApplicationJpaEntity.reason,
                    studentJpaEntity.name,
                    studentJpaEntity.grade,
                    studentJpaEntity.classRoom,
                    studentJpaEntity.number
                )
            )
            .from(daybreakStudyApplicationJpaEntity)
            .join(studentJpaEntity).on(daybreakStudyApplicationJpaEntity.studentJpaEntity.id.eq(studentJpaEntity.id))
            .where(
                dateFilter(date),
                typeFilter(typeId),
                statusFilter(status)
            )
            .offset(pageData.offset)
            .limit(pageData.size)
            .orderBy(daybreakStudyApplicationJpaEntity.createdAt.desc())
            .fetch()
    }

    override fun managerGetDaybreakStudyApplications(
        grade: Int?,
        pageData: PageData
    ): List<ManagerDaybreakStudyApplicationVO> {
        println(grade)
        return queryFactory
            .select(
                QQueryManagerDaybreakStudyApplicationVO(
                    daybreakStudyApplicationJpaEntity.id,
                    daybreakStudyApplicationJpaEntity.daybreakStudyTypeJpaEntity.name,
                    daybreakStudyApplicationJpaEntity.teacherJpaEntity.name,
                    daybreakStudyApplicationJpaEntity.createdAt,
                    daybreakStudyApplicationJpaEntity.startDate,
                    daybreakStudyApplicationJpaEntity.endDate,
                    daybreakStudyApplicationJpaEntity.reason,
                    studentJpaEntity.name,
                    studentJpaEntity.grade,
                    studentJpaEntity.classRoom,
                    studentJpaEntity.number
                )
            )
            .from(daybreakStudyApplicationJpaEntity)
            .join(studentJpaEntity).on(daybreakStudyApplicationJpaEntity.studentJpaEntity.id.eq(studentJpaEntity.id))
            .where(
                daybreakStudyApplicationJpaEntity.status.eq(Status.SECOND_APPROVED),
                grade?.let { studentJpaEntity.grade.eq(it) }
            )
            .offset(pageData.offset)
            .limit(pageData.size)
            .fetch()
    }

    override fun getAllByIdIn(ids: List<UUID>): List<DaybreakStudyApplication> {
        return daybreakStudyApplicationRepository.findAllByIdIn(ids).mapNotNull {
            daybreakStudyApplicationMapper.toDomain(it)
        }
    }

    override fun saveDaybreakStudyApplication(application: DaybreakStudyApplication) {
        daybreakStudyApplicationRepository.save(daybreakStudyApplicationMapper.toEntity(application))
    }

    override fun saveAllDaybreakStudyApplications(applications: List<DaybreakStudyApplication>) {
        val applicationEntities = applications.map { daybreakStudyApplicationMapper.toEntity(it) }

        daybreakStudyApplicationRepository.saveAll(applicationEntities)
    }

    private fun dateFilter(date: LocalDate) =
        daybreakStudyApplicationJpaEntity.createdAt.between(
            date.atStartOfDay(),
            date.atTime(LocalTime.MAX)
        )

    private fun typeFilter(typeId: UUID?) =
        typeId?.let { daybreakStudyApplicationJpaEntity.daybreakStudyTypeJpaEntity.id.eq(it) }

    private fun statusFilter(status: Status?): BooleanExpression {
        val validHeadTeacherStatuses = listOf(Status.SECOND_APPROVED, Status.REJECTED)
        val effectiveStatus = if (status in validHeadTeacherStatuses) status!! else Status.FIRST_APPROVED
        return daybreakStudyApplicationJpaEntity.status.eq(effectiveStatus)
    }
}
