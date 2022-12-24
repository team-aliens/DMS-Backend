package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.StudyRoom

interface CommandStudyRoomPort {

    fun saveSeat(seat: Seat): Seat

    fun saveAllSeat(seats: List<Seat>)

    fun saveStudyRoom(studyRoom: StudyRoom): StudyRoom

}