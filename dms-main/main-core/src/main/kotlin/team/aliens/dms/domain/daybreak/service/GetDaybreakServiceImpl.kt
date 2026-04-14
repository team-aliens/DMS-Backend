package team.aliens.dms.domain.daybreak.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.daybreak.exception.DaybreakStudyTypeNotFoundException
import team.aliens.dms.domain.daybreak.model.DaybreakStudyType
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.spi.QueryDaybreakStudyApplicationPort
import team.aliens.dms.domain.daybreak.spi.QueryDaybreakStudyTypePort
import java.util.UUID

@Service
class GetDaybreakServiceImpl(
    private val queryDaybreakStudyTypePort: QueryDaybreakStudyTypePort,
    private val queryDaybreakStudyApplicationPort: QueryDaybreakStudyApplicationPort
) : GetDaybreakService {

    override fun getDaybreakStudyTypeById(id: UUID): DaybreakStudyType
    = queryDaybreakStudyTypePort.daybreakStudyTypeById(id) ?: throw DaybreakStudyTypeNotFoundException


    override fun generalTeacherGetDaybreakStudyApplications(
        typeId: UUID?,
        pageData: PageData
    ) = queryDaybreakStudyApplicationPort.generalTeacherGetDaybreakStudyApplications(
        typeId = typeId,
        pageData = pageData
    )

    override fun headTeacherGetDaybreakStudyApplications(
        typeId: UUID?,
        status: Status?,
        pageData: PageData
    ) = queryDaybreakStudyApplicationPort.headTeacherGetDaybreakStudyApplications(
        typeId = typeId,
        status = status,
        pageData = pageData
    )

    override fun managerGetDaybreakStudyApplications(
        grade: Int?,
        pageData: PageData
    ) = queryDaybreakStudyApplicationPort.managerGetDaybreakStudyApplications(
        grade = grade,
        pageData = pageData
    )

    override fun getDaybreakStudyTypesBySchoolId(schoolId: UUID)
    = queryDaybreakStudyTypePort.daybreakStudyTypesBySchoolId(schoolId)

}
