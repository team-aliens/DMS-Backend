package team.aliens.dms.domain.daybreak.spi

import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.spi.vo.DaybreakStudyApplicationStatusVO
import team.aliens.dms.domain.daybreak.spi.vo.DaybreakStudyApplicationVO
import java.util.UUID

interface QueryDaybreakStudyApplicationPort {

    fun existActiveDaybreakStudyApplicationByStudentId(studentId: UUID): Boolean

    fun generalTeacherGetDaybreakStudyApplications(
        teacherId: UUID,
        typeId: UUID? = null,
        pageData: PageData = PageData.DEFAULT
    ): List<DaybreakStudyApplicationVO>

    fun headTeacherGetDaybreakStudyApplications(
        grade: Int,
        typeId: UUID? = null,
        status: Status,
        pageData: PageData = PageData.DEFAULT
    ): List<DaybreakStudyApplicationVO>

    fun managerGetDaybreakStudyApplications(
        schoolId: UUID,
        grade: Int? = null,
        status: Status,
        pageData: PageData = PageData.DEFAULT
    ): List<DaybreakStudyApplicationVO>

    fun exportManagerDaybreakStudyApplications(
        schoolId: UUID,
        grade: Int? = null,
        status: Status
    ): List<DaybreakStudyApplicationVO>

    fun getAllByIdIn(ids: List<UUID>): List<DaybreakStudyApplication>

    fun getRecentDaybreakStudyApplicationStatusByStudentId(studentId: UUID): DaybreakStudyApplicationStatusVO?

    fun findExpiredDaybreakStudyApplications(): List<DaybreakStudyApplication>
}
