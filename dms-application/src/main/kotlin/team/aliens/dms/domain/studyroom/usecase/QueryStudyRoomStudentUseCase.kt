package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.QueryStudyRoomStudentResponse
import team.aliens.dms.domain.studyroom.dto.QueryStudyRoomStudentResponse.SeatElement
import team.aliens.dms.domain.studyroom.dto.QueryStudyRoomStudentResponse.SeatElement.StudentElement
import team.aliens.dms.domain.studyroom.dto.QueryStudyRoomStudentResponse.SeatElement.TypeElement
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.spi.QuerySeatTypePort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryStudentPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort

@ReadOnlyUseCase
class QueryStudyRoomStudentUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val querySeatTypePort: QuerySeatTypePort,
    private val queryStudentPort: StudyRoomQueryStudentPort
) {

    fun execute(studyRoomId: UUID): QueryStudyRoomStudentResponse {
        val currentUserId = securityPort.getCurrentUserId()

        val studyRoom = queryStudyRoomPort.queryStudyRoomById(studyRoomId) ?: throw StudyRoomNotFoundException
        val seatCount = queryStudyRoomPort.countSeatByStudyRoomId(studyRoomId)

        val seats = queryStudyRoomPort.queryAllSeatByStudyRoomId(studyRoomId).map {
            val seatType = querySeatTypePort.querySeatTypeId(it.typeId)
            val student = queryStudentPort.queryStudentByIdOrNull(it.studentId)

            SeatElement(
                id = it.id,
                widthSize = it.widthLocation,
                heightSize = it.heightLocation,
                number = it.number,
                type = seatType?.run {
                    TypeElement(
                        id = id,
                        name = name,
                        color = color
                    )
                },
                status = it.status,
                isMine = isMine(it.studentId, currentUserId),
                student = student?.run {
                    StudentElement(
                        id = id,
                        name = name
                    )
                }
            )
        }

        return studyRoom.run {
            QueryStudyRoomStudentResponse(
                totalAvailableSeat = seatCount,
                inUseHeadcount = inUseHeadcount!!,
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

    private fun isMine(studentId: UUID?, currentUserId: UUID): Boolean? {
        studentId?.run {
            return studentId == currentUserId
        } ?: run {
            return null
        }
    }
}