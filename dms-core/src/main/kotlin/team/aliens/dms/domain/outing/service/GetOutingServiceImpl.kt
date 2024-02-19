package team.aliens.dms.domain.outing.service

import org.springframework.stereotype.Service
import team.aliens.dms.domain.outing.exception.OutingApplicationNotFoundException
import team.aliens.dms.domain.outing.exception.OutingTypeNotFoundException
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.spi.QueryOutingApplicationPort
import team.aliens.dms.domain.outing.spi.QueryOutingTypePort
import java.util.UUID

@Service
class GetOutingServiceImpl(

    private val queryOutingApplicationPort: QueryOutingApplicationPort,
    private val queryOutingTypePort: QueryOutingTypePort
) : GetOutingService {

    override fun getOutingType(outingType: OutingType): OutingType =
        queryOutingTypePort.queryOutingType(outingType) ?: throw OutingTypeNotFoundException

    override fun getOutingApplicationById(outingApplicationId: UUID): OutingApplication =
        queryOutingApplicationPort.queryOutingApplicationById(outingApplicationId) ?: throw OutingApplicationNotFoundException
}
