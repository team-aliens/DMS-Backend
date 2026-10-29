package team.aliens.dms.persistence.daybreak

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.spi.DaybreakStudyApplicationPort
import team.aliens.dms.domain.daybreak.spi.vo.DaybreakStudyApplicationStatusVO
import team.aliens.dms.domain.daybreak.spi.vo.DaybreakStudyApplicationVO
import team.aliens.dms.persistence.daybreak.entity.QDaybreakStudyApplicationJpaEntity.daybreakStudyApplicationJpaEntity
import team.aliens.dms.persistence.daybreak.mapper.DaybreakStudyApplicationMapper
import team.aliens.dms.persistence.daybreak.repository.DaybreakStudyApplicationJpaRepository
import team.aliens.dms.persistence.daybreak.repository.vo.QQueryDaybreakStudyApplicationStatusVO
import team.aliens.dms.persistence.daybreak.repository.vo.QQueryDaybreakStudyApplicationVO
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import java.time.LocalDate
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
        pageData: PageData
    ): List<DaybreakStudyApplicationVO> = findDaybreakStudyApplications(
        daybreakStudyApplicationJpaEntity.teacherJpaEntity.id.eq(teacherId),
        daybreakStudyApplicationJpaEntity.status.ne(Status.EXPIRED),
        typeFilter(typeId),
        orders = arrayOf(statusOrder(), *gcnOrder()),
        pageData = pageData
    )

    override fun headTeacherGetDaybreakStudyApplications(
        schoolId: UUID,
        grade: Int,
        typeId: UUID?,
        status: Status,
        pageData: PageData
    ): List<DaybreakStudyApplicationVO> = findDaybreakStudyApplications(
        daybreakStudyApplicationJpaEntity.schoolJpaEntity.id.eq(schoolId),
        daybreakStudyApplicationJpaEntity.status.eq(status),
        daybreakStudyApplicationJpaEntity.studentJpaEntity.grade.eq(grade),
        typeFilter(typeId),
        orders = gcnOrder(),
        pageData = pageData
    )

    override fun managerGetDaybreakStudyApplications(
        schoolId: UUID,
        grade: Int?,
        status: Status,
        pageData: PageData
    ): List<DaybreakStudyApplicationVO> = findDaybreakStudyApplications(
        *managerConditions(schoolId, grade, status),
        orders = gcnOrder(),
        pageData = pageData
    )

    override fun exportManagerDaybreakStudyApplications(
        schoolId: UUID,
        grade: Int?,
        status: Status
    ): List<DaybreakStudyApplicationVO> = findDaybreakStudyApplications(
        *managerConditions(schoolId, grade, status),
        orders = gcnOrder()
    )

    override fun getAllByIdIn(ids: List<UUID>): List<DaybreakStudyApplication> {
        return daybreakStudyApplicationRepository.findAllByIdIn(ids).mapNotNull {
            daybreakStudyApplicationMapper.toDomain(it)
        }
    }

    override fun getRecentDaybreakStudyApplicationStatusByStudentId(studentId: UUID): DaybreakStudyApplicationStatusVO? {
        return queryFactory
            .select(
                QQueryDaybreakStudyApplicationStatusVO(
                    daybreakStudyApplicationJpaEntity.status,
                    daybreakStudyApplicationJpaEntity.studyDates,
                    daybreakStudyApplicationJpaEntity.daybreakStudyTypeJpaEntity.name,
                    daybreakStudyApplicationJpaEntity.reason,
                    daybreakStudyApplicationJpaEntity.teacherJpaEntity.name
                )
            )
            .from(daybreakStudyApplicationJpaEntity)
            .where(daybreakStudyApplicationJpaEntity.studentJpaEntity.id.eq(studentId))
            .orderBy(daybreakStudyApplicationJpaEntity.createdAt.desc())
            .fetchFirst()
    }

    override fun findExpiredDaybreakStudyApplications(): List<DaybreakStudyApplication> {
        return queryFactory
            .selectFrom(daybreakStudyApplicationJpaEntity)
            .where(
                daybreakStudyApplicationJpaEntity.endDate.lt(LocalDate.now()),
                daybreakStudyApplicationJpaEntity.status.eq(Status.SECOND_APPROVED)
            )
            .fetch()
            .mapNotNull { daybreakStudyApplicationMapper.toDomain(it) }
    }

    // status가 EXPIRED, SECOND_APPROVED인 것만 조회
    override fun getStudentDaybreakStudyApplicationHistoryByStudentId(
        studentId: UUID,
        pageData: PageData
    ): List<DaybreakStudyApplicationVO> = findDaybreakStudyApplications(
        daybreakStudyApplicationJpaEntity.studentJpaEntity.id.eq(studentId),
        daybreakStudyApplicationJpaEntity.status.`in`(Status.EXPIRED, Status.SECOND_APPROVED),
        orders = arrayOf(daybreakStudyApplicationJpaEntity.createdAt.desc()),
        pageData = pageData
    )

    override fun deleteOutdatedDaybreakStudyApplications() {
        queryFactory
            .delete(daybreakStudyApplicationJpaEntity)
            .where(
                daybreakStudyApplicationJpaEntity.endDate.lt(LocalDate.now()),
                daybreakStudyApplicationJpaEntity.status.`in`(
                    Status.PENDING,
                    Status.FIRST_APPROVED,
                    Status.REJECTED
                )
            )
            .execute()
    }

    override fun deleteDaybreakStudyApplication(studentId: UUID): Boolean {
        val deletedCount = queryFactory
            .delete(daybreakStudyApplicationJpaEntity)
            .where(
                daybreakStudyApplicationJpaEntity.studentJpaEntity.id.eq(studentId),
                daybreakStudyApplicationJpaEntity.status.eq(Status.PENDING)
            )
            .execute()

        return deletedCount > 0
    }

    override fun saveDaybreakStudyApplication(application: DaybreakStudyApplication) {
        daybreakStudyApplicationRepository.save(daybreakStudyApplicationMapper.toEntity(application))
    }

    override fun saveAllDaybreakStudyApplications(applications: List<DaybreakStudyApplication>) {
        val applicationEntities = applications.map { daybreakStudyApplicationMapper.toEntity(it) }

        daybreakStudyApplicationRepository.saveAll(applicationEntities)
    }

    // 신청 목록 API(담당·부장·사감 선생님, 학생 이력)는 응답 필드가 같아야 하므로 조회 본문을 하나로 둔다.
    // 학교·선생님·학생 같은 범위 조건은 빠뜨리지 않도록 각 포트 메서드가 시그니처로 받아 넘긴다. pageData가 없으면 전체 조회
    private fun findDaybreakStudyApplications(
        vararg conditions: Predicate?,
        orders: Array<out OrderSpecifier<*>>,
        pageData: PageData? = null
    ): List<DaybreakStudyApplicationVO> =
        queryFactory
            .select(
                QQueryDaybreakStudyApplicationVO(
                    daybreakStudyApplicationJpaEntity.id,
                    daybreakStudyApplicationJpaEntity.daybreakStudyTypeJpaEntity.name,
                    daybreakStudyApplicationJpaEntity.createdAt,
                    daybreakStudyApplicationJpaEntity.studyDates,
                    daybreakStudyApplicationJpaEntity.reason,
                    studentJpaEntity.id,
                    studentJpaEntity.name,
                    studentJpaEntity.grade,
                    studentJpaEntity.classRoom,
                    studentJpaEntity.number,
                    daybreakStudyApplicationJpaEntity.teacherJpaEntity.name,
                    daybreakStudyApplicationJpaEntity.status
                )
            )
            .from(daybreakStudyApplicationJpaEntity)
            .join(studentJpaEntity).on(daybreakStudyApplicationJpaEntity.studentJpaEntity.id.eq(studentJpaEntity.id))
            .where(*conditions)
            .orderBy(*orders)
            .apply { pageData?.let { offset(it.offset).limit(it.size) } }
            .fetch()

    // 사감 선생님 명단은 오늘 자습하는 학생만 보여준다(화면·엑셀 공통)
    private fun managerConditions(schoolId: UUID, grade: Int?, status: Status) = arrayOf(
        daybreakStudyApplicationJpaEntity.schoolJpaEntity.id.eq(schoolId),
        daybreakStudyApplicationJpaEntity.status.eq(status),
        gradeFilter(grade),
        studyOn(LocalDate.now()),
    )

    // PENDING -> REJECTED -> FIRST_APPROVED -> SECOND_APPROVED 순으로 정렬
    private fun statusOrder() = CaseBuilder()
        .`when`(daybreakStudyApplicationJpaEntity.status.eq(Status.PENDING)).then(0)
        .`when`(daybreakStudyApplicationJpaEntity.status.eq(Status.REJECTED)).then(1)
        .`when`(daybreakStudyApplicationJpaEntity.status.eq(Status.FIRST_APPROVED)).then(2)
        .`when`(daybreakStudyApplicationJpaEntity.status.eq(Status.SECOND_APPROVED)).then(3)
        .otherwise(4)
        .asc()

    private fun gcnOrder() = arrayOf(
        studentJpaEntity.grade.asc(),
        studentJpaEntity.classRoom.asc(),
        studentJpaEntity.number.asc()
    )

    private fun typeFilter(typeId: UUID?) =
        typeId?.let { daybreakStudyApplicationJpaEntity.daybreakStudyTypeJpaEntity.id.eq(it) }

    // 구간(startDate~endDate) 안이어도 쉬는 날일 수 있으므로 날짜 목록에 오늘이 들어있는지로 판단한다
    private fun studyOn(date: LocalDate) =
        Expressions.booleanTemplate("array_contains({0}, {1})", daybreakStudyApplicationJpaEntity.studyDates, date)

    private fun gradeFilter(grade: Int?) =
        grade?.let { daybreakStudyApplicationJpaEntity.studentJpaEntity.grade.eq(it) }
}
