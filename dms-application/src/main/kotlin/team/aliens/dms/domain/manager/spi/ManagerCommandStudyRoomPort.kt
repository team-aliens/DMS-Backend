package team.aliens.dms.domain.manager.spi

import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.StudyRoom

interface ManagerCommandStudyRoomPort {

    fun saveSeat(seat: Seat): Seat

    fun saveStudyRoom(studyRoom: StudyRoom): StudyRoom
}