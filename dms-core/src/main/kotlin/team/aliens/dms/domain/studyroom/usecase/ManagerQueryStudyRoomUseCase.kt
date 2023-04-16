package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.studyroom.dto.ManagerQueryStudyRoomResponse
import team.aliens.dms.domain.studyroom.dto.ManagerQueryStudyRoomResponse.SeatElement
import team.aliens.dms.domain.studyroom.dto.ManagerQueryStudyRoomResponse.SeatElement.StudentElement
import team.aliens.dms.domain.studyroom.dto.ManagerQueryStudyRoomResponse.SeatElement.TypeElement
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.exception.StudyRoomTimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@ReadOnlyUseCase
class ManagerQueryStudyRoomUseCase(
    private val userService: UserService,
    private val queryStudyRoomPort: QueryStudyRoomPort
) {

    fun execute(studyRoomId: UUID, timeSlotId: UUID): ManagerQueryStudyRoomResponse {

        val user = userService.getCurrentUser()

        val studyRoom = queryStudyRoomPort.queryStudyRoomById(studyRoomId) ?: throw StudyRoomNotFoundException
        validateSameSchool(user.schoolId, studyRoom.schoolId)

        val timeSlot = queryStudyRoomPort.queryTimeSlotById(timeSlotId) ?: throw TimeSlotNotFoundException
        validateSameSchool(user.schoolId, timeSlot.schoolId)

        val timeSlots = queryStudyRoomPort.queryTimeSlotsBySchoolIdAndStudyRoomId(user.schoolId, studyRoom.id)
            .apply {
                if (none { it.id == timeSlotId }) {
                    throw StudyRoomTimeSlotNotFoundException
                }
            }

        val seats =
            queryStudyRoomPort.queryAllSeatApplicationVOsByStudyRoomIdAndTimeSlotId(studyRoomId, timeSlotId).map {
                SeatElement(
                    id = it.seatId,
                    widthLocation = it.widthLocation,
                    heightLocation = it.heightLocation,
                    number = it.number,
                    type = it.typeId?.run {
                        TypeElement(
                            id = it.typeId,
                            name = it.typeName!!,
                            color = it.typeColor!!
                        )
                    },
                    status = it.status,
                    student = it.studentId?.run {
                        StudentElement(
                            id = it.studentId,
                            name = it.studentName!!,
                            gcn = Student.processGcn(it.studentGrade!!, it.studentClassRoom!!, it.studentNumber!!),
                            profileImageUrl = it.studentProfileImageUrl!!
                        )
                    }
                )
            }

        return studyRoom.run {
            ManagerQueryStudyRoomResponse(
                floor = floor,
                name = name,
                timeSlots = timeSlots.map {
                    ManagerQueryStudyRoomResponse.TimeSlotElement(
                        id = it.id,
                        startTime = it.startTime,
                        endTime = it.endTime
                    )
                },
                totalAvailableSeat = availableHeadcount,
                availableSex = availableSex,
                availableGrade = availableGrade,
                eastDescription = eastDescription,
                westDescription = westDescription,
                southDescription = southDescription,
                northDescription = northDescription,
                totalWidthSize = widthSize,
                totalHeightSize = heightSize,
                seats = seats
            )
        }
    }
}
