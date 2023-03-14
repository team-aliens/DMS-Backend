package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.StudyRoomFacade
import team.aliens.dms.domain.studyroom.exception.AvailableTimeNotFoundException
import team.aliens.dms.domain.studyroom.exception.SeatAlreadyAppliedException
import team.aliens.dms.domain.studyroom.exception.SeatCanNotAppliedException
import team.aliens.dms.domain.studyroom.exception.SeatNotFoundException
import team.aliens.dms.domain.studyroom.exception.StudyRoomAvailableGradeMismatchException
import team.aliens.dms.domain.studyroom.exception.StudyRoomAvailableSexMismatchException
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryAvailableTimePort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryStudentPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.time.LocalTime
import java.util.UUID

@UseCase
class ApplySeatUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val queryStudentPort: StudyRoomQueryStudentPort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandStudyRoomPort: CommandStudyRoomPort,
    private val queryAvailableTimePort: QueryAvailableTimePort,
    private val studyRoomFacade: StudyRoomFacade
) {

    fun execute(seatId: UUID, timeSlotId: UUID?) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        studyRoomFacade.validateNullableTimeSlotId(timeSlotId, user.schoolId)

        val seat = queryStudyRoomPort.querySeatById(seatId) ?: throw SeatNotFoundException
        val studyRoom = queryStudyRoomPort.queryStudyRoomById(seat.studyRoomId) ?: throw StudyRoomNotFoundException

        validateSameSchool(studyRoom.schoolId, user.schoolId)
        validateStudyRoomAvailable(studyRoom, currentUserId)
        validateTimeAvailable(studyRoom.schoolId)

        if (queryStudyRoomPort.existsSeatApplicationBySeatIdAndTimeSlotId(seatId, timeSlotId)) {
            throw SeatAlreadyAppliedException
        }

        commandStudyRoomPort.deleteSeatApplicationByStudentId(currentUserId)

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

        if (studyRoom.availableGrade != 0 && studyRoom.availableGrade != student.grade) {
            throw StudyRoomAvailableGradeMismatchException
        }

        if (studyRoom.availableSex != Sex.ALL && studyRoom.availableSex != student.sex) {
            throw StudyRoomAvailableSexMismatchException
        }
    }

    private fun validateTimeAvailable(schoolId: UUID) {
        val now = LocalTime.now()
        val availableTime = queryAvailableTimePort.queryAvailableTimeBySchoolId(schoolId) ?: throw AvailableTimeNotFoundException

        if (now < availableTime.startAt || now > availableTime.endAt) {
            throw SeatCanNotAppliedException
        }
    }
}
