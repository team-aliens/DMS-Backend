package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.StudyRoomTimeSlot
import java.util.UUID

interface CommandStudyRoomPort {

    fun saveSeat(seat: Seat): Seat

    fun saveAllSeats(seats: List<Seat>): List<Seat>

    fun saveTimeSlot(timeSlot: StudyRoomTimeSlot): StudyRoomTimeSlot

    fun saveSeatApplication(seatApplication: SeatApplication): SeatApplication

    fun saveStudyRoom(studyRoom: StudyRoom): StudyRoom

    fun deleteStudyRoomById(studyRoomId: UUID)

    fun deleteTimeSlotById(studyRoomTimeSlotId: UUID)

    fun deleteSeatApplicationByStudyRoomId(studyRoomId: UUID)

    fun deleteSeatByStudyRoomId(studyRoomId: UUID)

    fun deleteAllSeatApplications()

    fun deleteSeatApplications(seatApplicationIds: List<UUID>)

    fun deleteSeatApplicationByStudentIdAndTimeSlotId(studentId: UUID, timeSlotId: UUID?)

    fun deleteSeatApplicationByTimeSlotId(timeSlotId: UUID)
}
