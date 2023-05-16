package team.aliens.dms.domain.studyroom.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.studyroom.exception.SeatAlreadyAppliedException
import team.aliens.dms.domain.studyroom.exception.SeatTypeAlreadyExistsException
import team.aliens.dms.domain.studyroom.exception.SeatTypeInUseException
import team.aliens.dms.domain.studyroom.exception.SeatTypeNotFoundException
import team.aliens.dms.domain.studyroom.exception.TimeSlotAlreadyExistsException
import team.aliens.dms.domain.studyroom.exception.TimeSlotInUseException
import team.aliens.dms.domain.studyroom.model.AvailableTime
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.SeatType
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.StudyRoomTimeSlot
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import java.util.UUID

@Service
class CommandStudyRoomServiceImpl(
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandStudyRoomPort: CommandStudyRoomPort
) : CommandStudyRoomService {

    override fun saveStudyRoom(studyRoom: StudyRoom) =
        commandStudyRoomPort.saveStudyRoom(studyRoom)

    override fun saveSeatApplication(seatApplication: SeatApplication): SeatApplication {
        if (queryStudyRoomPort.existsSeatApplicationBySeatIdAndTimeSlotId(seatApplication.seatId, seatApplication.timeSlotId)) {
            throw SeatAlreadyAppliedException
        }
        commandStudyRoomPort.deleteSeatApplicationByStudentIdAndTimeSlotId(seatApplication.studentId, seatApplication.timeSlotId)
        return commandStudyRoomPort.saveSeatApplication(seatApplication)
    }

    override fun saveSeatType(seatType: SeatType): SeatType {
        if (queryStudyRoomPort.existsSeatTypeByNameAndSchoolId(seatType.name, seatType.schoolId)) {
            throw SeatTypeAlreadyExistsException
        }
        return commandStudyRoomPort.saveSeatType(seatType)
    }

    override fun saveTimeSlot(timeSlot: TimeSlot): TimeSlot {
        if (queryStudyRoomPort.existsTimeSlotByStartTimeAndEndTimeAndSchoolId(
                startTime = timeSlot.startTime,
                endTime = timeSlot.endTime,
                schoolId = timeSlot.schoolId
            )
        ) {
            throw TimeSlotAlreadyExistsException
        }
        return commandStudyRoomPort.saveTimeSlot(timeSlot)
    }

    override fun saveAllStudyRoomTimeSlots(studyRoomTimeSlots: List<StudyRoomTimeSlot>) =
        commandStudyRoomPort.saveAllStudyRoomTimeSlots(studyRoomTimeSlots)

    override fun saveAllSeats(seats: List<Seat>) =
        commandStudyRoomPort.saveAllSeats(seats)

    override fun saveAvailableTime(availableTime: AvailableTime) =
        commandStudyRoomPort.saveAvailableTime(availableTime)

    override fun deleteStudyRoom(studyRoomId: UUID) {
        commandStudyRoomPort.deleteStudyRoomTimeSlotByStudyRoomId(studyRoomId)
        commandStudyRoomPort.deleteSeatApplicationByStudyRoomId(studyRoomId)
        commandStudyRoomPort.deleteSeatByStudyRoomId(studyRoomId)
        commandStudyRoomPort.deleteStudyRoomById(studyRoomId)
    }

    override fun deleteTimeSlot(timeSlotId: UUID) {
        if (queryStudyRoomPort.existsSeatBySeatTypeId(timeSlotId)) {
            throw TimeSlotInUseException
        }
        commandStudyRoomPort.deleteSeatApplicationByTimeSlotId(timeSlotId)
        commandStudyRoomPort.deleteTimeSlotById(timeSlotId)
    }

    override fun deleteSeatApplication(studentId: UUID, seatId: UUID, timeSlotId: UUID) {
        commandStudyRoomPort.deleteSeatApplicationByStudentIdAndSeatIdAndTimeSlotId(studentId, seatId, timeSlotId)
    }

    override fun deleteSeatType(seatTypeId: UUID, schoolId: UUID) {
        val seatType = queryStudyRoomPort.querySeatTypeById(seatTypeId) ?: throw SeatTypeNotFoundException

        if (queryStudyRoomPort.existsSeatBySeatTypeId(seatTypeId)) {
            throw SeatTypeInUseException
        }

        commandStudyRoomPort.deleteSeatType(seatType)
    }

    override fun deleteSeatByStudyRoomId(studyRoomId: UUID) {
        commandStudyRoomPort.deleteSeatApplicationByStudyRoomId(studyRoomId)
        commandStudyRoomPort.deleteSeatByStudyRoomId(studyRoomId)
    }

    override fun deleteAllSeatApplications() {
        commandStudyRoomPort.deleteAllSeatApplications()
    }

    override fun deleteSeatApplicationBySeatIdAndStudentIdAndTimeSlotId(seatId: UUID, id: UUID, timeSlotId: UUID) {
        commandStudyRoomPort.deleteSeatApplicationBySeatIdAndStudentIdAndTimeSlotId(
            seatId = seatId,
            studentId = id,
            timeSlotId = timeSlotId
        )
    }

    override fun deleteStudyRoomTimeSlotByStudyRoomId(studyRoomId: UUID) {
        commandStudyRoomPort.deleteStudyRoomTimeSlotByStudyRoomId(studyRoomId)
    }

    override fun updateTimeSlotsByStudyRoom(studyRoomId: UUID, studyRoomTimeSlots: List<StudyRoomTimeSlot>) {
        commandStudyRoomPort.deleteStudyRoomTimeSlotByStudyRoomId(studyRoomId)
        commandStudyRoomPort.saveAllStudyRoomTimeSlots(studyRoomTimeSlots)
    }

    override fun updateSeatsByStudyRoom(studyRoomId: UUID, seats: List<Seat>) {
        commandStudyRoomPort.deleteSeatByStudyRoomId(studyRoomId)
        commandStudyRoomPort.saveAllSeats(seats)
    }
}
