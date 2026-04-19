package team.aliens.dms.persistence.daybreak

import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.spi.DaybreakStudyApplicationPort
import team.aliens.dms.domain.daybreak.spi.vo.DaybreakStudyApplicationVO
import team.aliens.dms.persistence.daybreak.entity.QDaybreakStudyApplicationJpaEntity.daybreakStudyApplicationJpaEntity
import team.aliens.dms.persistence.daybreak.mapper.DaybreakStudyApplicationMapper
import team.aliens.dms.persistence.daybreak.repository.DaybreakStudyApplicationJpaRepository
import team.aliens.dms.persistence.daybreak.repository.vo.QQueryDaybreakStudyApplicationVO
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
                daybreakStudyApplicationJpaEntity.studentJpaEntity.id.eq(studentId),
                daybreakStudyApplicationJpaEntity.status.notIn(Status.EXPIRED, Status.REJECTED)
            )
            .fetchFirst()

        return fetchOne != null
    }

    override fun generalTeacherGetDaybreakStudyApplications(
        teacherId: UUID,
        typeId: UUID?,
        date: LocalDate,
        pageData: PageData
    ): List<DaybreakStudyApplicationVO> {
        return queryFactory
            .select(
                QQueryDaybreakStudyApplicationVO(
                    daybreakStudyApplicationJpaEntity.id,
                    daybreakStudyApplicationJpaEntity.daybreakStudyTypeJpaEntity.name,
                    daybreakStudyApplicationJpaEntity.createdAt,
                    daybreakStudyApplicationJpaEntity.startDate,
                    daybreakStudyApplicationJpaEntity.endDate,
                    daybreakStudyApplicationJpaEntity.reason,
                    studentJpaEntity.name,
                    studentJpaEntity.grade,
                    studentJpaEntity.classRoom,
                    studentJpaEntity.number,
                    Expressions.nullExpression(),
                    daybreakStudyApplicationJpaEntity.status
                )
            )
            .from(daybreakStudyApplicationJpaEntity)
            .join(studentJpaEntity).on(daybreakStudyApplicationJpaEntity.studentJpaEntity.id.eq(studentJpaEntity.id))
            .where(
                dateFilter(date),
                typeFilter(typeId),
                daybreakStudyApplicationJpaEntity.teacherJpaEntity.id.eq(teacherId),
            )
            .offset(pageData.offset)
            .limit(pageData.size)
            .orderBy(daybreakStudyApplicationJpaEntity.createdAt.desc())
            .fetch()
    }

    override fun headTeacherGetDaybreakStudyApplications(
        grade: Int,
        typeId: UUID?,
        date: LocalDate,
        status: Status?,
        pageData: PageData
    ): List<DaybreakStudyApplicationVO> {
        return queryFactory
            .select(
                QQueryDaybreakStudyApplicationVO(
                    daybreakStudyApplicationJpaEntity.id,
                    daybreakStudyApplicationJpaEntity.daybreakStudyTypeJpaEntity.name,
                    daybreakStudyApplicationJpaEntity.createdAt,
                    daybreakStudyApplicationJpaEntity.startDate,
                    daybreakStudyApplicationJpaEntity.endDate,
                    daybreakStudyApplicationJpaEntity.reason,
                    studentJpaEntity.name,
                    studentJpaEntity.grade,
                    studentJpaEntity.classRoom,
                    studentJpaEntity.number,
                    daybreakStudyApplicationJpaEntity.teacherJpaEntity.name,
                    Expressions.nullExpression()
                )
            )
            .from(daybreakStudyApplicationJpaEntity)
            .join(studentJpaEntity).on(daybreakStudyApplicationJpaEntity.studentJpaEntity.id.eq(studentJpaEntity.id))
            .where(
                dateFilter(date),
                typeFilter(typeId),
                daybreakStudyApplicationJpaEntity.status.eq(status),
                daybreakStudyApplicationJpaEntity.studentJpaEntity.grade.eq(grade)
            )
            .offset(pageData.offset)
            .limit(pageData.size)
            .orderBy(daybreakStudyApplicationJpaEntity.createdAt.desc())
            .fetch()
    }

    override fun managerGetDaybreakStudyApplications(
        grade: Int?,
        status: Status,
        pageData: PageData
    ): List<DaybreakStudyApplicationVO> {
        return queryFactory
            .select(
                QQueryDaybreakStudyApplicationVO(
                    daybreakStudyApplicationJpaEntity.id,
                    daybreakStudyApplicationJpaEntity.daybreakStudyTypeJpaEntity.name,
                    daybreakStudyApplicationJpaEntity.createdAt,
                    daybreakStudyApplicationJpaEntity.startDate,
                    daybreakStudyApplicationJpaEntity.endDate,
                    daybreakStudyApplicationJpaEntity.reason,
                    studentJpaEntity.name,
                    studentJpaEntity.grade,
                    studentJpaEntity.classRoom,
                    studentJpaEntity.number,
                    daybreakStudyApplicationJpaEntity.teacherJpaEntity.name,
                    Expressions.nullExpression()
                )
            )
            .from(daybreakStudyApplicationJpaEntity)
            .join(studentJpaEntity).on(daybreakStudyApplicationJpaEntity.studentJpaEntity.id.eq(studentJpaEntity.id))
            .where(
                daybreakStudyApplicationJpaEntity.status.eq(status),
                gradeFilter(grade),
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

    private fun gradeFilter(grade: Int?) =
        grade?.let { daybreakStudyApplicationJpaEntity.studentJpaEntity.grade.eq(it) }
}
