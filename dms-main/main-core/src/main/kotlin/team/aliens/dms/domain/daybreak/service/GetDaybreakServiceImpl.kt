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
    ) = queryDaybreakStudyApplicationPort.headTeacherGetDaybreakStudyApplications(
        grade = grade,
        typeId = typeId,
        status = status,
        date = date,
        pageData = pageData
    )

    override fun managerGetDaybreakStudyApplications(
        grade: Int?,
        pageData: PageData
    ) = queryDaybreakStudyApplicationPort.managerGetDaybreakStudyApplications(
        grade = grade,
        pageData = pageData
    )

    override fun getDaybreakStudyTypesBySchoolId(schoolId: UUID) =
        queryDaybreakStudyTypePort.daybreakStudyTypesBySchoolId(schoolId)

    override fun getAllByIdIn(ids: List<UUID>): List<DaybreakStudyApplication> {
        return queryDaybreakStudyApplicationPort.getAllByIdIn(ids)
            .apply { if (size != ids.size) throw DaybreakStudyApplicationNotFoundException }
    }
}
