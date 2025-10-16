package team.aliens.dms.domain.outing.spi

import team.aliens.dms.domain.outing.model.OutingAvailableTime

interface CommandOutingTimePort {

    fun saveOutingAvailableTime(outingAvailableTime: OutingAvailableTime): OutingAvailableTime

    fun deleteOutingAvailableTime(outingAvailableTime: OutingAvailableTime)
}
