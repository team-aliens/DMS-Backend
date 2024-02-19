package team.aliens.dms.domain.outing.service

import org.springframework.stereotype.Service
import team.aliens.dms.domain.outing.exception.OutingTypeNotFoundException
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.spi.QueryOutingApplicationPort
import team.aliens.dms.domain.outing.spi.QueryOutingTypePort
import team.aliens.dms.domain.outing.spi.vo.OutingApplicationVO
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

    override fun getAllOutingApplicationVOsBetweenStartAndEnd(start: LocalDate, end: LocalDate) =
        queryOutingApplicationPort.queryAllOutingApplicationVOsBetweenStartAndEnd(start, end)
}
