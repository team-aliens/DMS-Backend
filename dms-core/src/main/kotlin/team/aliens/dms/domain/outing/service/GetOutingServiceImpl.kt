package team.aliens.dms.domain.outing.service

import org.springframework.stereotype.Service
import team.aliens.dms.domain.outing.exception.OutingApplicationNotFoundException
import team.aliens.dms.domain.outing.exception.OutingTypeNotFoundException
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.spi.QueryOutingApplicationPort
import team.aliens.dms.domain.outing.spi.QueryOutingTypePort
import team.aliens.dms.domain.outing.spi.vo.CurrentOutingApplicationVO
import java.time.LocalDate
import java.util.UUID

@Service
class GetOutingServiceImpl(
    private val queryOutingTypePort: QueryOutingTypePort,
    private val queryOutingApplicationPort: QueryOutingApplicationPort
) : GetOutingService {

    override fun getOutingType(outingType: OutingType) =
        queryOutingTypePort.queryOutingType(outingType) ?: throw OutingTypeNotFoundException

    override fun getAllOutingTypeTitlesBySchoolIdAndKeyword(schoolId: UUID, keyword: String?) =
        queryOutingTypePort.queryAllOutingTypeTitlesBySchoolIdAndKeyword(schoolId, keyword)

    override fun getOutingApplicationById(outingApplicationId: UUID): OutingApplication =
        queryOutingApplicationPort.queryOutingApplicationById(outingApplicationId)
            ?: throw OutingApplicationNotFoundException

    override fun getAllOutingApplicationVOsBetweenStartAndEnd(start: LocalDate, end: LocalDate) =
        queryOutingApplicationPort.queryAllOutingApplicationVOsBetweenStartAndEnd(start, end)

    override fun getCurrentOutingApplication(studentId: UUID): CurrentOutingApplicationVO =
        queryOutingApplicationPort.queryCurrentOutingApplicationVO(studentId)
            ?: throw OutingApplicationNotFoundException
}
