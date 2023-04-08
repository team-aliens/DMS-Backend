package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
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
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryStudentPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.util.UUID

@UseCase
class ApplySeatUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val queryStudentPort: StudyRoomQueryStudentPort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandStudyRoomPort: CommandStudyRoomPort,
    private val queryAvailableTimePort: QueryAvailableTimePort
) {

    fun execute(seatId: UUID, timeSlotId: UUID) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val seat = queryStudyRoomPort.querySeatById(seatId) ?: throw SeatNotFoundException
        val studyRoom = queryStudyRoomPort.queryStudyRoomById(seat.studyRoomId) ?: throw StudyRoomNotFoundException
        val timeSlot = queryStudyRoomPort.queryTimeSlotById(timeSlotId) ?: throw TimeSlotNotFoundException

        validateSameSchool(timeSlot.schoolId, user.schoolId)
        validateSameSchool(studyRoom.schoolId, user.schoolId)
        validateStudyRoomAvailable(studyRoom, currentUserId)
        validateTimeAvailable(studyRoom.schoolId)

        if (seat.status != SeatStatus.AVAILABLE) {
            throw SeatCanNotAppliedException
        }

        if (queryStudyRoomPort.existsSeatApplicationBySeatIdAndTimeSlotId(seatId, timeSlotId)) {
            throw SeatAlreadyAppliedException
        }

        commandStudyRoomPort.deleteSeatApplicationByStudentIdAndTimeSlotId(currentUserId, timeSlotId)

        commandStudyRoomPort.saveSeatApplication(
            SeatApplication(
                seatId = seatId,
                timeSlotId = timeSlotId,
                studentId = currentUserId
            )
        )
    }

    private fun validateStudyRoomAvailable(studyRoom: StudyRoom, currentUserId: UUID) {
        val student = queryStudentPort.queryStudentById(currentUserId) ?: throw StudentNotFoundException
        studyRoom.checkIsAvailableGradeAndSex(student.grade, student.sex)
    }

    private fun validateTimeAvailable(schoolId: UUID) {
        val availableTime = queryAvailableTimePort.queryAvailableTimeBySchoolId(schoolId) ?: throw AvailableTimeNotFoundException

        if (!availableTime.isAvailable()) {
            throw SeatCanNotAppliedException
        }
    }
}
