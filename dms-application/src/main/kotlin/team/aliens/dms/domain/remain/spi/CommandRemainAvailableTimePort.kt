package team.aliens.dms.domain.remain.spi

import team.aliens.dms.domain.remain.model.RemainAvailableTime

interface CommandRemainAvailableTimePort {

    fun saveRemainAvailableTime(remainAvailableTime: RemainAvailableTime): RemainAvailableTime

}