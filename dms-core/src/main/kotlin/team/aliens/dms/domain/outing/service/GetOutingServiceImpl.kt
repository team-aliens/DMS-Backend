package team.aliens.dms.domain.outing.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.outing.exception.OutingApplicationNotFoundException
import team.aliens.dms.domain.outing.exception.OutingAvailableTimeNotFoundException
import team.aliens.dms.domain.outing.exception.OutingTypeNotFoundException
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingAvailableTime
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.spi.QueryOutingApplicationPort
import team.aliens.dms.domain.outing.spi.QueryOutingAvailableTimePort
import team.aliens.dms.domain.outing.spi.QueryOutingCompanionPort
import team.aliens.dms.domain.outing.spi.QueryOutingTypePort
import team.aliens.dms.domain.outing.spi.vo.CurrentOutingApplicationVO
import team.aliens.dms.domain.outing.spi.vo.OutingCompanionDetailsVO
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.UUID

@Service
class GetOutingServiceImpl(
    private val queryOutingTypePort: QueryOutingTypePort,
    private val queryOutingApplicationPort: QueryOutingApplicationPort,
    private val queryOutingAvailableTimePort: QueryOutingAvailableTimePort,
    private val queryOutingCompanionPort: QueryOutingCompanionPort
) : GetOutingService {

    override fun getOutingType(outingType: OutingType) =
        queryOutingTypePort.queryOutingType(outingType) ?: throw OutingTypeNotFoundException

    override fun getAllOutingTypeTitlesBySchoolIdAndKeyword(schoolId: UUID, keyword: String?) =
        queryOutingTypePort.queryAllOutingTypeTitlesBySchoolIdAndKeyword(schoolId, keyword)

    override fun getOutingApplicationById(outingApplicationId: UUID): OutingApplication =
        queryOutingApplicationPort.queryOutingApplicationById(outingApplicationId)
            ?: throw OutingApplicationNotFoundException

    override fun getAllOutingApplicationVOsBetweenStartAndEnd(name: String?, start: LocalDate, end: LocalDate) =
        queryOutingApplicationPort.queryAllOutingApplicationVOsBetweenStartAndEnd(name, start, end)

    override fun getCurrentOutingApplication(studentId: UUID): CurrentOutingApplicationVO {
        return queryOutingApplicationPort.queryCurrentOutingApplicationVO(studentId)
            ?: throw OutingApplicationNotFoundException
    }

    override fun getOutingAvailableTimesByDayOfWeek(
        dayOfWeek: DayOfWeek
    ) = queryOutingAvailableTimePort.queryOutingAvailableTimesByDayOfWeek(
        dayOfWeek = dayOfWeek
    )

    override fun getOutingCompanionsByApplicationId(outingApplicationId: UUID): List<OutingCompanionDetailsVO> =
        queryOutingCompanionPort.queryOutingCompanionsById(outingApplicationId)

    override fun getOutingAvailableTimeById(outingAvailableTimeId: UUID): OutingAvailableTime =
        queryOutingAvailableTimePort.queryOutingAvailableTimeById(outingAvailableTimeId)
            ?: throw OutingAvailableTimeNotFoundException
}
