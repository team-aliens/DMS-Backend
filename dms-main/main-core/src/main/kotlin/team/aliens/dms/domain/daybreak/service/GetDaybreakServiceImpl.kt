package team.aliens.dms.domain.daybreak.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.daybreak.exception.DaybreakStudyApplicationNotFoundException
import team.aliens.dms.domain.daybreak.exception.DaybreakStudyTypeNotFoundException
import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.DaybreakStudyType
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.spi.QueryDaybreakStudyApplicationPort
import team.aliens.dms.domain.daybreak.spi.QueryDaybreakStudyTypePort
import team.aliens.dms.domain.daybreak.spi.vo.DaybreakStudyApplicationVO
import java.time.LocalDate
import java.util.UUID

@Service
class GetDaybreakServiceImpl(
    private val queryDaybreakStudyTypePort: QueryDaybreakStudyTypePort,
    private val queryDaybreakStudyApplicationPort: QueryDaybreakStudyApplicationPort
) : GetDaybreakService {

    override fun getDaybreakStudyTypeById(id: UUID): DaybreakStudyType =
        queryDaybreakStudyTypePort.daybreakStudyTypeById(id) ?: throw DaybreakStudyTypeNotFoundException

    override fun generalTeacherGetDaybreakStudyApplications(
        teacherId: UUID,
        typeId: UUID?,
        date: LocalDate,
        pageData: PageData
    ) = queryDaybreakStudyApplicationPort.generalTeacherGetDaybreakStudyApplications(
        teacherId = teacherId,
        typeId = typeId,
        date = date,
        pageData = pageData
    )

    override fun headTeacherGetDaybreakStudyApplications(
        grade: Int,
        typeId: UUID?,
        date: LocalDate,
        status: Status?,
        pageData: PageData
    ): List<DaybreakStudyApplicationVO> {
        val validHeadTeacherStatuses = listOf(Status.SECOND_APPROVED, Status.REJECTED)

        val effectiveStatus = if (status in validHeadTeacherStatuses) status!! else Status.FIRST_APPROVED

        return queryDaybreakStudyApplicationPort.headTeacherGetDaybreakStudyApplications(
            grade = grade,
            typeId = typeId,
            status = effectiveStatus,
            date = date,
            pageData = pageData
        )
    }

    override fun managerGetDaybreakStudyApplications(
        grade: Int?,
        pageData: PageData
    ): List<DaybreakStudyApplicationVO> {

        val status = Status.SECOND_APPROVED

        return queryDaybreakStudyApplicationPort.managerGetDaybreakStudyApplications(
            grade = grade,
            status = status,
            pageData = pageData
        )
    }

    override fun getDaybreakStudyTypesBySchoolId(schoolId: UUID) =
        queryDaybreakStudyTypePort.daybreakStudyTypesBySchoolId(schoolId)

    override fun getAllByIdIn(ids: List<UUID>): List<DaybreakStudyApplication> {
        return queryDaybreakStudyApplicationPort.getAllByIdIn(ids)
            .apply { if (size != ids.size) throw DaybreakStudyApplicationNotFoundException }
    }

    override fun getRecentDaybreakStudyApplicationStatusByStudentId(studentId: UUID) =
        queryDaybreakStudyApplicationPort.getRecentDaybreakStudyApplicationStatusByStudentId(studentId) ?: throw DaybreakStudyApplicationNotFoundException

}
