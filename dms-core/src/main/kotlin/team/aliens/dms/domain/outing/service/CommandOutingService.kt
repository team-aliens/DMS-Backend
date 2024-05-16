package team.aliens.dms.domain.outing.service

import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingAvailableTime
import team.aliens.dms.domain.outing.model.OutingType

interface CommandOutingService {

    fun saveOutingApplication(outingApplication: OutingApplication): OutingApplication

    fun saveOutingType(outingType: OutingType): OutingType

    fun deleteOutingType(outingType: OutingType)

    fun deleteOutingApplication(outingApplication: OutingApplication)

    fun saveOutingTime(outingAvailableTime: OutingAvailableTime)
}
