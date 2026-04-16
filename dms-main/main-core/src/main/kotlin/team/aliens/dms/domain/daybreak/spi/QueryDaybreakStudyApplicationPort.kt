package team.aliens.dms.domain.daybreak.spi

import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.spi.vo.GeneralTeacherDaybreakStudyApplicationVO
import team.aliens.dms.domain.daybreak.spi.vo.HeadTeacherDaybreakStudyApplicationVO
import team.aliens.dms.domain.daybreak.spi.vo.ManagerDaybreakStudyApplicationVO
import java.time.LocalDate
import java.util.UUID

interface QueryDaybreakStudyApplicationPort {

    fun existActiveDaybreakStudyApplicationByStudentId(studentId: UUID): Boolean

    fun generalTeacherGetDaybreakStudyApplications(
        teacherId: UUID,
        typeId: UUID? = null,
        date: LocalDate,
        pageData: PageData = PageData.DEFAULT
    ): List<GeneralTeacherDaybreakStudyApplicationVO>

    fun headTeacherGetDaybreakStudyApplications(
        typeId: UUID? = null,
        date: LocalDate,
        status: Status? = null,
        pageData: PageData = PageData.DEFAULT
    ): List<HeadTeacherDaybreakStudyApplicationVO>

    fun managerGetDaybreakStudyApplications(
        grade: Int? = null,
        pageData: PageData = PageData.DEFAULT
    ): List<ManagerDaybreakStudyApplicationVO>

    fun getAllByIdIn(ids: List<UUID>): List<DaybreakStudyApplication>
}
