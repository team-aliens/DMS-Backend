package team.aliens.dms.persistence.daybreak

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
                typeId?.let { daybreakStudyApplicationJpaEntity.daybreakStudyTypeJpaEntity.id.eq(it) }
            )
            .offset(pageData.offset)
            .limit(pageData.size)
            .orderBy(daybreakStudyApplicationJpaEntity.createdAt.desc())
            .fetch()
    }

    override fun headTeacherGetDaybreakStudyApplications(
        typeId: UUID?,
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
                typeId?.let { daybreakStudyApplicationJpaEntity.daybreakStudyTypeJpaEntity.id.eq(it) },

                if (status != null) {
                    // 2차 승인 또는 거절된 것만 확인할 수 있음
                    if (status in listOf(Status.SECOND_APPROVED, Status.REJECTED)) {
                        daybreakStudyApplicationJpaEntity.status.eq(status)
                    } else {
                        // 기본은 1차 승인만
                        daybreakStudyApplicationJpaEntity.status.eq(Status.FIRST_APPROVED)
                    }
                } else {
                    daybreakStudyApplicationJpaEntity.status.eq(Status.FIRST_APPROVED)
                }
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
}
