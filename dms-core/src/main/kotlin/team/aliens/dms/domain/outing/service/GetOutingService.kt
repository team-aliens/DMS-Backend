package team.aliens.dms.domain.outing.service

import team.aliens.dms.domain.outing.model.OutingType

interface GetOutingService {

    fun getOutingType(outingType: OutingType): OutingType
}
