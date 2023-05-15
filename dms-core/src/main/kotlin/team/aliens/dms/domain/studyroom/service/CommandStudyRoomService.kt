package team.aliens.dms.domain.studyroom.service

import java.util.UUID
import java.util.function.Function
import team.aliens.dms.domain.studyroom.model.AvailableTime
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.SeatType
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.StudyRoomTimeSlot
import team.aliens.dms.domain.studyroom.model.TimeSlot

interface CommandStudyRoomService {

    fun createStudyRoom(studyRoom: StudyRoom): StudyRoom

    fun updateStudyRoom(studyRoomId: UUID, updateFunction: Function<StudyRoom, StudyRoom>)

    fun createSeatType(seatType: SeatType): SeatType

    fun createTimeSlot(timeSlot: TimeSlot): TimeSlot

    fun updateTimeSlot(timeSlot: TimeSlot): TimeSlot

    fun createSeatApplication(seatApplication: SeatApplication): SeatApplication

    fun createAllStudyRoomTimeSlots(studyRoomTimeSlots: List<StudyRoomTimeSlot>): List<StudyRoomTimeSlot>

    fun createAllSeats(seats: List<Seat>): List<Seat>

    fun createAvailableTime(availableTime: AvailableTime): AvailableTime

    fun deleteStudyRoom(studyRoomId: UUID)

    fun deleteTimeSlot(timeSlotId: UUID)

    fun deleteSeatApplication(studentId: UUID, seatId: UUID, timeSlotId: UUID)

    fun deleteSeatType(seatTypeId: UUID, schoolId: UUID)

    fun deleteSeatByStudyRoomId(studyRoomId: UUID)

    fun deleteAllSeatApplications()

    fun deleteSeatApplicationBySeatIdAndStudentIdAndTimeSlotId(seatId: UUID, id: UUID, timeSlotId: UUID)

    fun deleteStudyRoomTimeSlotByStudyRoomId(studyRoomId: UUID)

    fun updateTimeSlotsByStudyRoom(studyRoomId: UUID, studyRoomTimeSlots: List<StudyRoomTimeSlot>)

    fun updateSeatsByStudyRoom(studyRoomId: UUID, seats: List<Seat>)
}
