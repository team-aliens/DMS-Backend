package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.studyroom.model.StudyRoomAvailableTime

interface CommandAvailableTimePort {

    fun saveAvailableTime(availableTime: StudyRoomAvailableTime): StudyRoomAvailableTime

}