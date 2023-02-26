package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.StudyRoom

interface StudentCommandStudyRoomPort {

    fun saveSeat(seat: Seat): Seat

    fun saveStudyRoom(studyRoom: StudyRoom): StudyRoom
}
