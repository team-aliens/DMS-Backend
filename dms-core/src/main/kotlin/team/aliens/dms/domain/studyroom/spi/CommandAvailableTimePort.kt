package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.studyroom.model.AvailableTime

interface CommandAvailableTimePort {

    fun saveAvailableTime(availableTime: AvailableTime): AvailableTime
}
