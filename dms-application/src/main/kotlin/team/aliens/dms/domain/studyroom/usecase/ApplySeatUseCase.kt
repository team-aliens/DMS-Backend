package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.studyroom.exception.SeatAlreadyAppliedException
import team.aliens.dms.domain.studyroom.exception.SeatCanNotAppliedException
import team.aliens.dms.domain.studyroom.exception.SeatNotFoundException
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.time.LocalTime
import java.util.UUID
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.exception.StudyRoomAvailableGradeMismatchException
import team.aliens.dms.domain.studyroom.exception.StudyRoomAvailableSexMismatchException
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryStudentPort

@UseCase
class ApplySeatUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val queryStudentPort: StudyRoomQueryStudentPort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandStudyRoomPort: CommandStudyRoomPort,
    private val queryAvailableTimeUseCase: QueryAvailableTimeUseCase
) {

    fun execute(seatId: UUID) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException
        val student = queryStudentPort.queryStudentById(user.id) ?: throw StudentNotFoundException

        val seat = queryStudyRoomPort.querySeatById(seatId) ?: throw SeatNotFoundException
        val studyRoom = queryStudyRoomPort.queryStudyRoomById(seat.studyRoomId) ?: throw StudyRoomNotFoundException

        if (studyRoom.schoolId != user.schoolId) {
            throw SchoolMismatchException
        }

        if (studyRoom.availableGrade != 0) {
            if (studyRoom.availableGrade != student.grade) {
                throw StudyRoomAvailableGradeMismatchException
            }
        }

        if (studyRoom.availableSex != Sex.ALL) {
            if (studyRoom.availableSex != student.sex) {
                throw StudyRoomAvailableSexMismatchException
            }
        }

        val now = LocalTime.now()
        val availableTime = queryAvailableTimeUseCase.execute()

        if (now < availableTime.startAt || now > availableTime.endAt) {
            throw SeatCanNotAppliedException
        }

        if (seat.status != SeatStatus.AVAILABLE) {
            throw SeatCanNotAppliedException
        }

        val currentSeat = queryStudyRoomPort.querySeatByStudentId(currentUserId)
        currentSeat?.let {
            commandStudyRoomPort.saveSeat(
                it.unUse()
            )

            if (studyRoom.id != it.studyRoomId) {
                val currentStudyRoom = queryStudyRoomPort.queryStudyRoomById(it.studyRoomId)
                    ?: throw StudyRoomNotFoundException

                commandStudyRoomPort.saveStudyRoom(
                    currentStudyRoom.unApply()
                )
            }
        }

        val saveSeat = seat.studentId?.run {
            throw SeatAlreadyAppliedException
        } ?: run {
            seat.use(currentUserId)
        }
        commandStudyRoomPort.saveSeat(saveSeat)

        if (studyRoom.id != currentSeat?.studyRoomId) {
            commandStudyRoomPort.saveStudyRoom(
                studyRoom.apply()
            )
        }
    }
}