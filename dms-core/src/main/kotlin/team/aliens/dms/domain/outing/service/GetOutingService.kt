package team.aliens.dms.domain.outing.service

import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.spi.vo.CurrentOutingApplicationVO
import team.aliens.dms.domain.outing.spi.vo.OutingApplicationVO
import team.aliens.dms.domain.outing.spi.vo.OutingAvailableTimeVO
import team.aliens.dms.domain.outing.spi.vo.OutingCompanionDetailsVO
import team.aliens.dms.domain.outing.spi.vo.OutingHistoryVO
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.UUID

interface GetOutingService {

    fun getOutingType(outingType: OutingType): OutingType

    fun getAllOutingTypeTitlesBySchoolIdAndKeyword(schoolId: UUID, keyword: String?): List<String>

    fun getOutingApplicationById(outingApplicationId: UUID): OutingApplication

    fun getAllOutingApplicationVOsBetweenStartAndEnd(start: LocalDate, end: LocalDate): List<OutingApplicationVO>

    fun getCurrentOutingApplication(studentId: UUID): CurrentOutingApplicationVO

    fun getOutingHistoriesByStudentNameAndDate(studentName: String?, date: LocalDate): List<OutingHistoryVO>

    fun getOutingAvailableTimesByDayOfWeek(dayOfWeek: DayOfWeek): List<OutingAvailableTimeVO>

    fun getOutingCompanionsById(outingApplicationId: UUID): List<OutingCompanionDetailsVO>
}
