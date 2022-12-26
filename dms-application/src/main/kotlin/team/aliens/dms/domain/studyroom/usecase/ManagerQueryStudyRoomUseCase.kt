package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.studyroom.dto.ManagerQueryStudyRoomResponse
import team.aliens.dms.domain.studyroom.dto.ManagerQueryStudyRoomResponse.SeatElement
import team.aliens.dms.domain.studyroom.dto.ManagerQueryStudyRoomResponse.SeatElement.StudentElement
import team.aliens.dms.domain.studyroom.dto.ManagerQueryStudyRoomResponse.SeatElement.TypeElement
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryStudentPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@ReadOnlyUseCase
class ManagerQueryStudyRoomUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val queryStudentPort: StudyRoomQueryStudentPort
) {

    fun execute(studyRoomId: UUID): ManagerQueryStudyRoomResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val studyRoom = queryStudyRoomPort.queryStudyRoomById(studyRoomId) ?: throw StudyRoomNotFoundException

        if (user.schoolId != studyRoom.schoolId) {
            throw SchoolMismatchException
        }

        val seats = queryStudyRoomPort.queryAllSeatsByStudyRoomId(studyRoom.id).map {
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
                    val student = queryStudentPort.queryStudentById(this)!!

                    StudentElement(
                        id = it.studentId,
                        name = it.studentName!!,
                        gcn = student.gcn,
                        profileImageUrl = student.profileImageUrl!!
                    )
                }
            )
        }

        return studyRoom.run {
            ManagerQueryStudyRoomResponse(
                floor = floor,
                name = name,
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