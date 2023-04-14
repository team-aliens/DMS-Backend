package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.studyroom.exception.AvailableTimeNotFoundException
import team.aliens.dms.domain.studyroom.exception.SeatAlreadyAppliedException
import team.aliens.dms.domain.studyroom.exception.SeatCanNotAppliedException
import team.aliens.dms.domain.studyroom.exception.SeatNotFoundException
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryAvailableTimePort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.user.service.GetUserService

@UseCase
class ApplySeatUseCase(
    private val getUserService: GetUserService,
    private val queryStudentPort: QueryStudentPort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandStudyRoomPort: CommandStudyRoomPort,
    private val queryAvailableTimePort: QueryAvailableTimePort
) {

    fun execute(seatId: UUID, timeSlotId: UUID) {

        val student = getUserService.getCurrentStudent()

        val seat = queryStudyRoomPort.querySeatById(seatId) ?: throw SeatNotFoundException
        val studyRoom = queryStudyRoomPort.queryStudyRoomById(seat.studyRoomId) ?: throw StudyRoomNotFoundException
        val timeSlot = queryStudyRoomPort.queryTimeSlotById(timeSlotId) ?: throw TimeSlotNotFoundException

        validateSameSchool(timeSlot.schoolId, student.schoolId)
        validateSameSchool(studyRoom.schoolId, student.schoolId)
        validateStudyRoomAvailable(studyRoom, student.id)
        validateTimeAvailable(studyRoom.schoolId)

        if (seat.status != SeatStatus.AVAILABLE) {
            throw SeatCanNotAppliedException
        }

        if (queryStudyRoomPort.existsSeatApplicationBySeatIdAndTimeSlotId(seatId, timeSlotId)) {
            throw SeatAlreadyAppliedException
        }

        commandStudyRoomPort.deleteSeatApplicationByStudentIdAndTimeSlotId(student.id, timeSlotId)

        commandStudyRoomPort.saveSeatApplication(
            SeatApplication(
                seatId = seatId,
                timeSlotId = timeSlotId,
                studentId = student.id
            )
        )
    }

    private fun validateStudyRoomAvailable(studyRoom: StudyRoom, currentUserId: UUID) {
        val student = queryStudentPort.queryStudentByUserId(currentUserId) ?: throw StudentNotFoundException
        studyRoom.checkIsAvailableGradeAndSex(student.grade, student.sex)
    }

    private fun validateTimeAvailable(schoolId: UUID) {
        val availableTime = queryAvailableTimePort.queryAvailableTimeBySchoolId(schoolId) ?: throw AvailableTimeNotFoundException

        if (!availableTime.isAvailable()) {
            throw SeatCanNotAppliedException
        }
    }
}
