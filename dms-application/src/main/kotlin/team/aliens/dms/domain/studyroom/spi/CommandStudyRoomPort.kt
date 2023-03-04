package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.StudyRoom
import java.util.UUID

interface CommandStudyRoomPort {

    fun saveSeat(seat: Seat): Seat

    fun saveAllSeat(seats: List<Seat>)

    fun deleteAllSeatsByStudyRoomId(studyRoomId: UUID)

    fun saveStudyRoom(studyRoom: StudyRoom): StudyRoom

    fun saveAllStudyRooms(studyRooms: List<StudyRoom>)

    fun deleteStudyRoomById(studyRoomId: UUID)
}
