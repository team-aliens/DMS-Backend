package team.aliens.dms.domain.daybreak.service

import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.DaybreakStudyType
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.spi.vo.GeneralTeacherDaybreakStudyApplicationVO
import team.aliens.dms.domain.daybreak.spi.vo.HeadTeacherDaybreakStudyApplicationVO
import team.aliens.dms.domain.daybreak.spi.vo.ManagerDaybreakStudyApplicationVO
import java.util.UUID

interface GetDaybreakService {

    fun getDaybreakStudyTypeById(id: UUID): DaybreakStudyType

    fun generalTeacherGetDaybreakStudyApplications(
        typeId: UUID? = null,
        pageData: PageData = PageData.DEFAULT
    ): List<GeneralTeacherDaybreakStudyApplicationVO>

    fun headTeacherGetDaybreakStudyApplications(
        typeId: UUID? = null,
        status: Status? = null,
        pageData: PageData = PageData.DEFAULT
    ): List<HeadTeacherDaybreakStudyApplicationVO>

    fun managerGetDaybreakStudyApplications(
        grade: Int? = null,
        pageData: PageData = PageData.DEFAULT
    ): List<ManagerDaybreakStudyApplicationVO>

    fun getDaybreakStudyTypesBySchoolId(schoolId: UUID): List<DaybreakStudyType>

    fun getAllByIdIn(ids: List<UUID>): List<DaybreakStudyApplication>
}
