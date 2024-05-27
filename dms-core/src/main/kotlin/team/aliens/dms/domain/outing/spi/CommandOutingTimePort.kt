package team.aliens.dms.domain.outing.spi

import team.aliens.dms.domain.outing.model.OutingAvailableTime

interface CommandOutingTimePort {

    fun saveOutingTime(outingAvailableTime: OutingAvailableTime): OutingAvailableTime
}
