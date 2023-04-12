package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.StudyRoomTimeSlot
import team.aliens.dms.domain.studyroom.model.TimeSlot
import java.util.UUID

interface CommandStudyRoomPort {

    fun saveAllSeats(seats: List<Seat>): List<Seat>

    fun saveTimeSlot(timeSlot: TimeSlot): TimeSlot

    fun saveSeatApplication(seatApplication: SeatApplication): SeatApplication

    fun saveStudyRoom(studyRoom: StudyRoom): StudyRoom

    fun saveAllStudyRoomTimeSlots(studyRoomTimeSlots: List<StudyRoomTimeSlot>): List<StudyRoomTimeSlot>

    fun deleteStudyRoomById(studyRoomId: UUID)

    fun deleteTimeSlotById(timeSlotId: UUID)

    fun deleteSeatApplicationByStudyRoomId(studyRoomId: UUID)

    fun deleteSeatByStudyRoomId(studyRoomId: UUID)

    fun deleteAllSeatApplications()

    fun deleteSeatApplicationBySeatIdAndStudentIdAndTimeSlotId(seatId: UUID, studentId: UUID, timeSlotId: UUID)

    fun deleteSeatApplicationByTimeSlotId(timeSlotId: UUID)

    fun deleteStudyRoomTimeSlotByStudyRoomId(studyRoomId: UUID)
}
