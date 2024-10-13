package team.aliens.dms.domain.outing.spi

import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.spi.vo.CurrentOutingApplicationVO
import team.aliens.dms.domain.outing.spi.vo.OutingApplicationVO
import java.time.LocalDate
import java.util.UUID

interface QueryOutingApplicationPort {

    fun existOutingApplicationByOutingDateAndStudentId(outingDate: LocalDate, studentId: UUID): Boolean

    fun queryOutingApplicationById(outingApplicationId: UUID): OutingApplication?

    fun queryAllOutingApplicationVOsBetweenStartAndEnd(start: LocalDate, end: LocalDate): List<OutingApplicationVO>

    fun queryCurrentOutingApplicationVO(studentId: UUID): CurrentOutingApplicationVO?

    fun isApplicant(studentId: UUID): Boolean
}
